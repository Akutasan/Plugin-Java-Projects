package com.akutasan.scoreboard.manager;


import com.akutasan.scoreboard.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnlineTime implements Listener {
    public static HashMap<UUID, Long> joinTimes = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        joinTimes.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        joinTimes.remove(uuid);
    }

    public static long getOnlineTime(UUID uuid) {
        String sql = "SELECT PlayTime FROM PlayerTimes WHERE uuid ='"+uuid+"';";
        long onlineTime = 0;
        try {
            ResultSet resultSet = Main.getInstance().getMySQL().query(sql);
            while (resultSet.next()) {
                onlineTime += (resultSet.getLong("PlayTime"));
            }
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return onlineTime;
    }
}
