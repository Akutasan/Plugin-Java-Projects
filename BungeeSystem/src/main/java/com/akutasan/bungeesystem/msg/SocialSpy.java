package com.akutasan.bungeesystem.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akutasan.bungeesystem.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpy extends Command {
    public static HashMap<ProxiedPlayer, ProxiedPlayer> replyhash = new HashMap();
    public static ArrayList sp = new ArrayList();

    public SocialSpy() {
        super("spy", "Sys.Spy", "socialspy");
    }

    public void execute(CommandSender sender, String[] args) {
        String prefix = Main.getInstance().prefix;
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)sender;
            if (player.hasPermission("synodix.admin")) {
                if (sp.contains(player)) {
                    sp.remove(player);
                    player.sendMessage(new TextComponent(prefix + "§cSocial Spy wurde deaktiviert!"));
                    return;
                }

                sp.add(player);
                player.sendMessage(new TextComponent(prefix + "§aSocial Spy wurde aktiviert!"));
            } else {
                player.sendMessage(new TextComponent(prefix + "§cDu hast keine Berechtigung dazu!"));
            }
        }

    }
}
