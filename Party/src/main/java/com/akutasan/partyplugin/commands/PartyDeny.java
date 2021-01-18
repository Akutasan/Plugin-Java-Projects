package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyDeny extends SubCommand
{
    public PartyDeny()
    {
        super("", "<Spieler>", "deny");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cBitte gebe §ceinen §cNamen §can."));
            return;
        }
        if (PartyManager.getParty(p) != null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist bereits §cin §ceiner §cParty."));
            return;
        }
        ProxiedPlayer pl = BungeeCord.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler §cist §cnicht §cOnline."));
            return;
        }
        if (PartyManager.getParty(pl) == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDer Spieler §e" + args[0] + " §chat §ckeine §cParty."));
            return;
        }
        PlayerParty party = PartyManager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist nicht §czu §cdieser §cParty §ceingeladen."));
            return;
        }
        party.removeInvite(p);
        p.sendMessage(new TextComponent(Main.partyprefix + "§cDu hast die §cAnfrage §cvon §e" + PlayerParty.getRankCol(party.getLeader()) + party.getLeader() + " §cabgelehnt."));
        pl.sendMessage(new TextComponent(Main.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §chat §cdeine §cAnfrage §cabgelehnt."));
        if (party.getPlayers().size() == 1) {
            party.removePlayer(pl);
        }
    }
}
