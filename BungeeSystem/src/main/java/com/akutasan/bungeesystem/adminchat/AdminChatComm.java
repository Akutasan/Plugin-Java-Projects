package com.akutasan.bungeesystem.adminchat;

import com.akutasan.bungeesystem.Main;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminChatComm extends Command {
    private Main plugin;

    public AdminChatComm(Main plugin){
        super("ac","synodix.admin", "adminchat");
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
        this.plugin.sendAdmin(getRank(p),p.getName()+" §8» §7"+sb.toString());
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
                return "§bSrDev §r§7• §b";
            case "Developer":
                return "§bDeveloper §r§7• §b";
            case "SrModerator":
                return "§9SrMod §r§7• §9";
            case "SrBuilder":
                return "§eSrBuilder §r§7• §e";
            default:
                return "§7Tester §r§7• §7";
        }
    }
}
