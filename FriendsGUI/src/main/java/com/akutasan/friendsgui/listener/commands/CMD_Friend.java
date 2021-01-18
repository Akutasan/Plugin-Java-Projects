package com.akutasan.friendsgui.listener.commands;

import com.akutasan.friendsgui.FriendsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Friend implements CommandExecutor {

    FriendsGUI friends;

    public CMD_Friend(FriendsGUI friends){
        this.friends = friends;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        friends.getMethods().loadFriendInventory(player, 1);
        return false;
    }
}
