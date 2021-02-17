package com.akutasan;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    private Nickname_Manager manager;

    private File configFile;

    private YamlConfiguration config;

    private File nickFile;

    private YamlConfiguration nicks;

    private boolean duplicates;

    private String jqMsgColour;

    public void onEnable(){
        this.configFile = new File(getDataFolder(), "config.yml");
        this.nickFile = new File(getDataFolder(), "nick.yml");
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        if (!this.configFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        if (!this.nickFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.nicks = YamlConfiguration.loadConfiguration(this.nickFile);
        if (!this.config.contains("max-nick-length"))
            this.config.set("max-nick-length", 20);
        if (!this.config.contains(""))
        try {
            this.config.save(this.configFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
