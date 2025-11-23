package net.azisaba.crazyauctionaddon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CAACommand implements CommandExecutor {

    private final CrazyAuctionAddon plugin;
    private final GUIManager guiManager;
    private final BlockedItemsManager blockedItemsManager;

    public CAACommand(CrazyAuctionAddon plugin, GUIManager guiManager, BlockedItemsManager blockedItemsManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.blockedItemsManager = blockedItemsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("ゲーム内で実行してください");
            return true;
        }

        Player p = (Player) sender;

        // 権限チェック
        if (!p.hasPermission("crazyauctionaddon.use")) {
            p.sendMessage("§c権限がありません");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§e/caa set §7- 出品設定GUIを開く");
            p.sendMessage("§e/caa noselllist [ページ] §7- 出品不可リストを見る");
            p.sendMessage("§e/caa reload §7- プラグイン設定をリロード");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "set":
                guiManager.openConfigGUI(p);
                break;

            case "noselllist":
                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage("§cページ番号が不正です");
                        return true;
                    }
                }
                guiManager.openNoSellList(p, page);
                break;

            case "reload":
                if (!p.hasPermission("crazyauctionaddon.reload")) {
                    p.sendMessage("§c権限がありません");
                    return true;
                }
                plugin.reloadAll(); // config と blocked-items をリロード
                p.sendMessage("§aCrazyAuctionAddon 設定をリロードしました");
                break;

            default:
                p.sendMessage("§c不明なサブコマンドです");
                break;
        }

        return true;
    }
}
