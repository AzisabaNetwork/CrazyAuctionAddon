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

    // 画面1: CAA 設定
    public void openConfigGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定");

        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gm = gray.getItemMeta();
        gm.setDisplayName("§7ここにアイテムをドロップ");
        gray.setItemMeta(gm);
        inv.setItem(4, gray);

        p.openInventory(inv);
    }

    // 画面2: 出品可能設定
    public void openSetGUI(Player p, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品可能");
        inv.setItem(4, item.clone());

        ItemStack green = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta gm = green.getItemMeta();
        gm.setDisplayName("§c出品不可にする");
        green.setItemMeta(gm);
        inv.setItem(8, green);

        p.openInventory(inv);
    }

    // 画面3: 出品不可設定
    public void openBlockGUI(Player p, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 9, "CAA 設定 - 出品不可");
        inv.setItem(4, item.clone());

        ItemStack red = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta rm = red.getItemMeta();
        rm.setDisplayName("§a出品可能に戻す");
        red.setItemMeta(rm);
        inv.setItem(8, red);

        p.openInventory(inv);
    }

    // 出品不可リスト
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
