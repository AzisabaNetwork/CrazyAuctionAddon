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
        // blockedItems が Set<String>（Material 名）を返す前提で実装
        // 1ページあたりの表示数（54スロット全体を使う場合）
        int perPage = 54;
        java.util.List<String> items = new java.util.ArrayList<>(manager.getBlockedItems()); // Set -> List

        int maxPage = (items.size() + perPage - 1) / perPage;
        if (maxPage <= 0) maxPage = 1;
        if (page < 1) page = 1;
        if (page > maxPage) page = maxPage;

        Inventory inv = Bukkit.createInventory(null, 54, "出品不可リスト - ページ " + page + "/" + maxPage);

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, items.size());

        int slotIndex = 0;
        for (int i = start; i < end; i++) {
            String matName = items.get(i);
            Material mat = Material.getMaterial(matName);
            ItemStack is;
            if (mat != null) {
                is = new ItemStack(mat);
            } else {
                // Material が見つからない場合は名前表示用の紙などに置き換える（安全策）
                is = new ItemStack(Material.PAPER);
                org.bukkit.inventory.meta.ItemMeta im = is.getItemMeta();
                im.setDisplayName(matName);
                is.setItemMeta(im);
            }
            inv.setItem(slotIndex, is);
            slotIndex++;
        }

        // ページ移動用のボタン（任意）
        // 例: 左上に戻る/次へを置きたい場合はここで setItem する

        p.openInventory(inv);
    }
}
