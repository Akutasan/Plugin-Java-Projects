package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyChat extends Command {
    public PartyChat()
    {
        super("pc");
    }

    public void execute(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(new TextComponent(Party.partyprefix + "§c/p <Message>"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (PartyManager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou aren't in any §cParty."));
            return;
        }
        PlayerParty party = PartyManager.getParty(p);
        StringBuilder msg = new StringBuilder();
        int j = args.length;
        int i = 0;
        while (i < j) {
            String s = args[i];
            msg.append("§7 ").append(s);
            i++;
        }
        assert party != null;
        for (ProxiedPlayer members : party.getPlayers()) {
            members.sendMessage(new TextComponent("§d§lParty §8• §6" + PlayerParty.getRankCol(p) + p + " §8» §7" + msg));
        }
        party.getLeader().sendMessage(new TextComponent("§d§lParty §8• §6" + PlayerParty.getRankCol(p) + p + " §8» §7" + msg));
    }
}