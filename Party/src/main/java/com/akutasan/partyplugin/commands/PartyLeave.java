package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Main;
import com.akutasan.partyplugin.manager.PlayerParty;
import com.akutasan.partyplugin.manager.PartyManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyLeave extends SubCommand {
    public PartyLeave()
    {
        super("", "", "leave");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        PlayerParty party = PartyManager.getParty(p);
        if (PartyManager.getParty(p) == null) {
            p.sendMessage(new TextComponent(Main.partyprefix + "§cDu bist in keiner Party."));
        } else {
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
                p.sendMessage(new TextComponent(Main.partyprefix + "§cDu hast die Party §cverlassen §cund §cdie §cParty §cwurde §caufgelöst."));
            } else {
                party.removePlayer(p);
                p.sendMessage(new TextComponent(Main.partyprefix + "§cDu hast die Party verlassen!"));
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(Main.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §chat §cdie §cParty §cverlassen."));
                }
                party.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §chat §cdie §cParty §cverlassen."));
            }
        }
    }
}
