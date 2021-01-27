package com.akutasan.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class randomSpawner{
    static World world = Bukkit.getWorld("world");
    static int x1 = 72, z1 = -60, x2 = -59, z2 = 77;

    public static void randomGold(){

        //Remove all items on ground
        List<Entity> entities = world.getEntities();
        for(Entity current : entities) {
            if (current instanceof Item) {
                current.remove();
            }
        }

        //Randomly spawn item
        ItemStack drop = new ItemStack(Material.GOLD_INGOT, 1);
        int randomNum = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        for (int i = 0; i < randomNum; i++){
            int rndmx = ThreadLocalRandom.current().nextInt(x2, x1);
            int rndmz = ThreadLocalRandom.current().nextInt(z1, z2);
            Location high = new Location(world, rndmx, 0, rndmx);
            Location location = new Location(world, rndmx, world.getHighestBlockYAt(high), rndmz);
            world.dropItem(location, drop);
        }
    }
}
