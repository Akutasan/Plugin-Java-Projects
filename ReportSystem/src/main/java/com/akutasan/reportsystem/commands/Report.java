package com.akutasan.reportsystem.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.akutasan.reportsystem.Main;
import com.akutasan.reportsystem.util.Data;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Report extends Command {
    public Report() {
        super("report");
    }
    public static String prefix = Main.getInstance().prefix;

    public static void sendHelp(ProxiedPlayer p) {
        p.sendMessage(new TextComponent(prefix + "§e/report <Name> <Grund>"));
        if (p.hasPermission("reqiuem.team")){
            p.sendMessage(new TextComponent(prefix + "§e/report login"));
            p.sendMessage(new TextComponent(prefix + "§e/report logout"));
            p.sendMessage(new TextComponent(prefix + "§e/reports"));
            p.sendMessage(new TextComponent(prefix + "§e/report autologin"));
            p.sendMessage(new TextComponent(prefix + "§e/report remove <Name>"));
            if (p.hasPermission("reqiuem.admin")){
                p.sendMessage(new TextComponent(prefix + "§e/report clear"));
            }
        }
    }

    public static HashMap<String, Long> cooldowns = new HashMap<>();
    public static String[] gruende = new String[10];

    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if (args.length == 0) {
                sendHelp(p);
            } else if (args.length == 1) {
                 if (!p.hasPermission("Sys.Report")) {
                    sendHelp(p);
                } else if (args[0].equalsIgnoreCase("login")) {
                    if (!Data.login.contains(p)) {
                        Data.login.add(p);
                    } else {
                        p.sendMessage(new TextComponent(prefix + " §cDu bist bereits eingeloggt!"));
                        return;
                    }
                    p.sendMessage(new TextComponent(prefix + "§aDu hast dich eingeloggt!"));
                    for (ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
                        if (team.hasPermission("Sys.Report") && !team.getName().equals(p.getName()))
                                team.sendMessage(new TextComponent(prefix+"§e"+p.getName()+" §ahat sich eingeloggt"));
                    }
                } else if (args[0].equalsIgnoreCase("logout")) {
                    if (Data.login.contains(p)) {
                        Data.login.remove(p);
                    } else {
                        p.sendMessage(new TextComponent(prefix + " §cDu bist bereits ausgeloggt!"));
                        return;
                    }
                     if (Main.doesUserExist(p.getUniqueId().toString())) {
                         Main.deactivateAutoLogin(p.getUniqueId().toString());
                     }
                     p.sendMessage(new TextComponent(prefix + "§cDu hast dich ausgeloggt!"));
                    for (ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
                        if (team.hasPermission("Sys.Report") &&
                                !team.getName().equals(p.getName()))
                            team.sendMessage(new TextComponent(prefix+"§e"+p.getName()+" §chat sich ausgeloggt"));
                    }
                } else if(args[0].equalsIgnoreCase("autologin")) {
                    if (Main.doesUserExist(p.getUniqueId().toString())) {
                        if (Main.isActivated(p.getUniqueId().toString())) {
                            Main.deactivateAutoLogin(p.getUniqueId().toString());
                            Data.login.remove(p);
                            p.sendMessage(new TextComponent(prefix+"§cDein Autologin wurde deaktiviert")) ;
                        } else {
                            Main.activateAutoLogin(p.getUniqueId().toString());
                            Data.login.add(p);
                            p.sendMessage(new TextComponent(prefix+"§aDein Autologin wurde aktiviert"));
                        }
                    } else {
                        Main.activateAutoLogin(p.getUniqueId().toString());
                        p.sendMessage(new TextComponent(prefix+"§aDein Autologin wurde aktiviert"));
                    }
                } else if (args[0].equalsIgnoreCase("clear") && p.hasPermission("reqiuem.admin")) {
                     Data.reports.clear();
                     Data.offlinereports.clear();
                     p.sendMessage(new TextComponent(prefix+"§aDu hast erfolgreich alle Reports gelöscht!"));
            } else {
                    p.sendMessage(new TextComponent(prefix + "§e/report <Name> <Grund>"));
                    if (p.hasPermission("reqiuem.team")){
                        p.sendMessage(new TextComponent(prefix + "§e/report login"));
                        p.sendMessage(new TextComponent(prefix + "§e/report logout"));
                        p.sendMessage(new TextComponent(prefix + "§e/reports"));
                        p.sendMessage(new TextComponent(prefix + "§e/report autologin"));
                        p.sendMessage(new TextComponent(prefix + "§e/report remove <Name>"));
                        if (p.hasPermission("reqiuem.admin")){
                            p.sendMessage(new TextComponent(prefix + "§e/report clear"));
                        }
                    }
                }
            } else if (args.length == 2) {
                //Report remove
                if (args[0].equalsIgnoreCase("remove") && p.hasPermission("Sys.Report")) {
                        ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if (Data.reports.containsKey(target)) {
                                Data.reports.remove(target);
                                p.sendMessage(new TextComponent(prefix + "§aDu hast erfolgreich §e"+target.getName() +" §aaus §ader §aReportliste §aentfernt!"));
                            } else {
                                p.sendMessage((BaseComponent) new TextComponent(prefix + "§cEs gibt §csind keine §cReports für §cden §cSpieler §cvorhanden!"));
                            }
                        } else if (target == null) {
                            if (Data.offlinereports.containsKey(args[1])) {
                                Data.offlinereports.remove(args[1]);

                                p.sendMessage(new TextComponent(prefix + "§aDu hast erfolgreich §e" + args[1] + " §aaus §ader §aReportliste §aentfernt!"));
                            } else {

                                p.sendMessage(new TextComponent(prefix + "§cEs gibt sind keine Reports für den Spieler vorhanden!"));
//                                        p.sendMessage((BaseComponent) new TextComponent(prefix + "§cBitte achte auf Ground Kleinschreibung!"));
                            }
                        }
                        return;
                    }
                    ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                    if (target != null) {
                        String reason = "";
                        reason = args[1];

                        try {
                            File file = new File("plugins//Report", "config.yml");
                            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
                            StringBuilder grund = new StringBuilder();
                            for (String g : config.getStringList("reason.reasons"))
                                grund.append("§e").append(g).append("§7, ");
                            if (!config.getStringList("reason.reasons").contains(reason)) {
                                p.sendMessage(new TextComponent(prefix + "§7Bitte gib einen der folgenden Gründe an: " + grund));
                                return;
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        if (!target.getName().equals(p.getName())) {
                            if (!Data.reports.containsKey(target) || !Data.offlinereports.containsKey(target.getName())) {

                                int cooldownTime = 60;
                                if (cooldowns.containsKey(sender.getName())) {
                                    long secondsLeft = cooldowns.get(sender.getName()) / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                                    if (secondsLeft > 0L) {
                                        p.sendMessage(new TextComponent(prefix + "§cDu musst noch §e" + secondsLeft + " §cSekunden warten!"));
                                        return;
                                    }
                                }
                                cooldowns.put(sender.getName(), System.currentTimeMillis());

                                p.sendMessage(new TextComponent(prefix + "§aDu hast den Spieler §e" + target.getName() + " §aerfolgreich reportet!"));
                                for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                                    if (all.hasPermission("Sys.Report"))
                                        if (Data.login.contains(all)) {
                                            TextComponent tc = new TextComponent();
                                            tc.setText(prefix);
                                            TextComponent tc1 = new TextComponent();
                                            tc1.setText(target.getName());
                                            tc1.setColor(ChatColor.RED);
                                            tc.addExtra(tc1);
                                            TextComponent tc2 = new TextComponent();
                                            tc2.setText(" ➛");
                                            tc2.setColor(ChatColor.YELLOW);
                                            tc.addExtra(tc2);
                                            Data.reports.put(target, reason);
                                            TextComponent tc3 = new TextComponent();
                                            tc3.setText(" " + reason);
                                            tc3.setColor(ChatColor.DARK_PURPLE);
                                            tc.addExtra(tc3);
                                            TextComponent tc5 = new TextComponent();
                                            tc5.setText(" §7(von §a" + p.getName() + "§7)");
                                            tc.addExtra(tc5);
                                            TextComponent tc4 = new TextComponent();
                                            tc4.setText(" [Ansehen]");
                                            tc4.setColor(ChatColor.GREEN);
                                            tc4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reporttp " + target.getName()));
                                            tc4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§aZum Spieler teleportieren")).create()));
                                            tc.addExtra(tc4);
                                            all.sendMessage(tc);
                                        }
                                }
                            } else {
                                p.sendMessage(new TextComponent(prefix + "§cDieser Spieler wurde bereits reportet!"));
                            }
                        } else {
                            p.sendMessage(new TextComponent(prefix + "§cDu kannst dich nicht selbst reporten"));
                        }
                    } else {
                        p.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist nicht online!"));
                    }
//                } else {
//                        p.sendMessage(new TextComponent(prefix + "§cDu hast keine Rechte dazu!"));
//                }
            } else if (args.length > 2){
                sendHelp(p);
            }
        } else {
            sender.sendMessage(new TextComponent("Du musst ein Spieler sein!"));
        }
    }
}
