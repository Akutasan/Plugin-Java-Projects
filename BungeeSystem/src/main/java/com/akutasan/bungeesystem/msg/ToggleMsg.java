package com.akutasan.bungeesystem.msg;

import java.util.ArrayList;
import java.util.List;

import com.akutasan.bungeesystem.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleMsg extends Command {
    public static List<ProxiedPlayer> tmsg = new ArrayList();

    public ToggleMsg() {
        super("msgtoggle", "", "msgt","togglemsg","tmsg");
    }

    public void execute(CommandSender sender, String[] args) {
        String prefix = Main.getInstance().prefix;
        if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer)sender;
                if (tmsg.contains(player)) {
                    tmsg.remove(player);
                    player.sendMessage(new TextComponent(prefix + "§aDu §awirst wieder §aNachrichten §aerhalten!"));
                    return;
                }

                tmsg.add(player);
                player.sendMessage(new TextComponent(prefix + "§cDu wirst nun §ckeine §cNachrichten §cmehr §cerhalten!"));
        } else {
            sender.sendMessage(new TextComponent(prefix + "§cDu musst ein Spieler §csein um §cdies §czu §ctun!"));
        }

    }
}