package com.akutasan.bungeesystem.ping;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class PingComm extends Command {

    public PingComm(){
        super("ping");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            int ping = p.getPing();

            p.sendMessage(new TextComponent("§9§lSynodix §8§l× §7Ping: §a" + ping));
        }
    }
}
