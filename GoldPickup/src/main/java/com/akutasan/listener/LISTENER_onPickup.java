package com.akutasan.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;


public class LISTENER_onPickup implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        Player p = (Player) e.getEntity();
        if (!(e.getEntity() instanceof Player)){
            return;
        }
        if (!(e.getItem().getItemStack().getType().equals(Material.GOLD_INGOT))){
            return;
        }
        ConsoleCommandSender con = Bukkit.getServer().getConsoleSender();
        String command = "token add " + p.getName() + " " + Math.round((Math.random() * 4 + 1));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
        Bukkit.dispatchCommand(con, command);
        e.getItem().remove();
        e.setCancelled(true);
    }
}
