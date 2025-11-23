package net.azisaba.crazyauctionaddon;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockedItemsManager {

    private final CrazyAuctionAddon plugin;
    private final File file;
    private FileConfiguration config;

    private final List<ItemStack> blockedItems = new ArrayList<>();

    public BlockedItemsManager(CrazyAuctionAddon plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "blocked.yml");
    }

    public void load() {
        if (!file.exists()) {
            plugin.saveResource("blocked.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);

        blockedItems.clear();

        if (config.contains("items")) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                String serialized = config.getString("items." + key);
                ItemStack item = ItemSerializer.deserialize(serialized);
                if (item != null)
                    blockedItems.add(item);
            }
        }
    }

    public void save() {
        config.set("items", null);
        int index = 0;
        for (ItemStack item : blockedItems) {
            config.set("items." + index, ItemSerializer.serialize(item));
            index++;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBlocked(ItemStack item) {
        if (item == null) return false;

        for (ItemStack blocked : blockedItems) {
            if (blocked.isSimilar(item) &&
                    blocked.getAmount() == item.getAmount()) {
                return true;
            }
        }
        return false;
    }

    public void add(ItemStack item) {
        blockedItems.add(item.clone());
        save();
    }

    public void remove(ItemStack item) {
        blockedItems.removeIf(blocked ->
                blocked.isSimilar(item) &&
                        blocked.getAmount() == item.getAmount());
        save();
    }

    public List<ItemStack> getBlockedItems() {
        return blockedItems;
    }
}
