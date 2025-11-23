package net.azisaba.crazyauctionaddon;

import org.bukkit.plugin.java.JavaPlugin;

public class CrazyAuctionAddon extends JavaPlugin {

    private static CrazyAuctionAddon instance;
    private BlockedItemsManager blockedItemsManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;

        try {
            saveDefaultConfig();
        } catch (IllegalArgumentException ignored) {
            getLogger().warning("config.yml が見つかりません。デフォルト設定は読み込まれません。");
        }

        blockedItemsManager = new BlockedItemsManager(this);
        blockedItemsManager.load();

        guiManager = new GUIManager(this, blockedItemsManager);

        getCommand("caa").setExecutor(new CAACommand(this, guiManager, blockedItemsManager));
        getCommand("caa").setTabCompleter(new CAATabCompleter());

        getServer().getPluginManager().registerEvents(
                new InventoryListener(this, guiManager, blockedItemsManager), this);

        getServer().getPluginManager().registerEvents(
                new SellBlockerListener(blockedItemsManager), this);

        getLogger().info("CrazyAuctionAddon enabled");
    }

    @Override
    public void onDisable() {
        blockedItemsManager.save();
    }

    public static CrazyAuctionAddon getInstance() {
        return instance;
    }
}
