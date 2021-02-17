package com.akutasan.manager;

import com.akutasan.SkinRoleComp;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

public class CMD_skincomp implements CommandExecutor {
    HashMap<UUID, Long> cooldown = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("skincomp")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
                    if (this.cooldown.containsKey(p.getUniqueId())) {
                        long secondsLeft = this.cooldown.get(p.getUniqueId()) / 1000L + SkinRoleComp.getInstance().getFileManager().getCooldownTime() - System.currentTimeMillis() / 1000L;
                        if (secondsLeft > 0L) {
                            long millis = (this.cooldown.get(p.getUniqueId()) / 1000L + SkinRoleComp.getInstance().getFileManager().getCooldownTime() - System.currentTimeMillis() / 1000L) * 1000L;
                            long second = millis / 1000L % 60L;
                            long minute = millis / 60000L % 60L;
                            long hour = millis / 3600000L % 24L;
                            String time = String.format("%02d:%02d:%02d", hour, minute, second);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(SkinRoleComp.getInstance().getFileManager().getConfiguration().getString("cooldown_msg:")).replace("%time%", time)));
                        } else {
                            this.cooldown.remove(p.getUniqueId());
                        }
                    } else {
                        String[] split = Objects.requireNonNull(SkinRoleComp.getInstance().getFileManager().getConfiguration().getString("message:")).split(" ");
                        TreeMap<Integer, TextComponent> components = new TreeMap<>();
                        for (int i = 0; i < split.length; i++) {
                            String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', split[i]));
                            if (str.equalsIgnoreCase("%component%")) {
                                if (i != split.length) {
                                    TextComponent comp = new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i].replace("%component%", SkinRoleComp.getInstance().getFileManager().getConfiguration().getString("component:") + " ")));
                                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/eventtp " + p.getName() + " M2kKcMPzE7KtajLz"));
                                    components.put(i, comp);
                                } else {
                                    TextComponent comp = new TextComponent(ChatColor.translateAlternateColorCodes('&', split[i].replace("%component%", SkinRoleComp.getInstance().getFileManager().getConfiguration().getString("component:"))));
                                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/eventtp " + p.getName() + " M2kKcMPzE7KtajLz"));
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
                    p.sendMessage(ChatColor.RED + "Usage: /skincomp start");
                }
            }
        } else {
            System.err.println("[SkinComp] This is not a console command!");
        }
        return false;
    }
}

