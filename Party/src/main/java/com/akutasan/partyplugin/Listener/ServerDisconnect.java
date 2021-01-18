package com.akutasan.partyplugin.Listener;

import java.util.concurrent.TimeUnit;
import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerDisconnect implements Listener {
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (PartyManager.getParty(p) != null) {
            PlayerParty party = PartyManager.getParty(p);
            assert party != null;
            if (party.isLeader(p)) {
                if (party.getPlayers().size() == 1) {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Main.partyprefix + "§cDa der Partybesitzer §cdie §cParty §cverlassen hat, §cwurde §cdie §cParty §caufgelöst."));
                    }
                    party.removePlayer(p);
                    PartyManager.deleteParty(p);
                } else {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Main.partyprefix + "§cDa der Partybesitzer §cdie §cParty §cverlassen hat, §cwurde §cdie §cParty §caufgelöst."));
                        party.removePlayer(p);
                        party.removePlayer(pp);
                    }
                }
            } else {
                party.removePlayer(p);
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(Main.partyprefix + "§6" + p.getName() + " §chat §cdie §cParty §cverlassen."));
                }
                start(p);
            }
        }
    }

    private void start(final ProxiedPlayer p)
    {
        BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
            PlayerParty party = PartyManager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0)) {
                party.removePlayer(p);
                party.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§cDie Party §cwird §cwegen §czu §cwenig §cMitgliedern §caufgelöst."));
                p.sendMessage(new TextComponent(Main.partyprefix + "§cDie §cParty §cwurde §caufgelöst."));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}
