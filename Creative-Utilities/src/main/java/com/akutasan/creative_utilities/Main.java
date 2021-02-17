package com.akutasan.creative_utilities;

import com.akutasan.creative_utilities.commands.CMD_nv;
import com.akutasan.creative_utilities.manager.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    private FileManager fileManager;
    private static Main instance;


    public void onEnable() {
        instance = this;
        this.fileManager = new FileManager();
        this.fileManager.init();
        Objects.requireNonNull(getCommand("nv")).setExecutor(new CMD_nv());
        getLogger().info("NightVision successfully enabled!");
    }


    @Override
    public void onDisable() {
        getLogger().info("NightVision successfully disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }
}
