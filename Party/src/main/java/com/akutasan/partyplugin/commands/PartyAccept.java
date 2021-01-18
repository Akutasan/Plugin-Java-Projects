package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyAccept extends SubCommand
{
    public PartyAccept()
    {
        super("", "<Spieler>", "accept");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cBitte gebe einen §cNamen §can."));
            return;
        }
        if (PartyManager.getParty(p) != null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist bereits in §ceiner §cParty."));
            return;
        }
        ProxiedPlayer pl = BungeeCord.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§e" + args[0] + " §cist nicht §conline."));
            return;
        }
        if (PartyManager.getParty(pl) == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler " + args[0] + " §chat keine §cParty."));
            return;
        }
        PlayerParty party = PartyManager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p)) {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu hast keine Einladung zur Party §cvon §6" + args[0]));
        }
        if (party.isInParty(p))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist bereits in der §cParty."));
            return;
        }
        if (party.addPlayer(p))
        {
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(Main.partyprefix + "§6" + PlayerParty.getRankCol(p) + p + " §eist der §eParty §ebeigetreten."));
            }
            party.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§6" + PlayerParty.getRankCol(p) + p + " §eist der §eParty §ebeigetreten."));
            party.removeInvite(p);
        }
    }
}

