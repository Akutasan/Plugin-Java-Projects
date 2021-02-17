package com.akutasan.creative_utilities.commands;

import com.akutasan.creative_utilities.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMD_nv implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("nv")) {
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Main.getInstance().getFileManager().getConfiguration().getString("NV-Disabled"))));
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Main.getInstance().getFileManager().getConfiguration().getString("NV-Enabled"))));
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999,250, true, false));
                return true;
            }
            if (args.length != 1 && args.length != 0){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Main.getInstance().getFileManager().getConfiguration().getString("NV-NoCommand"))));
            }
        }
        return false;
    }
}

