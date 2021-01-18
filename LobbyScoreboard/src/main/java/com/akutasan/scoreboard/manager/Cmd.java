package com.akutasan.scoreboard.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Cmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("setsc")){
            ScoreboardManager.setScoreboard(player);
        }
        return true;
    }
}

