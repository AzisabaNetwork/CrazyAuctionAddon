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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // プレイヤーチェック
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cこのコマンドはゲーム内から実行してください");
            return true;
        }
        Player p = (Player) sender;

        // 権限チェック（OP または permission 所持）
        if (!p.hasPermission("crazyauctionaddon.use") && !p.isOp()) {
            p.sendMessage("§c権限がありません");
            return true;
        }

        // 引数なし → ヘルプ
        if (args.length == 0) {
            sendHelp(p);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "set":
                guiManager.openConfigGUI(p);
                return true;

            case "noselllist":
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException ex) {
                        p.sendMessage("§cページ番号が不正です");
                        return true;
                    }
                }
                guiManager.openNoSellList(p, page);
                return true;

            default:
                p.sendMessage("§c不明なサブコマンドです");
                sendHelp(p);
                return true;
        }
    }

    private void sendHelp(Player p) {
        p.sendMessage("§e==== CrazyAuctionAddon ====");
        p.sendMessage("§e/caa set §7- 出品設定GUIを開く");
        p.sendMessage("§e/caa noselllist [ページ] §7- 出品不可一覧を開く");
    }
}
