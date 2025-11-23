package net.azisaba.crazyauctionaddon;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        this.file = new File(plugin.getDataFolder(), "blocked-items.yml");

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void load() {
        blockedItems.clear();
        List<Map<?, ?>> items = config.getMapList("blocked-items");
        for (Map<?, ?> map : items) {
            Material mat = Material.getMaterial((String) map.get("material"));
            if (mat == null) continue;

            int amount = 1;
            Object amtObj = map.get("amount");
            if (amtObj instanceof Number) amount = ((Number) amtObj).intValue();

            ItemStack item = new ItemStack(mat, amount);

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (map.containsKey("name")) meta.setDisplayName((String) map.get("name"));
                if (map.containsKey("lore")) meta.setLore((List<String>) map.get("lore"));
                if (map.containsKey("enchants")) {
                    Map<String, Object> enchants = (Map<String, Object>) map.get("enchants");
                    for (Map.Entry<String, Object> entry : enchants.entrySet()) {
                        Enchantment ench = Enchantment.getByName(entry.getKey());
                        if (ench != null) meta.addEnchant(ench, ((Number) entry.getValue()).intValue(), true);
                    }
                }
                item.setItemMeta(meta);
            }
            blockedItems.add(item);
        }
    }

    public void save() {
        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemStack item : blockedItems) {
            if (item == null || item.getType() == Material.AIR) continue;

            Map<String, Object> map = new HashMap<>();
            map.put("material", item.getType().name());
            map.put("amount", item.getAmount());

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (meta.hasDisplayName()) map.put("name", meta.getDisplayName());
                if (meta.hasLore()) map.put("lore", meta.getLore());

                if (meta.hasEnchants()) {
                    Map<String, Integer> enchants = new HashMap<>();
                    meta.getEnchants().forEach((ench, level) -> enchants.put(ench.getName(), level));
                    map.put("enchants", enchants);
                }
            }
            items.add(map);
        }
        config.set("blocked-items", items);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(ItemStack item) {
        blockedItems.removeIf(bi -> isSameItem(bi, item)); // 一度消してから追加
        blockedItems.add(item.clone());
        save();
    }

    public void remove(ItemStack item) {
        blockedItems.removeIf(bi -> isSameItem(bi, item));
        save();
    }

    public boolean isBlocked(ItemStack item) {
        for (ItemStack bi : blockedItems) {
            if (isSameItem(bi, item)) return true;
        }
        return false;
    }

    public List<ItemStack> getBlockedItems() {
        return new ArrayList<>(blockedItems);
    }

    private boolean isSameItem(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getType() != b.getType()) return false;

        ItemMeta am = a.getItemMeta();
        ItemMeta bm = b.getItemMeta();

        if (am == null && bm != null) return false;
        if (bm == null && am != null) return false;
        if (am != null && bm != null) {
            if (!Objects.equals(am.getDisplayName(), bm.getDisplayName())) return false;
            if (!Objects.equals(am.getLore(), bm.getLore())) return false;
            if (!am.getEnchants().equals(bm.getEnchants())) return false;
        }

        return true;
    }
}
