package com.akutasan.friendsgui.listener;

import com.akutasan.friendsgui.FriendsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LISTENER_PlayerJoin implements Listener {

    FriendsGUI friends;

    public LISTENER_PlayerJoin(FriendsGUI friends){
        this.friends = friends;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.updateInventory();
    }


}
