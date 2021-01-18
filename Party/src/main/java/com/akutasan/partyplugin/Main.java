package com.akutasan.partyplugin;

import com.akutasan.partyplugin.Listener.ServerDisconnect;
import com.akutasan.partyplugin.Listener.ServerSwitch;
import com.akutasan.partyplugin.commands.PartyChat;
import com.akutasan.partyplugin.commands.PartyCommand;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends Plugin {

    public static String lang = "";
    private static Main instance;
    public static String partyprefix = "§9§lSynodix §8§l× ";
    public static List<String> disabledServer = new ArrayList<>();

    public void onEnable()
    {
        instance = this;
        getLogger().info(ChatColor.GREEN +"ist erfolgreich geladen!");
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new PartyCommand());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ServerSwitch());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ServerDisconnect());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new PartyChat());
    }

    public void onDisable(){
        getLogger().info(ChatColor.RED+"ist erfolgreich heruntergefahren!");
    }

    public static Plugin getInstance()
    {
        return instance;
    }
}
