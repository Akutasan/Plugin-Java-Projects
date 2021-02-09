package com.akutasan.bungeesystem;

import com.akutasan.bungeesystem.adminchat.AdminChatComm;
import com.akutasan.bungeesystem.joinme.JoinmeComm;
import com.akutasan.bungeesystem.modchat.ModChatComm;
import com.akutasan.bungeesystem.msg.Message;
import com.akutasan.bungeesystem.msg.ReplyCommand;
import com.akutasan.bungeesystem.msg.SocialSpy;
import com.akutasan.bungeesystem.msg.ToggleMsg;
import com.akutasan.bungeesystem.ping.PingComm;
import com.akutasan.bungeesystem.teamchat.TeamChatComm;
import com.akutasan.bungeesystem.teamlist.TeamListComm;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    private static Main instance;
    public String prefix = "§9§lSynodix §8§l× ";

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info(ChatColor.GREEN +"ist erfolgreich geladen!");
        getProxy().getPluginManager().registerCommand(this, new PingComm());
        getProxy().getPluginManager().registerCommand(this, new TeamChatComm(this));
        getProxy().getPluginManager().registerCommand(this, new TeamListComm());
        getProxy().getPluginManager().registerCommand(this, new Message());
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
        getProxy().getPluginManager().registerCommand(this, new ToggleMsg());
        getProxy().getPluginManager().registerCommand(this, new SocialSpy());
        getProxy().getPluginManager().registerCommand(this, new AdminChatComm(this));
        getProxy().getPluginManager().registerCommand(this, new ModChatComm(this));
//        getProxy().getPluginManager().registerCommand(this, new JoinmeComm());
    }

    public void sendTeam(String prefix, String message){
        for (ProxiedPlayer online : getProxy().getPlayers()){
            if (online.hasPermission("synodix.team")){
                online.sendMessage(new TextComponent("§c§lTeamChat §r§7| "+prefix+ message));
            }
        }
    }

    public void sendMod(String prefix, String message){
        for (ProxiedPlayer online : getProxy().getPlayers()){
            if (online.hasPermission("synodix.mod")){
                online.sendMessage(new TextComponent("§9§lModChat §r§7| "+prefix+ message));
            }
        }
    }

    public void sendAdmin(String prefix, String message){
        for (ProxiedPlayer online : getProxy().getPlayers()){
            if (online.hasPermission("synodix.admin")){
                online.sendMessage(new TextComponent("§4§lAdminChat §r§7| "+prefix+ message));
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"ist erfolgreich heruntergefahren!");
    }

    public static Main getInstance() {
        return instance;
    }

}
