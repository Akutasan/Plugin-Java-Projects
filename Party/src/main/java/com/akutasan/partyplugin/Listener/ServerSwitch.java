package com.akutasan.partyplugin.Listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitch
        implements Listener
{
    @EventHandler
    public void onSwitch(ServerSwitchEvent e)
    {
        ProxiedPlayer p = e.getPlayer();
        final PlayerParty party;
        if ((PartyManager.getParty(p) != null) && ((party = PartyManager.getParty(p)).isLeader(p)))
        {
            assert party != null;
            if ((party.getLeader().getServer().getInfo().getName().contains("Lobby")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby01")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby-01"))) {
                return;
            }
            party.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§eDie Party betritt den Server §6" + party.getLeader().getServer().getInfo().getName()));
            for (final ProxiedPlayer pp : party.getPlayers())
            {
                pp.sendMessage(new TextComponent(Main.partyprefix + "§eDie Party betritt den Server §6" + party.getLeader().getServer().getInfo().getName()));
                BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable()
                {
                    public void run()
                    {
                        pp.connect(party.getServer());
                    }
                }, 2L, TimeUnit.SECONDS);
            }
        }
    }


    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equalsIgnoreCase("BungeeCord")) {

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String channel = in.readUTF();
                if (channel.equalsIgnoreCase("party")) {
                    String input = in.readUTF();
                    String target = in.readUTF();
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
                    if (input.equalsIgnoreCase("invite")){
                        ProxiedPlayer pl = BungeeCord.getInstance().getPlayer(target);
                        PlayerParty party;
                        if ((PartyManager.getParty(player) != null) && (!PartyManager.getParty(player).isLeader(player))) {
                            player.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist nicht der Party §cBesitzer."));
                        }
                        if ((PartyManager.getParty(player) != null) && ((party = PartyManager.getParty(player)).hasRequest(pl)))
                        {
                            player.sendMessage(new TextComponent(Main.partyprefix + "§cDer §cSpieler §chat bereits eine §cEinladung §czu §cdeiner §cParty."));
                            return;
                        }
                        if (PartyManager.getParty(pl) != null)
                        {
                            player.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler ist bereits in §ceiner §canderen §cParty."));
                            return;
                        }
                        if (PartyManager.getParty(player) != null)
                        {
                            party = PartyManager.getParty(player);
                            if (party.isInParty(pl)) {
                                player.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler ist bereits §cin §cdeiner §cParty."));
                                return;
                            }
                            player.sendMessage(new TextComponent(Main.partyprefix + "§aDu hast §6" + target + " §ain deine §aParty §aeingeladen."));
                            party.invite(pl);
                        }
                        else
                        {
                            PartyManager.createParty(player);
                            player.sendMessage(new TextComponent(Main.partyprefix + "§aDu hast §6" + target + " §ain deine Party §aeingeladen."));
                            party = PartyManager.getParty(player);
                            assert party != null;
                            party.invite(pl);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}