package com.akutasan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Main extends JavaPlugin {
    File config = new File("plugins/SkinRoleComp", "config.yml");

    FileConfiguration cfg;

    private static int cooldownTime;

    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (!this.config.exists()) {
            try {
                this.config.createNewFile();
                this.cfg = YamlConfiguration.loadConfiguration(this.config);
                this.cfg.set("#This is for Skin Competition!", null);
                this.cfg.set("cooldown:", 3600);
                this.cfg.set("cooldown_msg:", "&cYou have to wait &e%time% &cbefore executing that command again!");
                this.cfg.set("message:", "&e%player% &ahas &astarded &aa &askin &acomp! %component%");
                this.cfg.set("component:", "&7(&eteleport&7)");
                this.cfg.set("event.hover:", "/tp %player%");
                this.cfg.set(" ",null);
                this.cfg.set("#This is for Rollplay!", null);
                this.cfg.set("cooldownR:", 3600);
                this.cfg.set("cooldown_msgR:", "&cYou have to wait &e%time% &cbefore executing that command again!");
                this.cfg.set("messageR:", "&e%player% &ahas &astarded &aa &askin &acomp! %component%");
                this.cfg.set("componentR:", "&7(&eteleport&7)");
                this.cfg.set("event.hoverR:", "/tp %player%");
                this.cfg.save(this.config);
                cooldownTime = this.cfg.getInt("cooldown:");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.cfg = YamlConfiguration.loadConfiguration(this.config);
            cooldownTime = this.cfg.getInt("cooldown:");
        }

    }

    public int getCooldownTime() {
        return cooldownTime;
    }


    private Long secondsBetween(Date first, Date second) {
        return (first.getTime() - second.getTime()) / 1000L;
    }
}
