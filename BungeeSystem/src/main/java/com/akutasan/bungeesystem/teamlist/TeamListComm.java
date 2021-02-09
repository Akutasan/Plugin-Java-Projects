package com.akutasan.bungeesystem.teamlist;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamListComm extends Command{

    public TeamListComm() {
        super("teamon","synodix.team","teamlist");
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer p = (ProxiedPlayer) commandSender;
        ArrayList<Data> teamlist = new ArrayList<>();

        p.sendMessage(new TextComponent("§7§m--------§bTeamliste§7§m--------"));
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission("synodix.team")) {
                getRankName(player, teamlist);
            }
        }

        Collections.sort(teamlist);

        for (Data pl : teamlist) {
                String playerServer = pl.play.getServer().getInfo().getName();
                p.sendMessage(new TextComponent(getRank(pl.play) + pl.play.getName() + " §7auf §6" + playerServer));
        }
        p.sendMessage(new TextComponent("§7§m--------§bTeamliste§7§m--------"));
    }

    static class Data implements Comparable<Data> {

        private int id;
        private String ud;
        public ProxiedPlayer play;

        public Data(int i, String u, ProxiedPlayer play) {
            this.id = i;
            this.ud = u;
            this.play = play;
        }

        @Override
        public int compareTo(Data d) {
            return this.id - d.getId();
        }

        public int getId() {
            return id;
        }

        public String getUd(){
            return ud;
        }
    }



    public static void getRankName(ProxiedPlayer player, ArrayList<Data> data){
        IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        assert user != null;
        String group = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getName();
        int groupId = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getSortId();

        data.add(new Data(groupId, group, player));

    }

    private static String getRank(ProxiedPlayer player) {
        IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        assert user != null;
        String group = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getName();
        switch (group) {
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
}
