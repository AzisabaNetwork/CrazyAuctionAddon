package net.azisaba.crazyauctionaddon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIManager {

    private final CrazyAuctionAddon plugin;
    private final BlockedItemsManager manager;

    public GUIManager(CrazyAuctionAddon plugin, BlockedItemsManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void openConfigGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定");

        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gm = gray.getItemMeta();
        gm.setDisplayName("§7ここにアイテムをドロップ");
        gray.setItemMeta(gm);
        inv.setItem(4, gray);

        ItemStack stone = new ItemStack(Material.STONE);
        ItemMeta sm = stone.getItemMeta();
        sm.setDisplayName("§7");
        stone.setItemMeta(sm);
        inv.setItem(8, stone);

        p.openInventory(inv);
    }

    public void openSetGUI(Player p, ItemStack item) {
        // すでに出品不可なら画面3から
        if (manager.isBlocked(item)) {
            openBlockGUI(p, item);
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品設定");
        inv.setItem(4, item);

        ItemStack emerald = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta em = emerald.getItemMeta();
        em.setDisplayName("§a出品可能");
        emerald.setItemMeta(em);
        inv.setItem(8, emerald);

        p.openInventory(inv);
    }

    public void openBlockGUI(Player p, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品不可");
        inv.setItem(4, item.clone());

        ItemStack red = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta rm = red.getItemMeta();
        rm.setDisplayName("§c出品不可");
        red.setItemMeta(rm);
        inv.setItem(8, red);

        p.openInventory(inv);
    }

    public void openNoSellList(Player p, int page) {
        List<ItemStack> items = manager.getBlockedItems();
        int perPage = 54;
        int maxPage = (items.size() + perPage - 1) / perPage;
        if (maxPage <= 0) maxPage = 1;
        if (page < 1) page = 1;
        if (page > maxPage) page = maxPage;

        Inventory inv = Bukkit.createInventory(null, 54, "出品不可リスト - ページ " + page + "/" + maxPage);

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, items.size());
        int slotIndex = 0;
        for (int i = start; i < end; i++) {
            inv.setItem(slotIndex, items.get(i).clone());
            slotIndex++;
        }

        p.openInventory(inv);
    }

}
