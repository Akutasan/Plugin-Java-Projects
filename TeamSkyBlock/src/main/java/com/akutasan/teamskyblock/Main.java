package com.akutasan.teamskyblock;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new EVENT_BlockFormEvent(this), this);
        getLogger().info(ChatColor.GREEN+" ist erfolgreich aktiviert!");
    }

    public void onDisable() {
        getLogger().info(ChatColor.GREEN+" ist erfolgreich deaktiviert!");
    }

}
