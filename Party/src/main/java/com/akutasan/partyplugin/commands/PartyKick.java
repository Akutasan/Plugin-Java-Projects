package com.akutasan.partyplugin.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyKick extends SubCommand
{
    public PartyKick()
    {
        super("", "<Spieler>", "kick");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cBitte gebe einen §cNamen an."));
            return;
        }
        if (PartyManager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist in §ckeiner §cParty."));
            return;
        }
        PlayerParty party = PartyManager.getParty(p);
        assert party != null;
        if (!party.isLeader(p))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist nicht der §cParty §cBesitzer."));
            return;
        }
        ProxiedPlayer pl = BungeeCord.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§c" + args[0] + " §cist nicht §cOnline."));
            return;
        }
        if (!party.isInParty(pl))
        {
            p.sendMessage(new TextComponent(Main.partyprefix + "§c" + args[0] + " §cist nicht §cin §cdeiner §cParty."));
            return;
        }
        if (party.removePlayer(pl)) {
            pl.sendMessage(new TextComponent(Main.partyprefix + "§cDu wurdest §cvon der §cParty §cgekickt!"));
            p.sendMessage(new TextComponent(Main.partyprefix + "§6" + PlayerParty.getRankCol(p) + p.getName() + " §chat die Party verlassen."));
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(Main.partyprefix + "§6" + PlayerParty.getRankCol(pl) + pl.getName() + " §cwurde aus der Party gekickt!"));
            }
            start(p);
        }
    }

    private void start(final ProxiedPlayer p)
    {
        BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
            PlayerParty party = PartyManager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0))
            {
                PartyManager.deleteParty(p);
                p.sendMessage(new TextComponent(Main.partyprefix + "§cDie Party wird wegen zu wenig §cMitgliedern §caufgelöst."));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}