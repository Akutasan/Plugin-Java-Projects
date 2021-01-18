package com.akutasan.partyplugin.commands;


import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

public class PartyInvite extends SubCommand {
    public PartyInvite()
    {
        super("", "<Spieler>", "invite");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cBitte gebe einen §cNamen §can."));
            return;
        }
        ProxiedPlayer pl = BungeeCord.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§e" + args[0] + " §cist nicht §cOnline."));
            return;
        }
        if (pl.getName().equals(p.getName()))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu kannst dich nicht §cselber §ceinladen."));
            return;
        }
        PlayerParty party;
        if ((PartyManager.getParty(p) != null) && (!Objects.requireNonNull(PartyManager.getParty(p)).isLeader(p))) {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist nicht der Party §cBesitzer."));
        }
        if ((PartyManager.getParty(p) != null) && (Objects.requireNonNull(PartyManager.getParty(p)).hasRequest(pl)))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDer §cSpieler §chat bereits eine §cEinladung §czu §cdeiner §cParty."));
            return;
        }
        if (PartyManager.getParty(pl) != null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler ist bereits in §ceiner §canderen §cParty."));
            return;
        }
        if (PartyManager.getParty(p) != null)
        {
            party = PartyManager.getParty(p);
            assert party != null;
            if (party.isInParty(pl)) {
                p.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler ist bereits §cin §cdeiner §cParty."));
                return;
            }
            p.sendMessage(new TextComponent(Main.partyprefix + "§aDu hast §6" + PlayerParty.getRankCol(pl) + pl + " §ain deine §aParty §aeingeladen."));
            party.invite(pl);
        }
        else
        {
            PartyManager.createParty(p);
            p.sendMessage(new TextComponent(Main.partyprefix + "§aDu hast §6" + PlayerParty.getRankCol(pl) + pl + " §ain deine Party §aeingeladen."));
            party = PartyManager.getParty(p);
            assert party != null;
            party.invite(pl);
        }
    }
}