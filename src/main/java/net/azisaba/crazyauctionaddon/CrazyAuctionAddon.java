package net.azisaba.crazyauctionaddon;

import org.bukkit.plugin.java.JavaPlugin;

public class CrazyAuctionAddon extends JavaPlugin {

    private BlockedItemsManager blockedItemsManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {

        // Configロード/作成
        saveDefaultConfig();

        // マネージャー類の初期化（順番重要）
        blockedItemsManager = new BlockedItemsManager(this);
        blockedItemsManager.load(); // ← config から読み込み

        guiManager = new GUIManager(this, blockedItemsManager);

        // コマンド登録
        getCommand("caa").setExecutor(
                new CAACommand(this, guiManager, blockedItemsManager)
        );

        // Listener 登録
        getServer().getPluginManager().registerEvents(
                new InventoryListener(this, guiManager, blockedItemsManager),
                this
        );

        getLogger().info("CrazyAuctionAddon Enabled");

        this.getCommand("caa").setExecutor(new CAACommand(this, guiManager, blockedItemsManager));
        this.getCommand("caa").setTabCompleter(new CAATabCompleter());
    }

    @Override
    public void onDisable() {
        blockedItemsManager.save(); // ← config へ保存
    }
}
