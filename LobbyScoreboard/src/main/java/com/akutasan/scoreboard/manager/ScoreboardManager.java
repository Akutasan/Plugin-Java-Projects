package com.akutasan.scoreboard.manager;

import com.akutasan.scoreboard.Main;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.PermissionUserGroupInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;

public class ScoreboardManager implements Listener {

    private static HashMap<Scoreboard, Player> boards = new HashMap<>();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void setScoreboard(Player player) {
        Scoreboard sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective obj = sb.getObjective("aaa");
        if (obj == null){
            obj = sb.registerNewObjective("aaa","bbb");
        }



        obj.setDisplayName("§8➥ §b" + player.getName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("§6 ").setScore(13);
        obj.getScore(Objects.requireNonNull(Main.getInstance().getFileManager().getScoreboardConfig().getString("RangLine")).replace("&", "§")).setScore(12);
        Team rank = sb.registerNewTeam("rank");
        rank.setPrefix(" §8» §7");
        rank.setSuffix(getRank(player));
        rank.addEntry(ChatColor.BOLD.toString());
        obj.getScore(ChatColor.BOLD.toString()).setScore(11);
        boards.put(sb, player);


        obj.getScore("§4 ").setScore(10);
        obj.getScore(Objects.requireNonNull(Main.getInstance().getFileManager().getScoreboardConfig().getString("OnlinetimeLine")).replace("&", "§")).setScore(9);
        Team time = sb.registerNewTeam("zeit");
        time.setPrefix(" §8» §7");
        time.setSuffix(getTime(player));
        time.addEntry(ChatColor.AQUA.toString());
        obj.getScore(ChatColor.AQUA.toString()).setScore(8);
        boards.put(sb, player);

        obj.getScore( "§5 ").setScore(7);
        obj.getScore(Objects.requireNonNull(Main.getInstance().getFileManager().getScoreboardConfig().getString("Rangzeit")).replace("&", "§")).setScore(6);
        Team rankZ = sb.registerNewTeam("rankZ");
        rankZ.setPrefix(" §8» §7");
        rankZ.setSuffix("§7"+getRankTime(player));
        rankZ.addEntry(ChatColor.DARK_AQUA.toString());
        obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(5);
        boards.put(sb, player);

        obj.getScore("§4").setScore(4);
        obj.getScore(Objects.requireNonNull(Main.getInstance().getFileManager().getScoreboardConfig().getString("Geldline")).replace("&", "§")).setScore(3);
        Team coins = sb.registerNewTeam("geld");
        coins.setPrefix(" §8» §7");
        coins.setSuffix("§7"+getCoins(player));
        coins.addEntry(ChatColor.GOLD.toString());
        obj.getScore(ChatColor.GOLD.toString()).setScore(2);
        obj.getScore("§9").setScore(1);
        boards.put(sb, player);



        player.setScoreboard(sb);

    }

    public static void startScheduler(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Scoreboard sb : boards.keySet()){
                    Player player = boards.get(sb);
                    Objects.requireNonNull(sb.getTeam("rank")).setSuffix(getRank(player));
                    Objects.requireNonNull(sb.getTeam("zeit")).setSuffix(getTime(player));
                    Objects.requireNonNull(sb.getTeam("geld")).setSuffix(getCoins(player));
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 600);
    }

    public static String getRankTime(Player player){
        String Ranktime = "Lifetime \n";

        for (PermissionUserGroupInfo groupInfo : Objects.requireNonNull(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId())).getGroups()) {
            Ranktime = groupInfo.getTimeOutMillis() > 0 ? DATE_FORMAT.format(groupInfo.getTimeOutMillis()) : "Lifetime \n";
        }

        return Ranktime;
    }

    public static String getTime(Player player) {
        long minutes = OnlineTime.getOnlineTime(player.getUniqueId());
        int hours = (int)(minutes / 60);
        long minutes1 = minutes % 60;
        if (hours == 0) { //00:var
            if (minutes1 < 10) { //00:0var
                return "§700:0" + minutes1 + "h";
            } else { //00:var
                return "§700:" + minutes1 + "h";
            }
        } else if ((hours < 10) && (minutes1 < 10)) { //0var:0var
            return "§70" + hours + ":0" + minutes1 + "h";
        } else if ((hours < 10) && (minutes1 > 10)) { //0var:var
            return"§70" + hours + ":" + minutes1 + "h";
        } else if ((hours > 10) && (minutes1 < 10)) { //var:0var
            return"§7" + hours + ":0" + minutes1 + "h";
        } else if ((minutes1 == 0)) { //0var:00
            return "§70" + hours + ":00h";
        } else {
            return"§7" + hours + ":" + minutes1 + "h"; //var:var
        }
    }

    public static String getCoins(Player player){
        long con = Coins.getCoins(player.getUniqueId());
        return Long.toString(con);
    }

    public static String getRankName(Player player){
        String Rankname = "Tester \n";
        for (PermissionUserGroupInfo groupInfo : Objects.requireNonNull(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId())).getGroups()) {
            Rankname = groupInfo.getGroup();
        }
        return Rankname;
    }

    private static String getRank(Player player) {
        switch (getRankName(player)) {
            case "Owner":
                return "§4Owner";
            case "Admin":
                return "§cAdmin";
            case "SrDeveloper":
                return "§cSrDev";
            case "SrModerator":
                return "§cSrMod";
            case "SrBuilder":
                return "§cSrBuilder";
            case "Developer":
                return "§bDev";
            case "Moderator":
                return "§9Mod";
            case "Supporter":
                return "§3Sup";
            case "Builder":
                return "§e";
            case "Social":
                return "§5Social";
            case "MVP":
                return "§dMVP";
            case "VIP":
                return "§aVIP";
            case "Premium":
                return "§6Premium";
            default:
                return "§7Spieler";
        }
    }
}
