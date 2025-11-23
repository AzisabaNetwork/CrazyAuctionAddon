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
        blockedItemsManager.load(); // ← config から読み込み

        guiManager = new GUIManager(this, blockedItemsManager);

        // コマンド登録
        getCommand("caa").setExecutor(new CAACommand(this, guiManager, blockedItemsManager));
        getCommand("caa").setTabCompleter(new CAATabCompleter());

        // Listener 登録
        inventoryListener = new InventoryListener(this, guiManager, blockedItemsManager);
        getServer().getPluginManager().registerEvents(inventoryListener, this);

        getLogger().info("CrazyAuctionAddon Enabled");
    }

    @Override
    public void onDisable() {
        // データ保存
        if (blockedItemsManager != null) blockedItemsManager.save();

        // リスナー解除
        if (inventoryListener != null) {
            HandlerList.unregisterAll(inventoryListener);
        }

        getLogger().info("CrazyAuctionAddon Disabled");
    }
}
