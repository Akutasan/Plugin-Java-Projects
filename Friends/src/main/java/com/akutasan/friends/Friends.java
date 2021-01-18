package com.akutasan.friends;

import com.akutasan.friends.commands.CMD_Friend;
import com.akutasan.friends.listener.LISTENER_FriendHandler;
import com.akutasan.friends.manager.FileManager;
import com.akutasan.friends.manager.FriendManager;
import com.akutasan.friends.mysql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class Friends extends Plugin {

    private MySQL mysql;
    private FileManager fileManager;
    private FriendManager friendManager;

    @Override
    public void onEnable() {
        fetchClasses();

        getProxy().getPluginManager().registerCommand(this, new CMD_Friend(this));
        getProxy().getPluginManager().registerListener(this, new LISTENER_FriendHandler(this));

        loadMySQLFiles();

        if (getMySQL().isConnected()){
            getFriendManager().createTables();
        }
        getLogger().info(ChatColor.GREEN+"ist erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getMySQL().close();
        getLogger().info(ChatColor.RED+"ist erfolgreich heruntergefahren!");
    }

    public void fetchClasses(){
        mysql =  new MySQL(this);
        fileManager = new FileManager(this);
        friendManager = new FriendManager(this);
    }

    public void loadMySQLFiles(){
        getDataFolder().mkdirs();
        if (!fileManager.exists("mysql.yml","Friends")){
            File file = fileManager.createNewFile("mysql.yml","Friends");
            Configuration cfg = fileManager.getConfiguration("mysql.yml","Friends");
            cfg.set("Username","DEINUSERNAME");
            cfg.set("Password","DEINPASSWORT");
            cfg.set("Host","DEINHOST");
            cfg.set("Database","DEINEDATABASE");

            fileManager.saveFile(file,cfg);
        }
        Configuration cfg = fileManager.getConfiguration("mysql.yml","Friends");

        getMySQL().username = cfg.getString("Username");
        getMySQL().host = cfg.getString("Host");
        getMySQL().database = cfg.getString("Database");
        getMySQL().password = cfg.getString("Password");

        getMySQL().connect();
    }

    public String getPrefix(){
        return "§cReqiuem §7• ";
    }

    public MySQL getMySQL(){
        return mysql;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public FriendManager getFriendManager(){
        return friendManager;
    }
}
