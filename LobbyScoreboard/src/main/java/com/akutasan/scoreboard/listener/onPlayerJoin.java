package com.akutasan.scoreboard.listener;

import com.akutasan.scoreboard.manager.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager.setScoreboard(player);
    }

    @EventHandler
    public void onLeave(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager.setScoreboard(player);
    }

}