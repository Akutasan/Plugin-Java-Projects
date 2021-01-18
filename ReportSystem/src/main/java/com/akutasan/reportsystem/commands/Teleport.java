package com.akutasan.reportsystem.commands;

import com.akutasan.reportsystem.Main;
import com.akutasan.reportsystem.util.Data;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Teleport extends Command {
    public static String prefix = Main.getInstance().prefix;
    public Teleport() {
        super("reporttp");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if (p.hasPermission("Sys.Report") && args.length == 1) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (Data.reports.containsKey(target)) {
                        Data.reports.remove(target);
                        p.connect(ProxyServer.getInstance().getServerInfo(target.getServer().getInfo().getName()));
                    } else {
                        p.sendMessage(new TextComponent(prefix + "§cDieser Report wird bereits bearbeitet!"));
                    }
                } else {
                    p.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist nicht mehr online!"));
                }
            }
        } else {
            sender.sendMessage(new TextComponent("Du musst ein Spieler sein!"));
        }
    }
}
