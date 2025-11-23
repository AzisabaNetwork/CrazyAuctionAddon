package net.azisaba.crazyauctionaddon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CAATabCompleter implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("set", "noselllist");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            String current = args[0].toLowerCase();
            for (String sub : subCommands) {
                if (sub.startsWith(current)) {
                    result.add(sub);
                }
            }
        }
        return result;
    }
}
