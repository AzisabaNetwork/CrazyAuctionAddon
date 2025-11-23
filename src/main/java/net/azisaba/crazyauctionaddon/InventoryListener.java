package net.azisaba.crazyauctionaddon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
        int topSize = e.getView().getTopInventory().getSize();

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) clicked = new ItemStack(Material.AIR);

        // =======================================================
        // 画面1：CAA 設定
        // =======================================================
        if (title.equals("CAA 設定")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                ItemStack cursor = e.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    ItemStack put = cursor.clone();
                    e.getInventory().setItem(4, put);
                    p.setItemOnCursor(null);

                    // 既に出品不可に設定されている場合は画面3へ
                    if (blockedItemsManager.isBlocked(put)) {
                        guiManager.openBlockGUI(p, put);
                    } else {
                        guiManager.openSetGUI(p, put);
                    }
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }
            return;
        }

        // =======================================================
        // 画面2：出品可能設定
        // =======================================================
        if (title.startsWith("CAA 設定 - 出品設定")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                guiManager.openConfigGUI(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            if (raw == 8 && clicked.getType() == Material.EMERALD_BLOCK) {
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.add(item); // 永続化も内部で行う
                guiManager.openBlockGUI(p, item);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }
        }

        // =======================================================
        // 画面3：出品不可設定
        // =======================================================
        if (title.startsWith("CAA 設定 - 出品不可")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                guiManager.openConfigGUI(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            if (raw == 8 && clicked.getType() == Material.REDSTONE_BLOCK) {
                ItemStack item = e.getInventory().getItem(4).clone();
                blockedItemsManager.remove(item); // 永続化も内部で行う
                guiManager.openSetGUI(p, item);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }
        }

        // =======================================================
        // 出品不可リスト（完全操作禁止）
        // =======================================================
        if (title.startsWith("出品不可リスト - ページ")) {
            if (raw < topSize) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        String title = e.getView().getTitle();
        int topSize = e.getView().getTopInventory().getSize();

        if (title.startsWith("CAA 設定") || title.startsWith("出品不可リスト")) {
            for (int slot : e.getRawSlots()) {
                if (slot < topSize) {
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage().toLowerCase();

        if (msg.startsWith("/ah sell")) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (item != null && blockedItemsManager.isBlocked(item)) {
                    e.setCancelled(true);
                    p.sendMessage("§cこのアイテムを持っている間は /ah sell を使用できません。");
                    return;
                }
            }
        }
    }

    // =======================================================
    // GUIを閉じたときのみアイテム返却
    // =======================================================
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        String title = e.getView().getTitle();

        if (title.equals("CAA 設定") ||
                title.startsWith("CAA 設定 - 出品設定") ||
                title.startsWith("CAA 設定 - 出品不可")) {

            ItemStack item = e.getInventory().getItem(4);
            if (item != null && item.getType() != Material.AIR && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                p.getInventory().addItem(item); // ガラスは返さない
                e.getInventory().setItem(4, null);
            }
        }
    }
}
