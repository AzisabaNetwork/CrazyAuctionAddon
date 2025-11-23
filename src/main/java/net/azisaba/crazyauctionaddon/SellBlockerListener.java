package net.azisaba.crazyauctionaddon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

public class SellBlockerListener implements Listener {

    private final BlockedItemsManager manager;

    public SellBlockerListener(BlockedItemsManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage().toLowerCase();

        if (!(msg.startsWith("/ah") || msg.startsWith("/auctionhouse"))) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        // if (manager.isBlocked(item)) {
           // e.setCancelled(true);
           // p.sendMessage("§cこのアイテムは出品できません！");
        //}
        if (blockedItemsManager.isBlocked(item)) {
            event.setCancelled(true);
            player.sendMessage("§cこのアイテムは出品できません！");
        }

    }
}
