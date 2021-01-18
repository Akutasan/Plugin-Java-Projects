package com.akutasan.reportsystem.commands;

import com.akutasan.reportsystem.Main;
import com.akutasan.reportsystem.util.Data;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Map;

public class Reports extends Command {
    public Reports() {
        super("reports");
    }

    public static String prefix = Main.getInstance().prefix;


    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (p.hasPermission("Sys.Report")) {
                if (Data.login.contains(p)) {
                        if (Data.reports.size() > 0 || Data.offlinereports.size() > 0) {
                            p.sendMessage(new TextComponent(prefix + "§7Alle offenen Reports:"));
                            p.sendMessage(new TextComponent(prefix));
                            for (Map.Entry<ProxiedPlayer, String> entry : Data.reports.entrySet()) {
                                if (entry.getKey() != null) {
                                    TextComponent tc = new TextComponent();
                                    tc.setText(prefix + "§c"+ entry.getKey() +"§e➛ §5" + entry.getValue());
                                    TextComponent tc2 = new TextComponent();
                                    tc2.setText(" [Ansehen]");
                                    tc2.setColor(ChatColor.GREEN);
                                    tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reporttp " + entry.getKey()));
                                    tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§aZum Spieler teleportieren")).create()));
                                    tc.addExtra(tc2);
                                    p.sendMessage(tc);
                                }
                            }
                            for (Map.Entry<String, String> entry : Data.offlinereports.entrySet())
                                p.sendMessage(new TextComponent(prefix + "§c" + entry.getKey() + "§e» §5" + entry.getValue() + "§c[OFFLINE]"));
                            return;
                        }
                        p.sendMessage(new TextComponent(prefix + "§cEs gibt momentan keine offenen Reports!"));
                    } else {
                        p.sendMessage(new TextComponent(prefix + "§cDu bist nicht eingeloggt!"));
                    }
                } else {
                    p.sendMessage(new TextComponent(prefix + "§cDu hast keine Rechte dazu!"));
                }
            } else {
                sender.sendMessage(new TextComponent("Du musst ein Spieler sein"));
            }
        }
    }
