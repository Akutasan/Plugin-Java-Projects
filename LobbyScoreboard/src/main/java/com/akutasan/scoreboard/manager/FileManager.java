package com.akutasan.scoreboard.manager;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager
{
    private File file = new File("plugins/ScoreB/config.yml");
    private File scoreboardFile = new File("plugins/ScoreB/scoreboard.yml");
    private FileConfiguration scoreboardConfig = YamlConfiguration.loadConfiguration(this.scoreboardFile);
    private FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file);

    public void init()
    {
        this.configuration.options().copyDefaults(true);
        this.configuration.addDefault("Host", "DEINHOST");
        this.configuration.addDefault("Username", "DEINUSERNAME");
        this.configuration.addDefault("Database", "DEINEDATABASE");
        this.configuration.addDefault("Password", "DEINPASSWORT");
        this.configuration.addDefault("Database2", "DEINEDATABASE");
        try
        {
            this.configuration.save(this.file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.scoreboardConfig.options().copyDefaults(true);
        this.scoreboardConfig.addDefault("RangLine", " &8❙ &7Rang");
        this.scoreboardConfig.addDefault("OnlinetimeLine", " &8❙ &7Spielzeit");
        this.scoreboardConfig.addDefault("Rangzeit", " &8❙ &7Rangzeit");
//        this.scoreboardConfig.addDefault("TeamSpeak", "  &8● &eSynodix.net");
        this.scoreboardConfig.addDefault("Geldline", " &8❙ &7Coins");
//        this.scoreboardConfig.addDefault("News", "  &8● &eKnocked");
        try
        {
            this.scoreboardConfig.save(this.scoreboardFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File getScoreboardFile()
    {
        return this.scoreboardFile;
    }

    public FileConfiguration getScoreboardConfig()
    {
        return this.scoreboardConfig;
    }

    public File getFile()
    {
        return this.file;
    }

    public FileConfiguration getConfiguration()
    {
        return this.configuration;
    }
}
