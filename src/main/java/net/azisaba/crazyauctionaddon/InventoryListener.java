package net.azisaba.crazyauctionaddon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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

        // ===========================
        // 画面1：CAA 設定
        // ===========================
        if (title.equals("CAA 設定")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                ItemStack cursor = e.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    ItemStack put = cursor.clone();
                    e.getInventory().setItem(4, put);
                    p.setItemOnCursor(null);

                    // 既に出品不可なら画面3、そうでなければ画面2
                    if (blockedItemsManager.isBlockedIgnoreAmount(put)) {
                        guiManager.openBlockGUI(p, put);
                    } else {
                        guiManager.openSetGUI(p, put);
                    }
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }
            return;
        }

        // ===========================
        // 画面2：出品可能設定
        // ===========================
        if (title.startsWith("CAA 設定 - 出品可能")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                guiManager.openConfigGUI(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            // 8番：緑ブロック → 出品不可に設定
            if (raw == 8 && clicked.getType() == Material.EMERALD_BLOCK) {
                ItemStack item = e.getInventory().getItem(4);
                if (item != null) {
                    blockedItemsManager.add(item.clone()); // 出品不可に設定
                    guiManager.openBlockGUI(p, item.clone()); // 画面3へ
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
                return;
            }
        }

        // ===========================
        // 画面3：出品不可設定
        // ===========================
        if (title.startsWith("CAA 設定 - 出品不可")) {
            if (raw < topSize) e.setCancelled(true);

            if (raw == 4) {
                guiManager.openConfigGUI(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            // 8番：赤ブロック → 出品可能に戻す
            if (raw == 8 && clicked.getType() == Material.REDSTONE_BLOCK) {
                ItemStack item = e.getInventory().getItem(4);
                if (item != null) {
                    blockedItemsManager.remove(item.clone()); // 出品可能に戻す
                    guiManager.openSetGUI(p, item.clone()); // 画面2へ
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
                return;
            }
        }

        // ===========================
        // 出品不可リスト（完全操作禁止）
        // ===========================
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

    // ===========================
    // /ah sell, /auction sell, /auctionhouse sell 対応
    // ===========================
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage().toLowerCase();

        if (msg.startsWith("/ah sell") || msg.startsWith("/auction sell") || msg.startsWith("/auctionhouse sell")) {
            ItemStack main = p.getInventory().getItemInMainHand();
            ItemStack off = p.getInventory().getItemInOffHand();

            if ((main != null && main.getType() != Material.AIR && blockedItemsManager.isBlockedIgnoreAmount(main)) ||
                    (off != null && off.getType() != Material.AIR && blockedItemsManager.isBlockedIgnoreAmount(off))) {
                e.setCancelled(true);
                p.sendMessage("§c手に持っているアイテムは出品できません");
            }
        }
    }

}
