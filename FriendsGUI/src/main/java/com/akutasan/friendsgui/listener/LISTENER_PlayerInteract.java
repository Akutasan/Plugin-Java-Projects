package com.akutasan.friendsgui.listener;

import com.akutasan.friendsgui.FriendsGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LISTENER_PlayerInteract implements Listener {

    FriendsGUI friends;

    public LISTENER_PlayerInteract(FriendsGUI friends){
        this.friends = friends;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        //Still under construction

//        Player player = event.getPlayer();

//        if (event.getItem() == null || player.getItemInHand() == null){
//            return;
//        }

//        if (event.getItem().getType() == Material.SKULL_ITEM){
//            if (event.getItem().getItemMeta().hasDisplayName()){
//                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§dFreunde")){
//                    friends.getMethods().loadFriendInventory(player, 1);
//                }
//            }
//        }
    }
}
