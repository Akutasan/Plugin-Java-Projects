package com.akutasan.reportsystem;

import com.akutasan.reportsystem.commands.Report;
import com.akutasan.reportsystem.commands.Reports;
import com.akutasan.reportsystem.commands.Teleport;
import com.akutasan.reportsystem.listener.PlayerJoinLeaveListener;
import com.akutasan.reportsystem.util.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Main extends Plugin {
    public String prefix = "§9§lSynodix §8§l× ";
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getProxy().getPluginManager().registerCommand(this, new Report());
        getProxy().getPluginManager().registerCommand(this, new Reports());
        getProxy().getPluginManager().registerCommand(this, new Teleport());
        getProxy().getPluginManager().registerListener(this, new PlayerJoinLeaveListener());
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            File file = new File(getDataFolder().getPath(), "config.yml");
            if (!file.exists()) {
                file.createNewFile();
                Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                Data.saveConfig(config, file);
                Data.reportReasons.add("Hacking");
                Data.reportReasons.add("Teaming");
                Data.reportReasons.add("Werbung");
                Data.reportReasons.add("Namensgebung");
                Data.reportReasons.add("Skin");
                Data.reportReasons.add("Cape");
                Data.reportReasons.add("Chatverhalten");
                Data.reportReasons.add("Rangausnutzung");
                Data.reportReasons.add("Bugsusing");
                Data.saveConfig(config, file);
            } else {
                Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                Data.saveConfig(config, file);
                    Data.reportReasons.add("Hacking");
                    Data.reportReasons.add("Teaming");
                    Data.reportReasons.add("Werbung");
                    Data.reportReasons.add("Namensgebung");
                    Data.reportReasons.add("Skin");
                    Data.reportReasons.add("Cape");
                    Data.reportReasons.add("Chatverhalten");
                    Data.reportReasons.add("Rangausnutzung");
                    Data.reportReasons.add("Bugsusing");
                    config.set("reason.reasons", Data.reportReasons);
                Data.saveConfig(config, file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File file2 = new File(getDataFolder().getPath(), "autologin.yml");
        if (!file2.exists())
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        getLogger().info(ChatColor.GREEN +"ist erfolgreich geladen!");
    }

    public static Main getInstance() {
        return instance;
    }

    public static boolean isActivated(String uuid) {
        File file2 = new File("plugins//Report", "autologin.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file2);
            return config.getBoolean(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void activateAutoLogin(String uuid) {
        File file2 = new File("plugins//Report", "autologin.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file2);
            config.set(uuid, Boolean.TRUE);
            Data.saveConfig(config, file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean doesUserExist(String uuid) {
        File file2 = new File("plugins//Report", "autologin.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file2);
            return (config.getString(uuid) != null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deactivateAutoLogin(String uuid) {
        File file2 = new File("plugins//Report", "autologin.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file2);
            config.set(uuid, Boolean.FALSE);
            Data.saveConfig(config, file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"ist erfolgreich heruntergefahren!");
    }
}
