package com.akutasan.teamskyblock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EVENT_BlockFormEvent implements Listener{
    private Main _main;
    private final BlockFace[] faces;

    private final double coal = 10;
    private final double iron = 8 + coal;
    private final double gold = 2 + iron;
    private final double redstone = 3 + gold;
    private final double lapis = 1 + redstone;
    private final double emerald = 1 + lapis;
    private final double diamond = 1 + emerald;
    private final double quartz = 2 + diamond;
    private final double clay = 5 + quartz;
    private final double andesite = 6 + clay;


    public EVENT_BlockFormEvent(Main main) {
        this.faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
        this._main = main;
    }

    public static int rand(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    double a;


    @EventHandler
    public void onFromTo(BlockFormEvent event){
        Material type = event.getBlock().getType();
        if (type != Material.LAVA) return;
        Block b = event.getBlock();
        event.setCancelled(true);
        a = rand(1, 100);
        if (a < coal){
            b.setType(Material.COAL_ORE);
        } else if (a > coal && a <= iron){
            b.setType(Material.IRON_ORE);
        } else if (a > iron && a <= gold){
            b.setType(Material.GOLD_ORE);
        } else if (a > gold && a <= redstone){
            b.setType(Material.REDSTONE_ORE);
        } else if (a > redstone && a <= lapis){
            b.setType(Material.LAPIS_ORE);
        } else if (a > lapis && a <= emerald){
            b.setType(Material.EMERALD_ORE);
        } else if (a > emerald && a <= diamond){
            b.setType(Material.DIAMOND_ORE);
        } else if (a > diamond && a <= quartz){
            b.setType(Material.NETHER_QUARTZ_ORE);
        } else if (a > quartz && a <= clay){
            b.setType(Material.CLAY);
        } else if (a > clay && a <= andesite){
            b.setType(Material.ANDESITE);
        } else {
            b.setType(Material.STONE);
        }
    }
}
