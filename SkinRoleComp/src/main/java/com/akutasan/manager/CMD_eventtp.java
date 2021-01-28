package com.akutasan.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMD_eventtp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("eventtp")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("M2kKcMPzE7KtajLz")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    p.teleport(Objects.requireNonNull(target).getLocation());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6&l(!) &fYou have been teleported to &6"+target.getDisplayName()+"&7!"));
                } else {
                    p.sendMessage(ChatColor.RED + "No permission!");
                }
            }
        }
        return false;
    }
}
