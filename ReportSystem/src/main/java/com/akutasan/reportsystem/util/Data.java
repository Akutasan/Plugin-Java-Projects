package com.akutasan.reportsystem.util;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    public static ArrayList<String> reportReasons = new ArrayList<>();

    public static HashMap<ProxiedPlayer, String> reports = new HashMap<>();

    public static HashMap<String, String> offlinereports = new HashMap<>();

    public static HashMap<ProxiedPlayer, ProxiedPlayer> waitForAnswer = new HashMap<>();

    public static ArrayList<ProxiedPlayer> login = new ArrayList<>();

    public static void saveConfig(Configuration config, File file) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
