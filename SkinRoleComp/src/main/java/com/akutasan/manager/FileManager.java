package com.akutasan.manager;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager
{
    private File config = new File("plugins/SkinRoleComp/config.yml");
    private FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);

    private static int cooldownTime;

    public void init()
    {
        if (!this.config.exists()) {
            try {
                this.config.createNewFile();
                this.cfg.set("cooldown:", 300);
                this.cfg.set("cooldown_msg:", "&cYou have to wait &e%time% &cbefore executing that command again!");
                this.cfg.set("message:", "&6&l(!) &6%player% &fstarted a &aSkinComp! &f%component%");
                this.cfg.set("component:", "&7(&fClick &6&nHERE&f to teleport&7)");
                this.cfg.set("messageR:", "&6&l(!) &6%player% &fstarted a &aRoleplay! &f%componentR%");
                this.cfg.set("componentR:", "&7(&fClick &6&nHERE&f to teleport&7)");
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

    public File getFile()
    {
        return this.config;
    }

    public FileConfiguration getConfiguration()
    {
        return this.cfg;
    }

    public int getCooldownTime(){
        return cooldownTime;
    }
}
