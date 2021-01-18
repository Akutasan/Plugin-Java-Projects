package com.akutasan.scoreboard;

import com.akutasan.scoreboard.listener.RangChange;
import com.akutasan.scoreboard.listener.onPlayerJoin;
import com.akutasan.scoreboard.manager.*;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main instance;
    private MySQL mySQL;
    private MySQL mySQLC;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        this.fileManager = new FileManager();
        this.fileManager.init();
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new onPlayerJoin(), this);
        pluginManager.registerEvents(new OnlineTime(), this);
        pluginManager.registerEvents(new Coins(),this);
        this.mySQL = new MySQL(this.fileManager.getConfiguration().getString("Host"), this.fileManager.getConfiguration().getString("Database"), this.fileManager.getConfiguration().getString("Username"), this.fileManager.getConfiguration().getString("Password"));
        this.mySQLC = new MySQL(this.fileManager.getConfiguration().getString("Host"), this.fileManager.getConfiguration().getString("Database2"), this.fileManager.getConfiguration().getString("Username"), this.fileManager.getConfiguration().getString("Password"));
        Objects.requireNonNull(getCommand("setsc")).setExecutor(new Cmd());

        ScoreboardManager.startScheduler();
        getLogger().info(ChatColor.GREEN+"ist erfolgreich aktiviert!");
    }

    public void onDisable() {
        getLogger().info(ChatColor.GREEN+"ist erfolgreich deaktiviert!");
    }


    public static Main getInstance() {
        return instance;
    }
    public MySQL getMySQL() {
        return this.mySQL;
    }
    public MySQL getMySQLC() {
        return this.mySQLC;
    }
    public FileManager getFileManager() {
        return this.fileManager;
    }

}
