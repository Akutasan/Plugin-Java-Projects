package com.akutasan;

import com.akutasan.listener.LISTENER_onPickup;
import com.akutasan.manager.randomSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class GoldPickup extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new LISTENER_onPickup(), this);
        getLogger().info("GoldPickup successfully enabled!");
        startScheduler();
    }

    public void startScheduler(){
        new BukkitRunnable() {
            @Override
            public void run() {
                randomSpawner.randomGold();
            }
        }.runTaskTimer(this, 0, 600);
    }

    public void onDisable() {
        getLogger().info("GoldPickup successfully disabled!");
    }
}
