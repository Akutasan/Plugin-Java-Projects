package com.akutasan.bungeesystem.joinme;

import com.akutasan.bungeesystem.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class JoinmeComm extends Command {


    public JoinmeComm(){
        super("joinme","sys.joinme");
    }
    private final List<UUID> cooldown = new ArrayList<>();

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String prefix = Main.getInstance().prefix;
        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;

            if (this.cooldown.contains(p.getUniqueId())){
                p.sendMessage(new TextComponent(prefix + "§cDu bist noch in der Cooldown Periode!"));
                return;
            }

            if (!p.hasPermission("sys.joinmebypass")){
                this.startCooldown(p.getUniqueId());
            }

            for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                if (all.toString().equalsIgnoreCase(p.toString())) {
                    p.sendMessage(new TextComponent(prefix + "§aDein JoinMe wurde losgeschickt!"));
                    continue;
                }
                all.sendMessage(new TextComponent(" "));
                all.sendMessage(new TextComponent(prefix + "§6" + p + " §aspielt gerade auf §a" + p.getServer().getInfo().getName()));
                TextComponent tc = new TextComponent();
                tc.setText(prefix);
                TextComponent tc4 = new TextComponent();
                tc4.setText("Klicke hier um zu joinen!");
                tc4.setColor(ChatColor.YELLOW);
                tc4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + p.getServer().getInfo().getName()));
                tc4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§6" + p + " §ajoinen!")).create()));
                tc.addExtra(tc4);
                all.sendMessage(tc);
                all.sendMessage(new TextComponent(" "));
            }
        }
    }

    private void startCooldown(UUID uniqueID){
        this.cooldown.add(uniqueID);
        ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> cooldown.remove(uniqueID), 1, TimeUnit.HOURS);
    }
}

