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

        String title = e.getView().getTitle();
        int raw = e.getRawSlot();

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) clicked = new ItemStack(Material.AIR);

        // =======================================================
        // 画面1：CAA 設定
        // =======================================================
        if (title.equals("CAA 設定")) {
            e.setCancelled(true); // 他はすべて禁止

            // slot4 にアイテムを置く処理
            if (raw == 4) {
                ItemStack cursor = e.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    // カーソルのアイテムをセット
                    ItemStack put = cursor.clone();
                    e.getInventory().setItem(4, put);
                    p.setItemOnCursor(null);

                    // 画面2へ
                    guiManager.openSetGUI(p, put);
                }
            }
            return;
        }

        // =======================================================
        // 画面2：出品可能設定
        // =======================================================
        if (title.startsWith("CAA 設定 - 出品設定")) {
            e.setCancelled(true); // 基本禁止

            // slot4 → アイテムを回収して画面1へ
            if (raw == 4) {
                ItemStack item = clicked.clone();
                p.getInventory().addItem(item);
                guiManager.openConfigGUI(p);
                return;
            }

            // slot8 → 出品不可へ移行（赤石画面へ）
            if (raw == 8 && clicked.getType() == Material.EMERALD_BLOCK) {
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.add(item);
                guiManager.openBlockGUI(p, item);
                return;
            }

            return;
        }

        // =======================================================
        // 画面3：出品不可設定
        // =======================================================
        if (title.startsWith("CAA 設定 - 出品不可")) {
            e.setCancelled(true); // 基本禁止

            // slot4 → アイテム回収して画面1へ
            if (raw == 4) {
                ItemStack item = clicked.clone();
                p.getInventory().addItem(item);
                guiManager.openConfigGUI(p);
                return;
            }

            // slot8 → 出品可能に戻す（エメラルド画面へ）
            if (raw == 8 && clicked.getType() == Material.REDSTONE_BLOCK) {
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.remove(item);
                guiManager.openSetGUI(p, item);
                return;
            }

            return;
        }

        // =======================================================
        // 出品不可リスト（完全操作禁止）
        // =======================================================
        if (title.startsWith("出品不可リスト - ページ")) {
            e.setCancelled(true); // 完全ロック
        }
    }

    // =======================================================
    // ドラッグすべて禁止
    // =======================================================
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        String title = e.getView().getTitle();
        if (title.startsWith("CAA 設定") || title.startsWith("出品不可リスト")) {
            e.setCancelled(true);
        }
    }
}
