package net.azisaba.crazyauctionaddon;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockedItemsManager {

    private final CrazyAuctionAddon plugin;
    private final Set<String> blockedItems = new HashSet<>();

    public BlockedItemsManager(CrazyAuctionAddon plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        blockedItems.clear();

        FileConfiguration config = plugin.getConfig();
        if (config.contains("blocked-items")) {
            blockedItems.addAll(config.getStringList("blocked-items"));
        }
    }

    public void save() {
        FileConfiguration config = plugin.getConfig();
        List<String> saveList = new ArrayList<>(blockedItems);
        config.set("blocked-items", saveList);
        plugin.saveConfig();
    }

    /** アイテムがブロック対象か調べる */
    public boolean isBlocked(ItemStack item) {
        if (item == null) return false;

        Material type = item.getType();
        return blockedItems.contains(type.name());
    }

    /** アイテムをブロックリストに追加 */
    public void add(ItemStack item) {
        if (item == null) return;
        blockedItems.add(item.getType().name());
        save();
    }

    /** ブロックリストから削除 */
    public void remove(ItemStack item) {
        if (item == null) return;
        blockedItems.remove(item.getType().name());
        save();
    }

    /** 全リスト取得 */
    public Set<String> getBlockedItems() {
        return blockedItems;
    }
}
