//package com.akutasan.bungeesystem.msg;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import net.md_5.bungee.api.ChatColor;
//import net.md_5.bungee.api.CommandSender;
//import net.md_5.bungee.api.ProxyServer;
//import net.md_5.bungee.api.connection.ProxiedPlayer;
//import net.md_5.bungee.api.plugin.Command;
//import net.md_5.bungee.api.plugin.Listener;
//import net.md_5.bungee.api.plugin.Plugin;
//import net.md_5.bungee.api.plugin.PluginManager;
//
//public class BungeeMsg extends Plugin implements Listener {
//    private Map<String, String> messagers = new HashMap();
//
//    public void onEnable()
//    {
//        getProxy().getPluginManager().registerListener(this, this);
//        getProxy().getPluginManager().registerCommand(this, new Command("msg")
//        {
//            public void execute(CommandSender sender, String[] args)
//            {
//                if (args.length == 0)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
//                }
//                else if (args.length == 1)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
//                }
//                else if (!(sender instanceof ProxiedPlayer))
//                {
//                    if (args[0].equalsIgnoreCase("proxy"))
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                    }
//                    else
//                    {
//                        StringBuilder str = new StringBuilder();
//                        for (int i = 1; i < args.length; i++) {
//                            str.append(args[i] + " ");
//                        }
//                        String nmessage = str.toString();
//                        String message = nmessage.replace("&", "�");
//                        ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                        if (p1 != null)
//                        {
//                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                            p1.sendMessage(ChatColor.LIGHT_PURPLE + "From proxy: " + ChatColor.RESET + message);
//                            BungeeMsg.this.messagers.put(p1.getName(), "proxy");
//                            BungeeMsg.this.messagers.put("proxy", p1.getName());
//                        }
//                        else
//                        {
//                            sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                        }
//                    }
//                }
//                else if (args[0].equals(sender.getName()))
//                {
//                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                }
//                else
//                {
//                    StringBuilder str = new StringBuilder();
//                    for (int i = 1; i < args.length; i++) {
//                        str.append(args[i] + " ");
//                    }
//                    String nmessage = str.toString();
//                    String message = nmessage.replace("&", "�");
//                    ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                    if (p1 != null)
//                    {
//                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                        p1.sendMessage(ChatColor.LIGHT_PURPLE + "From " + sender.getName() + ": " + ChatColor.RESET + message);
//                        BungeeMsg.this.messagers.put(p1.getName(), sender.getName());
//                        BungeeMsg.this.messagers.put(sender.getName(), p1.getName());
//
//                        BungeeMsg.this.logToConsole((ProxiedPlayer)sender, p1, message);
//                    }
//                    else
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                    }
//                }
//            }
//        });
//        getProxy().getPluginManager().registerCommand(this, new Command("tell")
//        {
//            public void execute(CommandSender sender, String[] args)
//            {
//                if (args.length == 0)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /tell <player> <message>");
//                }
//                else if (args.length == 1)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /tell <player> <message>");
//                }
//                else if (!(sender instanceof ProxiedPlayer))
//                {
//                    if (args[0].equalsIgnoreCase("proxy"))
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                    }
//                    else
//                    {
//                        StringBuilder str = new StringBuilder();
//                        for (int i = 1; i < args.length; i++) {
//                            str.append(args[i] + " ");
//                        }
//                        String nmessage = str.toString();
//                        String message = nmessage.replace("&", "�");
//                        ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                        if (p1 != null)
//                        {
//                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                            p1.sendMessage(ChatColor.LIGHT_PURPLE + "From proxy: " + ChatColor.RESET + message);
//                            BungeeMsg.this.messagers.put(p1.getName(), "proxy");
//                            BungeeMsg.this.messagers.put("proxy", p1.getName());
//
//                            BungeeMsg.this.logToConsole((ProxiedPlayer)sender, p1, message);
//                        }
//                        else
//                        {
//                            sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                        }
//                    }
//                }
//                else if (args[0].equals(sender.getName()))
//                {
//                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                }
//                else
//                {
//                    StringBuilder str = new StringBuilder();
//                    for (int i = 1; i < args.length; i++) {
//                        str.append(args[i] + " ");
//                    }
//                    String nmessage = str.toString();
//                    String message = nmessage.replace("&", "�");
//                    ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                    if (p1 != null)
//                    {
//                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                        p1.sendMessage(ChatColor.LIGHT_PURPLE + "From " + sender.getName() + ": " + ChatColor.RESET + message);
//                        BungeeMsg.this.messagers.put(p1.getName(), sender.getName());
//                        BungeeMsg.this.messagers.put(sender.getName(), p1.getName());
//
//                        BungeeMsg.this.logToConsole((ProxiedPlayer)sender, p1, message);
//                    }
//                    else
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                    }
//                }
//            }
//        });
//        getProxy().getPluginManager().registerCommand(this, new Command("w")
//        {
//            public void execute(CommandSender sender, String[] args)
//            {
//                if (args.length == 0)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /w <player> <message>");
//                }
//                else if (args.length == 1)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /w <player> <message>");
//                }
//                else if (!(sender instanceof ProxiedPlayer))
//                {
//                    if (args[0].equalsIgnoreCase("proxy"))
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                    }
//                    else
//                    {
//                        StringBuilder str = new StringBuilder();
//                        for (int i = 1; i < args.length; i++) {
//                            str.append(args[i] + " ");
//                        }
//                        String nmessage = str.toString();
//                        String message = nmessage.replace("&", "�");
//                        ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                        if (p1 != null)
//                        {
//                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                            p1.sendMessage(ChatColor.LIGHT_PURPLE + "From proxy: " + ChatColor.RESET + message);
//                            BungeeMsg.this.messagers.put(p1.getName(), "proxy");
//                            BungeeMsg.this.messagers.put("proxy", p1.getName());
//
//                            BungeeMsg.this.logToConsole((ProxiedPlayer)sender, p1, message);
//                        }
//                        else
//                        {
//                            sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                        }
//                    }
//                }
//                else if (args[0].equals(sender.getName()))
//                {
//                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You cannot send message to yourself");
//                }
//                else
//                {
//                    StringBuilder str = new StringBuilder();
//                    for (int i = 1; i < args.length; i++) {
//                        str.append(args[i] + " ");
//                    }
//                    String nmessage = str.toString();
//                    String message = nmessage.replace("&", "�");
//                    ProxiedPlayer p1 = BungeeMsg.this.getProxy().getPlayer(args[0]);
//                    if (p1 != null)
//                    {
//                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + args[0] + ": " + ChatColor.RESET + message);
//                        p1.sendMessage(ChatColor.LIGHT_PURPLE + "From " + sender.getName() + ": " + ChatColor.RESET + message);
//                        BungeeMsg.this.messagers.put(p1.getName(), sender.getName());
//                        BungeeMsg.this.messagers.put(sender.getName(), p1.getName());
//
//                        BungeeMsg.this.logToConsole((ProxiedPlayer)sender, p1, message);
//                    }
//                    else
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "Could not find player " + args[0]);
//                    }
//                }
//            }
//        });
//        getProxy().getPluginManager().registerCommand(this, new Command("r")
//        {
//            public void execute(CommandSender sender, String[] args)
//            {
//                if (args.length == 0)
//                {
//                    sender.sendMessage(ChatColor.RED + "Usage: /r <message>");
//                }
//                else if (!(sender instanceof ProxiedPlayer))
//                {
//                    if (!BungeeMsg.this.messagers.containsKey("proxy"))
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You have nobody to reply to, you must reieve a message first");
//                        return;
//                    }
//                    ProxiedPlayer target = BungeeMsg.this.getProxy().getPlayer((String)BungeeMsg.this.messagers.get("proxy"));
//                    if (target == null)
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "The player you last got a message from is not online");
//                        return;
//                    }
//                    StringBuilder str = new StringBuilder();
//                    for (int i = 0; i < args.length; i++) {
//                        str.append(args[i] + " ");
//                    }
//                    String nmessage = str.toString();
//                    String message = nmessage.replace("&", "�");
//
//                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + target.getName() + ": " + ChatColor.RESET + message);
//                    target.sendMessage(ChatColor.LIGHT_PURPLE + "From proxy: " + ChatColor.RESET + message);
//
//                    BungeeMsg.this.messagers.put(target.getName(), "proxy");
//                    BungeeMsg.this.messagers.put("proxy", target.getName());
//
//                    BungeeMsg.this.logToConsole((ProxiedPlayer)sender, target, message);
//                }
//                else
//                {
//                    if (!BungeeMsg.this.messagers.containsKey(sender.getName()))
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "You have nobody to reply to, you must reieve a message first");
//                        return;
//                    }
//                    if (((String)BungeeMsg.this.messagers.get(sender.getName())).equalsIgnoreCase("proxy"))
//                    {
//                        StringBuilder str = new StringBuilder();
//                        for (int i = 0; i < args.length; i++) {
//                            str.append(args[i] + " ");
//                        }
//                        String nmessage = str.toString();
//                        String message = nmessage.replace("&", "�");
//
//                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "To proxy: " + ChatColor.RESET + message);
//                        BungeeMsg.this.getProxy().getLogger().log(Level.INFO, ChatColor.LIGHT_PURPLE + "From " + sender.getName() + ": " + ChatColor.RESET + message);
//
//                        BungeeMsg.this.messagers.put("proxy", sender.getName());
//                        BungeeMsg.this.messagers.put(sender.getName(), "proxy");
//                        return;
//                    }
//                    ProxiedPlayer target = BungeeMsg.this.getProxy().getPlayer((String)BungeeMsg.this.messagers.get(sender.getName()));
//                    if (target == null)
//                    {
//                        sender.sendMessage(ChatColor.RED + ChatColor.BOLD + "The player you last got a message from is not online");
//                    }
//                    else
//                    {
//                        StringBuilder str = new StringBuilder();
//                        for (int i = 0; i < args.length; i++) {
//                            str.append(args[i] + " ");
//                        }
//                        String nmessage = str.toString();
//                        String message = nmessage.replace("&", "�");
//
//                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "To " + target.getName() + ": " + ChatColor.RESET + message);
//                        target.sendMessage(ChatColor.LIGHT_PURPLE + "From " + sender.getName() + ": " + ChatColor.RESET + message);
//
//                        BungeeMsg.this.messagers.put(target.getName(), sender.getName());
//                        BungeeMsg.this.messagers.put(sender.getName(), target.getName());
//
//                        BungeeMsg.this.logToConsole((ProxiedPlayer)sender, target, message);
//                    }
//                }
//            }
//        });
//    }
//
//    public void logToConsole(ProxiedPlayer player, ProxiedPlayer target, String message)
//    {
//        getProxy().getLogger().log(Level.INFO, "*** BungePrivateMessage *** " + player.getName() + " to " + target.getName() + " >> " + message);
//    }
//}
//
