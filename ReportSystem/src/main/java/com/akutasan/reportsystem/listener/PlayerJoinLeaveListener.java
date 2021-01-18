package com.akutasan.reportsystem.listener;

import com.akutasan.reportsystem.Main;
import com.akutasan.reportsystem.util.Data;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinLeaveListener implements Listener {
    public static String prefix = Main.getInstance().prefix;

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (Data.reports.containsKey(p)) {
            Data.offlinereports.put(p.getName(), (String)Data.reports.get(p));
            Data.reports.remove(p);
        }
    }

    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (Data.offlinereports.containsKey(p.getName())) {
            Data.reports.put(p, Data.offlinereports.get(p.getName()));
            Data.offlinereports.remove(p.getName());
        }
        if (p.hasPermission("report.autologin") && Main.isActivated(p.getUniqueId().toString()) && !Data.login.contains(p)) {
            Data.login.add(p);
            p.sendMessage(new TextComponent(prefix + "§aDu hast dich eingeloggt!"));
            for (ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
                if (team.hasPermission("report.see") &&
                        !team.getName().equals(p.getName()))
                        team.sendMessage(new TextComponent(prefix + "§e"+p.getName()+" §ahat sich eingeloggt"));
            }
        }
    }
}
