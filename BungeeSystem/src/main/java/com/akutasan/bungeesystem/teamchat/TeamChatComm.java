package com.akutasan.bungeesystem.teamchat;

import com.akutasan.bungeesystem.Main;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

//import org.bukkit.entity.Player;


public class TeamChatComm extends Command {

    private Main plugin;

    public TeamChatComm(Main plugin){
        super("tc","synodix.team", "teamchat");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(ChatColor.GRAY).append(arg).append(" ");
        }
        if (sb.toString().equalsIgnoreCase("")||sb.toString().equalsIgnoreCase(" ")){
            return;
        }
        this.plugin.sendTeam(getRank(p),p.getName()+" §8» "+ChatColor.GRAY+sb.toString());
    }


    public static String getRankName(ProxiedPlayer player){
        IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        assert user != null;
        return CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getName();
    }

    private static String getRank(ProxiedPlayer player) {
        switch (getRankName(player)) {
            case "Owner":
                return "§4Owner §r§7• §4";
            case "Admin":
                return "§cAdmin §r§7• §c";
            case "SrDeveloper":
                return "§cSrDev §r§7• §c";
            case "Developer":
                return "§bDeveloper §r§7• §b";
            case "SrModerator":
                return "§cSrMod §r§7• §c";
            case "SrBuilder":
                return "§cSrBuilder §r§7• §c";
            case "Moderator":
                return "§9Moderator §r§7• §9";
            case "Supporter":
                return "§3Supporter §r§7• §3";
            case "Builder":
                return "§eBuilder §r§7• §e";
            default:
                return "§7Tester §r§7• §7";
        }
    }


//    private static String getRank(ProxiedPlayer player) {
//        if (player.hasPermission("Score.Owner")) {
//            return "§4Owner §r§7• §4";
//        } else if (player.hasPermission("Score.Admin")) {
//            return "§cAdmin §r§7• §c";
//        } else if (player.hasPermission("Score.SrDeveloper")) {
//            return "§cSrDev §r§7• §c";
//        } else if (player.hasPermission("Score.Developer")) {
//            return "§bDeveloper §r§7• §b";
//        } else if (player.hasPermission("Score.SrModerator")) {
//            return "§cSrMod §r§7• §c";
//        } else if (player.hasPermission("Score.SrBuilder")) {
//            return "§cSrBuilder §r§7• §c";
//        } else if (player.hasPermission("Score.Moderator")) {
//            return "§9Moderator §r§7• §9";
//        } else if (player.hasPermission("Score.Supporter")) {
//            return "§3Supporter §r§7• §3";
//        } else if (player.hasPermission("Score.Builder")) {
//            return "§eBuilder §r§7• §e";
//        } else {
//            return "§7Tester §r§7• §7";
//        }
//    }
}
