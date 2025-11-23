package net.azisaba.crazyauctionaddon;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyAuctionAddon extends JavaPlugin {

    private BlockedItemsManager blockedItemsManager;
    private GUIManager guiManager;
    private InventoryListener inventoryListener;

    @Override
    public void onEnable() {

        // Configロード/作成
        saveDefaultConfig();

        // マネージャー類の初期化（順番重要）
        blockedItemsManager = new BlockedItemsManager(this);
        blockedItemsManager.load(); // ← blocked-items.yml 読み込み

        guiManager = new GUIManager(this, blockedItemsManager);

        // コマンド登録
        getCommand("caa").setExecutor(new CAACommand(this, guiManager, blockedItemsManager));
        getCommand("caa").setTabCompleter(new CAATabCompleter());

        // Listener 登録
        registerListeners();

        getLogger().info("CrazyAuctionAddon Enabled");
    }

    @Override
    public void onDisable() {
        // データ保存
        if (blockedItemsManager != null) blockedItemsManager.save();

        // リスナー解除
        unregisterListeners();

        getLogger().info("CrazyAuctionAddon Disabled");
    }

    /** リスナー登録 */
    public void registerListeners() {
        if (inventoryListener != null) HandlerList.unregisterAll(inventoryListener);
        inventoryListener = new InventoryListener(this, guiManager, blockedItemsManager);
        getServer().getPluginManager().registerEvents(inventoryListener, this);
    }

    /** リスナー解除 */
    public void unregisterListeners() {
        if (inventoryListener != null) HandlerList.unregisterAll(inventoryListener);
    }

    /** config.yml と blocked-items.yml をリロード */
    public void reloadAll() {
        reloadConfig(); // config.yml
        if (blockedItemsManager != null) blockedItemsManager.reloadConfig(); // blocked-items.yml
        registerListeners(); // リスナー再登録
        getLogger().info("CrazyAuctionAddon: Configs reloaded.");
    }

    // ゲッター
    public BlockedItemsManager getBlockedItemsManager() {
        return blockedItemsManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }
}
