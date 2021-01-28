package com.akutasan;

import com.akutasan.manager.CMD_eventtp;
import com.akutasan.manager.CMD_roleplay;
import com.akutasan.manager.CMD_skincomp;
import com.akutasan.manager.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    private FileManager fileManager;
    private static Main instance;




    public void onEnable() {
        instance = this;
        this.fileManager = new FileManager();
        this.fileManager.init();
        Objects.requireNonNull(getCommand("roleplay")).setExecutor(new CMD_roleplay());
        Objects.requireNonNull(getCommand("skincomp")).setExecutor(new CMD_skincomp());
        Objects.requireNonNull(getCommand("eventtp")).setExecutor(new CMD_eventtp());
        getLogger().info("SkinRoleComp successfully enabled!");
    }



    @Override
    public void onDisable() {
        getLogger().info("SkinRoleComp successfully disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

}
