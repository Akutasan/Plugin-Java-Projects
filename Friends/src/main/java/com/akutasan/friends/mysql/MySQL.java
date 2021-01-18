package com.akutasan.friends.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.akutasan.friends.Friends;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class MySQL {

    public String username;
    public String password;
    public String database;
    public String host;
    public String port;
    public Connection con;

    Friends friends;

    public MySQL(Friends friends) {

        this.friends = friends;

    }

    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true",
                        username, password);
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent(friends.getPrefix() + "§aSuccessfully connected to MySQL-Database."));
            } catch (SQLException e) {
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent(friends.getPrefix() + "§cCould not connect to MySQL-Database, please check your MySQL-Settings."));
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                con.close();
                con = null;
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent(friends.getPrefix() + "§aSuccessfully closed MySQL-Connection."));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return con != null;
    }

    public void update(String qry) {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    

    public ResultSet getResult(String qry) {
        if (isConnected()) {
            try {
                return con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}