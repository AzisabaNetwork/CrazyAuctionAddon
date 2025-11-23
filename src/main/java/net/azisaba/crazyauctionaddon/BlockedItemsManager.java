package net.azisaba.crazyauctionaddon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BlockedItemsManager {

    private final JavaPlugin plugin;
    private final List<ItemStack> blockedItems;

    public BlockedItemsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.blockedItems = new ArrayList<>();
        load();
    }

    // ブロックリストに追加
    public void add(ItemStack item) {
        blockedItems.add(item.clone());
        save();
    }

    // ブロックリストから削除
    public void remove(ItemStack item) {
        blockedItems.removeIf(i -> i.isSimilar(item));
        save();
    }

    // ブロックリストを返す
    public List<ItemStack> getBlockedItems() {
        return new ArrayList<>(blockedItems);
    }

    // アイテムがブロックリストにあるか
    public boolean isBlocked(ItemStack item) {
        for (ItemStack i : blockedItems) {
            if (i.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    // 設定から読み込む
    public void load() {
        // ここに config.yml などから読み込む処理を追加
    }

    // 設定に保存する
    public void save() {
        // ここに config.yml などに保存する処理を追加
    }
}
