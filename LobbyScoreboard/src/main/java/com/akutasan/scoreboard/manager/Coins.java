package com.akutasan.scoreboard.manager;

import com.akutasan.scoreboard.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Coins implements Listener {
    public static HashMap<UUID, Integer> joinCoins = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        joinCoins.put(e.getPlayer().getUniqueId(), 69);
        e.getPlayer().sendMessage(String.valueOf(getCoins(e.getPlayer().getUniqueId())));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        joinCoins.remove(uuid);
    }

    public static long getCoins(UUID uuid) {
        String sql = "SELECT Coins FROM Data WHERE UUID ='"+uuid+"';";
        long coins = 0;
        try{
            ResultSet resultSet = Main.getInstance().getMySQLC().query(sql);
            while (resultSet.next()) {
                coins += (resultSet.getLong("Coins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }
}
