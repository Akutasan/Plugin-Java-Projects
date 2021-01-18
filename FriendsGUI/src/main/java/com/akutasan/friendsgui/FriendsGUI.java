package com.akutasan.friendsgui;

import com.akutasan.friendsgui.api.CookieAPI;
import com.akutasan.friendsgui.listener.LISTENER_InventoryClickListener;
import com.akutasan.friendsgui.listener.LISTENER_PlayerInteract;
import com.akutasan.friendsgui.listener.LISTENER_PlayerJoin;
import com.akutasan.friendsgui.listener.commands.CMD_Friend;
import com.akutasan.friendsgui.manager.Methods;
import com.akutasan.friendsgui.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendsGUI extends JavaPlugin {

    public MySQL mysql;
    public CookieAPI api;

    private Methods methods;

    @Override
    public void onEnable() {
        fetchClasses();

        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        getMySQL().CreateMySQLFile("FriendsGUI");

        this.getServer().getPluginManager().registerEvents(new LISTENER_InventoryClickListener(this), this);
        this.getCommand("friendsgui").setExecutor(new CMD_Friend(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        getMySQL().close();
    }

    public void fetchClasses(){
        mysql = new MySQL(this);
        api = new CookieAPI(this, "");
        methods = new Methods(this);
    }

    public MySQL getMySQL(){
        return mysql;
    }

    public CookieAPI getApi() {
        return api;
    }

    public Methods getMethods(){
        return methods;
    }

    public String getPrefix(){
        return "§cReqiuem §7• ";
    }
}
