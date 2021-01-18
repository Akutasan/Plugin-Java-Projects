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


public class Message extends Command {
    public static HashMap<String, String> reply = new HashMap();


    public Message() {
        super("msg","","message","whisper","tell","w");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String prefix = Main.getInstance().prefix;
        if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                if (args.length >= 2) {
                    ProxiedPlayer player2 = Main.getInstance().getProxy().getPlayer(args[0]);
                    if (player2 == null) {
                        player.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist nicht §cOnline!"));
                        return;
                    }

                    if (player.getName().equals(player2.getName())) {
                        player.sendMessage(new TextComponent(prefix + "§cDu kannst dir nicht §cselbst §cNachrichten §cschreiben!"));
                        return;
                    }

                    if (ToggleMsg.tmsg.contains(player2)) {
                        player.sendMessage(new TextComponent(prefix + getRankCol(player2)+player2.getName()+" §chat seine Nachrichten §cdeaktiviert!"));
                        return;
                    }

                    if (ToggleMsg.tmsg.contains(player)){
                        player.sendMessage(new TextComponent(prefix + " §cDu kannst keine Nachrichten versenden!"));
                        return;
                    }

                    StringBuilder mes = new StringBuilder();

                    for (int i = 1; i < args.length; ++i) {
                        mes.append(ChatColor.YELLOW).append(args[i]).append(" ");
                    }

                    String senderFormat = "§8[§d§lMSG§8] §7Du §8➛ "+getRankCol(player2)+player2.getName()+" §8» "+ ChatColor.YELLOW+mes.toString();
                    String receiverFormat = "§8[§d§lMSG§8] "+getRankCol(player)+player.getName()+" §8➛ §7Dir §8» "+ChatColor.YELLOW+mes.toString();
                    player.sendMessage(new TextComponent(senderFormat));
                    player2.sendMessage(new TextComponent(receiverFormat));

                    for (ProxiedPlayer staffs : Main.getInstance().getProxy().getPlayers()) {
                        if (SocialSpy.sp.contains(staffs)) {
                            if (staffs.equals(player))return;
                            String format = "§8[§5MSG Spy§8] "+getRankCol(player)+player.getName()+" §7zu "+getRankCol(player2)+player2.getName()+" §8» "+ChatColor.YELLOW+mes.toString();
                            staffs.sendMessage(new TextComponent(format));
                        }
                    }

                    if (ReplyCommand.replyhash.containsKey(player) || ReplyCommand.replyhash.containsKey(player2)) {
                        ReplyCommand.replyhash.remove(player);
                        ReplyCommand.replyhash.remove(player2);
                        ReplyCommand.replyhash.put(player, player2);
                        ReplyCommand.replyhash.put(player2, player);
                    }

                    ReplyCommand.replyhash.put(player, player2);
                    ReplyCommand.replyhash.put(player2, player);
                } else {
                    player.sendMessage(new TextComponent(prefix + "§e/msg <Spieler> <Nachricht>"));
                    player.sendMessage(new TextComponent(prefix + "§e/r <Nachricht>"));
                    player.sendMessage(new TextComponent(prefix + "§e/msgtoggle"));
                    if (player.hasPermission("synodix.admin")) {
                        player.sendMessage(new TextComponent(prefix + "§e/spy"));
                    }
                }
        } else {
            sender.sendMessage(new TextComponent(prefix + "§cDu musst ein Spieler §csein §cum §cdies §czu tun!"));
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
            case "SrDeveloper":
            case "SrModerator":
            case "SrBuilder":
                return "§c";
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
