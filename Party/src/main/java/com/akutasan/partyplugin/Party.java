package com.akutasan.partyplugin;

import com.akutasan.partyplugin.Listener.ServerDisconnect;
import com.akutasan.partyplugin.Listener.ServerSwitch;
import com.akutasan.partyplugin.commands.PartyChat;
import com.akutasan.partyplugin.commands.PartyCommand;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Party extends Plugin {

    public static String lang = "";
    private static Party instance;
    public static String partyprefix = "§3§lGhibli§b§lCraft §8§l| ";
    //§d§lParty §r§8●
    //§b[§5Party§b]
    //§3§lGhibli§b§lCraft §8⇨
    public static List<String> disabledServer = new ArrayList<>();

    public void onEnable()
    {
        instance = this;
        getLogger().info(ChatColor.GREEN +" successfully activated!");
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new PartyCommand());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ServerSwitch());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ServerDisconnect());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new PartyChat());
    }

    public void onDisable(){
        getLogger().info(ChatColor.RED+" successfully deactivated!");
    }

    public static Plugin getInstance()
    {
        return instance;
    }
}
