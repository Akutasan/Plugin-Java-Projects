package com.akutasan.manager;

import com.akutasan.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

public class COM_roleplay implements CommandExecutor {
    HashMap<UUID, Long> cooldown = new HashMap<>();

    private Main main;
    private Callback SpigotCallback;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("roleplay")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
                    if (this.cooldown.containsKey(p.getUniqueId())) {
                        long secondsLeft = this.cooldown.get(p.getUniqueId()) / 1000L + main.getCooldownTime() - System.currentTimeMillis() / 1000L;
                        if (secondsLeft > 0L) {
                            long millis = (this.cooldown.get(p.getUniqueId()) / 1000L + main.getCooldownTime() - System.currentTimeMillis() / 1000L) * 1000L;
                            long second = millis / 1000L % 60L;
                            long minute = millis / 60000L % 60L;
                            long hour = millis / 3600000L % 24L;
                            String time = String.format("%02d:%02d:%02d", hour, minute, second);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(main.getConfig().getString("cooldown_msg:")).replace("%time%", time)));
                        } else {
                            this.cooldown.remove(p.getUniqueId());
                        }
                    } else {
                        String[] split = Objects.requireNonNull(main.getConfig().getString("message:")).split(" ");
                        TreeMap<Integer, TextComponent> components = new TreeMap<>();
                        for (int i = 0; i < split.length; i++) {
                            String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', split[i]));
                            if (str.equalsIgnoreCase("%component%")) {
                                if (i != split.length) {
                                    TextComponent comp = new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i].replace("%component%", main.getConfig().getString("component:") + " ")));
                                    if (main.getConfig().getString("event.click:") != null)
//                                        comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "eventtp" + p.getName()));
                                        Callback.createCommand(comp, player -> {
                                            player.teleport(p.getLocation());
                                        });
                                    if (main.getConfig().getString("event.hover:") != null)
                                        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(main.getConfig().getString("event.hover:"))).create()));
                                    components.put(i, comp);
                                } else {
                                    TextComponent comp = new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i].replace("%component%", main.getConfig().getString("component:"))));
                                    if (main.getConfig().getString("event.click:") != null)
//                                        comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "eventtp" + p.getName()));
                                        Callback.createCommand(comp, player -> {
                                            player.teleport(p.getLocation());
                                        });
                                    if (main.getConfig().getString("event.hover:") != null)
                                        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(main.getConfig().getString("event.hover:"))).create()));
                                    components.put(i, comp);
                                }
                            } else if (i != split.length) {
                                components.put(i, new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i]).replace("%player%", p.getName()).replace("%component%", "") + " "));
                            } else {
                                components.put(i, new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i]).replace("%player%", p.getName()).replace("%component%", "")));
                            }
                        }
                        TextComponent whole = new TextComponent("");
                        for (int j = 0; j < components.keySet().size(); j++)
                            whole.addExtra(components.get(j));
                        Bukkit.spigot().broadcast(whole);
                        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    }
                    return true;
                } else {
                    p.sendMessage(String.valueOf(new TextComponent(ChatColor.DARK_RED + "Usage: /roleplay start")));
                }
            }
        } else {
            System.err.println("[RolePlay] This is not a console command!");
        }
        return false;
    }
}

