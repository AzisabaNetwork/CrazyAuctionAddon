package net.azisaba.crazyauctionaddon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.StringWriter;

public class ItemSerializer {

    public static String serialize(ItemStack item) {
        if (item == null) return null;
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", item);
        return config.saveToString();
    }

    public static ItemStack deserialize(String data) {
        if (data == null) return null;
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(data);
            return config.getItemStack("item");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
