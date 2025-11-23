package net.azisaba.crazyauctionaddon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {


    private final CrazyAuctionAddon plugin;
    private final GUIManager guiManager;
    private final BlockedItemsManager blockedItemsManager;

    public InventoryListener(CrazyAuctionAddon plugin, GUIManager guiManager, BlockedItemsManager blockedItemsManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.blockedItemsManager = blockedItemsManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) return;

        String title = e.getView().getTitle();
        int slot = e.getRawSlot();

        // -------------------------------------------------
        // 画面1（最初のSET画面）
        // -------------------------------------------------
        if (title.equals("CAA 設定")) {
            // スロット4（灰色ガラス）をクリック → 無効
            if (slot == 4) {
                e.setCancelled(true);
                return;
            }
            // スロット8（石ブロック）も無効
            if (slot == 8) {
                e.setCancelled(true);
                return;
            }

            // ドロップされたアイテムをセットして画面2へ
            ItemStack cursor = e.getCursor();
            if (cursor != null && cursor.getType() != Material.AIR) {
                e.setCancelled(true);
                p.setItemOnCursor(null);
                guiManager.openSetGUI(p, cursor.clone());
            }
        }

        // -------------------------------------------------
        // 画面2（エメラルド：出品可能）
        // -------------------------------------------------
        else if (title.startsWith("CAA 設定 - 出品設定")) {
            // アイテムスロット4クリック → 取り出して画面1へ
            if (slot == 4) {
                e.setCancelled(true);
                ItemStack item = clicked.clone();
                p.getInventory().addItem(item);
                guiManager.openConfigGUI(p);
            }
            // エメラルドスロット8クリック → 出品不可にして画面3へ
            else if (slot == 8 && clicked.getType() == Material.EMERALD_BLOCK) {
                e.setCancelled(true);
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.add(item);
                guiManager.openBlockGUI(p, item);
            } else {
                e.setCancelled(true);
            }
        }

        // -------------------------------------------------
        // 画面3（赤石：出品不可）
        // -------------------------------------------------
        else if (title.startsWith("CAA 設定 - 出品不可")) {
            // アイテムスロット4クリック → 取り出して画面1へ
            if (slot == 4) {
                e.setCancelled(true);
                ItemStack item = clicked.clone();
                p.getInventory().addItem(item);
                guiManager.openConfigGUI(p);
            }
            // 赤石スロット8クリック → 再び出品可能にして画面2へ
            else if (slot == 8 && clicked.getType() == Material.REDSTONE_BLOCK) {
                e.setCancelled(true);
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.remove(item);
                guiManager.openSetGUI(p, item);
            } else {
                e.setCancelled(true);
            }
        }
    }

    // -------------------------------------------------
    // インベントリドラッグを防ぐ
    // -------------------------------------------------
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        String title = e.getView().getTitle();
        if (title.startsWith("CAA 設定")) {
            e.setCancelled(true);
        }
    }
}
