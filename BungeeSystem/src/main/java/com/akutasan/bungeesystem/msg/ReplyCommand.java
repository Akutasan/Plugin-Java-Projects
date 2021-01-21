package com.akutasan.bungeesystem.msg;

import com.akutasan.bungeesystem.Main;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class ReplyCommand extends Command {
    public static HashMap<ProxiedPlayer, ProxiedPlayer> replyhash = new HashMap();

    public ReplyCommand() {
        super("r", "", "reply");
    }

    public void execute(CommandSender sender, String[] args) {
        String prefix = Main.getInstance().prefix;
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)sender;
                if (replyhash.containsKey(player)) {
                    ProxiedPlayer player2 = replyhash.get(player);
                    if (replyhash.containsValue(player2)) {
                        if (player2 != null) {
                            if (args.length >= 1) {
                                if (ToggleMsg.tmsg.contains(player2)) {
                                    player.sendMessage(new TextComponent(prefix + getRankCol(player2)+player2.getName()+" §chat seine §cNachrichten §cdeaktiviert!"));
                                    replyhash.remove(player);
                                    replyhash.remove(player2);
                                    return;
                                }

                                if (ToggleMsg.tmsg.contains(player)){
                                    player.sendMessage(new TextComponent(prefix + " §cDu kannst keine Nachrichten versenden!"));
                                    replyhash.remove(player);
                                    replyhash.remove(player2);
                                    return;
                                }


                                StringBuilder mes = new StringBuilder();

                                for (String arg : args) {
                                    mes.append(ChatColor.YELLOW).append(arg).append(" ");
                                }

                                String senderFormat = "§8[§d§lMSG§8] §7Du §8➛ "+getRankCol(player2)+player2.getName()+" §8» "+ChatColor.YELLOW+mes.toString();
                                String receiverFormat = "§8[§d§lMSG§8] "+getRankCol(player)+player.getName()+" §8➛ §7Dir §8» "+ChatColor.YELLOW+mes.toString();
                                player.sendMessage(new TextComponent(senderFormat));
                                player2.sendMessage(new TextComponent(receiverFormat));

                                for (ProxiedPlayer staffs : Main.getInstance().getProxy().getPlayers()) {
                                    if (SocialSpy.sp.contains(staffs)) {
                                        if (staffs.equals(player))return;
                                        String format = "§8[§cSOCIAL SPY§8] "+getRankCol(player)+player.getName()+" §7zu "+getRankCol(player2)+player2.getName()+" §8» "+ChatColor.YELLOW+mes.toString();
                                        staffs.sendMessage(new TextComponent(format));
                                    }
                                }

                                if (replyhash.containsKey(player) || replyhash.containsKey(player2)) {
                                    replyhash.remove(player);
                                    replyhash.remove(player2);
                                    replyhash.put(player, player2);
                                    replyhash.put(player2, player);
                                }

                                replyhash.put(player, player2);
                                replyhash.put(player2, player);
                            } else {
                                player.sendMessage(new TextComponent(prefix + "§e/msg <Spieler> <Nachricht>"));
                                player.sendMessage(new TextComponent(prefix + "§e/r <Nachricht>"));
                                player.sendMessage(new TextComponent(prefix + "§e/msgtoggle"));
                                if (player.hasPermission("synodix.admin")) {
                                    player.sendMessage(new TextComponent(prefix + "§e/spy"));
                                }
                            }
                        } else {
                            player.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist §cnicht §cmehr §cOnline!"));
                            replyhash.remove(player);
                            replyhash.remove(player2);
                        }
                    } else {
                        player.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist §cnicht §cOnline!"));
                    }
                } else {
                    player.sendMessage(new TextComponent(prefix + "§cEs gibt nichts §czum §cbeantworten!"));
                }
        }

    }

    public static String getRankName(ProxiedPlayer player){
        IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        assert user != null;
        return CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getName();
    }

    private static String getRankCol(ProxiedPlayer player) {
        switch (getRankName(player)) {
            case "Owner":
                return "§4";
            case "Admin":
                return "§c";
            case "SrDeveloper":
                return "§b";
            case "SrModerator":
                return "§9";
            case "SrBuilder":
                return "§e";
            case "Developer":
                return "§b";
            case "Moderator":
                return "§9";
            case "Supporter":
                return "§3";
            case "Builder":
                return "§e";
            case "Social":
                return "§5";
            case "MVP":
                return "§d";
            case "VIP":
                return "§a";
            case "Premium":
                return "§6";
            default:
                return "§7";
        }
    }
}