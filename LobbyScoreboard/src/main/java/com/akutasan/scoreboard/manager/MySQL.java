package com.akutasan.scoreboard.manager;

import com.akutasan.scoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL{
    private String host;
    private String database;
    private String username;
    private String password;
    private Connection connection;

    public MySQL(String host, String database, String username, String password)
    {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        connect();
    }

    private void connect()
    {

        try
        {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.database, this.username, this.password);
            System.out.println("[Synodix|Scoreboard] MySQL verbunden");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            if (this.connection != null)
            {
                this.connection.close();
                System.out.println("[Synodix|Scoreboard] Die Verbindung zur MySQL wurde geschlossen");
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void update(String query)
    {
        try
        {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public ResultSet query(String query)
    {
        ResultSet resultSet = null;
        try
        {
            Statement statement = this.connection.createStatement();
            resultSet = statement.executeQuery(query);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return resultSet;
    }
}
