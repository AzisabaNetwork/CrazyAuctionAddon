package net.azisaba.crazyauctionaddon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIManager {

    private final CrazyAuctionAddon plugin;
    private final BlockedItemsManager manager;

    public GUIManager(CrazyAuctionAddon plugin, BlockedItemsManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    // =========================================================
    // 画面1（設定画面）
    // =========================================================
    public void openConfigGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定");

        // slot4 → アイテムをドロップする場所
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gm = gray.getItemMeta();
        gm.setDisplayName("§7ここにアイテムをドロップ");
        gray.setItemMeta(gm);
        inv.setItem(4, gray);

        // slot8 → 装飾（無効）
        ItemStack stone = new ItemStack(Material.STONE);
        ItemMeta sm = stone.getItemMeta();
        sm.setDisplayName("§7");
        stone.setItemMeta(sm);
        inv.setItem(8, stone);

        p.openInventory(inv);
    }

    // =========================================================
    // 画面2（出品可能設定）
    // =========================================================
    public void openSetGUI(Player p, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品設定");

        inv.setItem(4, item);

        ItemStack emerald = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta em = emerald.getItemMeta();
        em.setDisplayName("§a出品可能");
        emerald.setItemMeta(em);
        inv.setItem(8, emerald);

        p.openInventory(inv);
    }

    // =========================================================
    // 画面3（出品不可設定）
    // =========================================================
    public void openBlockGUI(Player p, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品不可");

        inv.setItem(4, item);

        ItemStack red = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta rm = red.getItemMeta();
        rm.setDisplayName("§c出品不可");
        red.setItemMeta(rm);
        inv.setItem(8, red);

        p.openInventory(inv);
    }

    // =========================================================
    // 出品不可リスト（ページ付き）
    // =========================================================
    public void openNoSellList(Player p, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "出品不可リスト - ページ " + page);

        int start = (page - 1) * 45;
        int end = Math.min(start + 45, manager.getBlockedItems().size());

        for (int i = start; i < end; i++) {
            inv.setItem(i - start, manager.getBlockedItems().get(i));
        }

        p.openInventory(inv);
    }
}
