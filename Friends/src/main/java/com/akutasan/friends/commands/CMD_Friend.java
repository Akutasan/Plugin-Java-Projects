package com.akutasan.friends.commands;

import com.akutasan.friends.Friends;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;

public class CMD_Friend extends Command {

    Friends friends;

    public CMD_Friend(Friends friends){
        super("friends");
        this.friends = friends;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (args.length == 0){
                sendHelp(p, 1);
            } else if (args[0].equalsIgnoreCase("add")){
                if (args.length == 2){
                    String target = args[1];
                    ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);
                    if (friend == null){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler §cexestiert §cnicht oder §cist §coffline!"));
                        return;
                    }
                    if (p.getName().equals(friend.getName())){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst dich nicht selber anfreunden!"));
                        return;
                    }
                    if (friends.getFriendManager().getRequestListRAW(p.getName()).contains(friend.getUniqueId().toString())){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast die §aFreundschaftsanfrage von §e"+friend.getName()+" §aangenommen!"));
                        friends.getFriendManager().removeRequest(p.getName(), friend.getUniqueId().toString());
                        friends.getFriendManager().addFriend(p.getName(), friend.getUniqueId().toString());
                        friends.getFriendManager().addFriend(friend.getName(), p.getUniqueId().toString());
                        if (friend != null) {
                            friend.sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+" §ahat deine §aFreundschaftsanfrage §aangenommen!"));
                        }
                        return;
                    }
                    if (friends.getFriendManager().getRequestListRAW(friend.getName()).contains(p.getUniqueId().toString())){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast §e"+friend+" §cschon eine §cFreundschaftsanfrage §cgesendet!"));
                        return;
                    }
                    if (!friends.getFriendManager().getSetting(friend.getName(), "FRequest")){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst diesem Spieler §ckeine §cFreundschaftsanfragen §cschicken!"));
                        return;
                    }
                    if (friends.getFriendManager().getFriends(friend.getName()) >= 100){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDer Spieler hat sein §cFreundschaftslimit §cerreicht!"));
                        return;
                    }
                    if (friends.getFriendManager().getFriendListRAW(friend.getName()).contains(p.getUniqueId().toString())){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu bist bereits mit diesem Spieler befreundet!"));
                        return;
                    }
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast " + friend.getName() + " §aeine §aFreundschaftsanfrage §ageschickt!"));
                    friend.sendMessage(new TextComponent(friends.getPrefix() + "§e" + p.getName() + " §ahat dir §aeine §aFreunschaftsanfrage §ageschickt!"));
                    sendConfirmation(friend, p);
                    friends.getFriendManager().addRequest(friend.getName(), p.getUniqueId().toString());
                } else {
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friend add <Spieler>"));
                }
            } else if (args[0].equalsIgnoreCase("remove")){
                if (args.length == 2){

                    String friend = args[1];
                    if (friends.getFriendManager().existPlayerName(friend)) {
                        String uuid = friends.getFriendManager().getUUIDbyName(friend, "friends_Users");

                        if (friends.getFriendManager().getFriendListRAW(p.getName()).contains(uuid)) {

                            friends.getFriendManager().removeFriend(friend, p.getUniqueId().toString());
                            friends.getFriendManager().removeFriend(p.getName(), uuid);

                            p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast §e" + friend + " §aerfolgreich aus §adeiner §aFreundesliste §aentfernt!"));

                            ProxiedPlayer f = ProxyServer.getInstance().getPlayer(friend);
                            if (f != null){
                                f.sendMessage(new TextComponent(friends.getPrefix()+"§e"+p.getName()+" §chat dich aus seiner §cFreundesliste §centfernt!"));
                            }
                        } else {
                            p.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler ist §cexistiert nicht §coder ist nicht §cin §cdeiner §cFreundesliste!"));
                        }
                    } else {
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler ist in nicht §cunserer §cDatenbank!"));
                    }
                } else {
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friend remove <Spieler>"));
                }
            } else if (args[0].equalsIgnoreCase("deny")){
                if (args.length == 2){
                    String tp = args[1];
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(tp);
                    String searchUUID;
                    if (target != null){
                        searchUUID = target.getUniqueId().toString();
                    } else {
                        searchUUID = friends.getFriendManager().getUUIDbyName(tp, "friends_Users");
                    }

                    if (friends.getFriendManager().getRequestListRAW(p.getName()).contains(searchUUID) && !searchUUID.isEmpty()){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast die §cFreundschaftsanfrage §cvon §e"+target+" §cabgelehnt!"));
                        friends.getFriendManager().removeRequest(p.getName(), searchUUID);
                        if (target != null) {
                            target.sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+"§chat deine §cFreundschaftsanfrage §cabgelehnt!"));
                        }
                    } else {
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu §chast §ckeine §cAnfragen von §e"+target));
                    }
                } else {
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friend deny <Spieler>"));
                }

            } else if (args[0].equalsIgnoreCase("accept")){
                if (args.length == 2){
                    String tp = args[1];
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(tp);
                    String searchUUID;
                    if (target != null){
                        searchUUID = target.getUniqueId().toString();
                    } else {
                        searchUUID = friends.getFriendManager().getUUIDbyName(tp, "friends_Users");
                    }

                    if (friends.getFriendManager().getRequestListRAW(p.getName()).contains(searchUUID) && !searchUUID.isEmpty()){
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast die §aFreundschaftsanfrage §avon §e"+tp+" §aangenommen!"));
                        friends.getFriendManager().removeRequest(p.getName(), searchUUID);
                        friends.getFriendManager().addFriend(p.getName(), searchUUID);
                        friends.getFriendManager().addFriend(tp, p.getUniqueId().toString());
                        if (target != null) {
                            target.sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+" §ahat deine §aFreundschaftsanfrage §aangenommen!"));
                        }
                    } else {
                        p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast §ckeine §cAnfragen von §e"+target));
                    }
                } else {
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friend accept <Spieler>"));
                }
            } else if (args[0].equalsIgnoreCase("list")){
                p.sendMessage(new TextComponent(friends.getPrefix() + "§dFreundesliste §8|§e "+friends.getFriendManager().getFriends(p.getName())+" §8/§e100§7"));
                HashMap<String, List<String>> friendlist = friends.getFriendManager().getList(p.getName());
                List<String> online = friendlist.get("online");
                List<String> offline = friendlist.get("offline");
                for (String friend : online){
                    String server;
                    try {
                        server = ProxyServer.getInstance().getPlayer(friend).getServer().getInfo().getName();
                    } catch (Exception e){
                        server = "Connecting...";
                    }
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§9"+friend+" §8| §7Online auf §e"+server));
                }
                for (String friend: offline){
                    p.sendMessage(new TextComponent(friends.getPrefix()+"§7"+friend+" §8| §7Zuletzt online: §e"+ getLastTimeOnline((Long) friends.getFriendManager().get(friend, "Name", "FConnect","friends_Users")) + ""));
                }
            } else if (args[0].equalsIgnoreCase("requests")){

                p.sendMessage(new TextComponent(friends.getPrefix() + "§9Freundschaftsanfragen §7|§e "+friends.getFriendManager().getRequests(p.getName()) + "§7/§e50"));
                for (String rq : friends.getFriendManager().getRequestList(p.getName())){
                    sendRequestConfirmation(p, friends.getFriendManager().getNamebyUUID(rq, "friends_Users"));
                }

            } else if (args[0].equalsIgnoreCase("clear")){

                List<String> friendlist = friends.getFriendManager().getFriendList(p.getName());
                if (friendlist.size() == 0){
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast keine Freunde!"));
                    return;
                }
                for (String friend : friendlist){
                    String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");

                    friends.getFriendManager().removeFriend(name, p.getUniqueId().toString());
                    friends.getFriendManager().removeFriend(p.getName(), friend);

                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast die Freundschaft mit §e"+name+" §cbeendet!"));
                    if (ProxyServer.getInstance().getPlayer(name) != null){
                        ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+" §chat eure §cFreundschaft §caufgelöst!"));
                    }
                }

            } else if (args[0].equalsIgnoreCase("acceptall")){

                List<String> requestList = friends.getFriendManager().getRequestList(p.getName());
                if (requestList.size() == 0){
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast zurzeit §ckeine §cFreundschaftsanfragen!"));
                    return;
                }
                for (String friend : requestList){
                    String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");
                    friends.getFriendManager().removeRequest(p.getName(), friend);
                    friends.getFriendManager().addFriend(name, p.getUniqueId().toString());
                    friends.getFriendManager().addFriend(p.getName(), friend);


                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu bist nun mit §e"+name+" §afreundet!"));
                    if (ProxyServer.getInstance().getPlayer(name) != null){
                        ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+" §ahat deine §aFreundschaftsanfrage §aangenommen!"));
                    }
                }

            } else if (args[0].equalsIgnoreCase("denyall")){

                List<String> requestList = friends.getFriendManager().getRequestList(p.getName());
                if (requestList.size() == 0){
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast zurzeit §ckeine §cFreundschaftsanfragen!"));
                    return;
                }
                for (String friend : requestList){
                    String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");
                    friends.getFriendManager().removeRequest(name, p.getUniqueId().toString());
                    friends.getFriendManager().removeRequest(p.getName(), friend);

                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast die Freundschaftsanfrage von §e"+name+" §cabgelehnt!"));
                    if (ProxyServer.getInstance().getPlayer(name) != null){
                        ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent(friends.getPrefix() + "§e"+p.getName()+" §chat deine §cFreundschaftsanfrage §cabgelehnt!"));
                    }
                }

            } else if (args[0].equalsIgnoreCase("jump")){
                if (args.length == 2){
                    String friendname = args[1];
                    ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(friendname);
                    if (friend != null){
                        if (friends.getFriendManager().getFriendListRAW(friend.getName()).contains(p.getUniqueId().toString())) {
                            if (friends.getFriendManager().getSetting(friend.getName(), "FJump")) {
                                p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu wirst nun zu dem Server von §e" + friend.getName() + " §averbunden!"));
                                p.connect(friend.getServer().getInfo());
                            } else {
                                p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst diesem Spieler §cnicht §cnachspringen!"));
                            }
                        }else {
                            p.sendMessage(new TextComponent(friends.getPrefix()+"§cDu bist nicht mit dem §cSpieler §cbefreundet!"));
                        }
                    }else {
                        p.sendMessage(new TextComponent(friends.getPrefix()+"§cDer Spieler existiert nicht §coder §cist §coffline!"));
                    }
                } else {
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friend jump <Spieler>"));
                }

            } else if (args[0].equalsIgnoreCase("togglejump")){
                boolean status = friends.getFriendManager().getSetting(p.getName(), "FJump");
                if (status) {
                    friends.getFriendManager().setSetting(p.getName(), "FJump","false");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cFreunde können nun nicht mehr zu §cdir §cspringen!"));
                } else {
                    friends.getFriendManager().setSetting(p.getName(), "FJump", "true");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aFreunden können nun wieder zu dir springen!"));
                }
            } else if (args[0].equalsIgnoreCase("toggleswitch")){
                boolean status = friends.getFriendManager().getSetting(p.getName(), "FSwitch");
                if (status) {
                    friends.getFriendManager().setSetting(p.getName(), "FSWitch","false");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu erählst nun keine Benachrichtigungen §cwenn ein §cFreund den §cServer §cwechselt!"));
                } else {
                    friends.getFriendManager().setSetting(p.getName(), "FSwitch", "true");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu erählst nun Benachrichtigungen wenn ein Freund §cden §cServer §cwechselt!"));
                }
            } else if (args[0].equalsIgnoreCase("togglerequest")){
                boolean status = friends.getFriendManager().getSetting(p.getName(), "FRequest");
                if (status) {
                    friends.getFriendManager().setSetting(p.getName(), "FRequest","false");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst nun keine §cFreundschaftsanfragen §cmehr §cbekommen!"));
                } else {
                    friends.getFriendManager().setSetting(p.getName(), "FRequest", "true");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu kannst nun Freundschaftsanfragen §abekommen!"));
                }
            } else if (args[0].equalsIgnoreCase("togglenotify")){
                boolean status = friends.getFriendManager().getSetting(p.getName(), "FOnline");
                if (status) {
                    friends.getFriendManager().setSetting(p.getName(), "FOnline","false");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§cDu wirst nun §cnicht §cmehr §cbenachrichtigt, §cwenn §cein §cFreund online kommt!"));
                } else {
                    friends.getFriendManager().setSetting(p.getName(), "FOnline", "true");
                    p.sendMessage(new TextComponent(friends.getPrefix() + "§aDu §awirst nun §awieder §abenachrichtigt, §awenn §aein §aFreund §aonline kommt!"));
                }
            } else if (args[0].equalsIgnoreCase("2")){
                sendHelp(p, 2);
            } else if (args[0].equalsIgnoreCase("help")){
                sendHelp(p, 1);
            } else if (args[0].equalsIgnoreCase("help 2")){
                sendHelp(p, 2);
            } else {
                sendHelp(p, 1);
            }
        }
    }

    private String getLastTimeOnline(long lastonline) {
        long current = System.currentTimeMillis();
        long lo = lastonline;

        long millis = current - lo;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        long months = 0;
        long years = 0;

        while (millis > 1000){
            millis -= 1000;
            seconds++;
        }

        while (seconds > 60){
            seconds -= 60;
            minutes++;
        }

        while (minutes > 60){
            minutes -= 60;
            hours++;
        }

        while (hours > 24){
            hours -= 24;
            days++;
        }

        while (days > 7){
            days -= 7;
            weeks++;
        }

        while (weeks > 4){
            weeks -= 4;
            months++;
        }

        while (months > 12) {
            months -= 12;
            years++;
        }

        if (years != 0){
            if (years == 1){
                return "§e" + 1 + " Jahr";
            } else {
                return "§e" + years + " Jahre";
            }
        } else if (months != 0){
            if (months == 1){
                return "§e" + 1 + " Monat";
            } else {
                return "§e" + months + " Monate";
            }
        } else if (weeks != 0){
            if (weeks == 1){
                return "§e" + 1 + " Woche";
            } else {
                return "§e" + weeks + " Wochen";
            }
        } else if (days != 0){
            if (days == 1){
                return "§e" + 1 + " Tag";
            } else {
                return "§e" + days + " Tage";
            }
        } else if (hours != 0){
            if (hours == 1){
                return "§e" + 1 + " Stunde";
            } else {
                return "§e" + hours + " Stunden";
            }
        } else if (minutes != 0){
            if (minutes == 1){
                return "§e" + 1 + " Minute";
            } else {
                return "§e" + minutes + " Minuten";
            }
        } else if (seconds != 0){
            if (seconds == 1){
                return "§e" + 1 + " Sekunde";
            } else {
                return "§e" + seconds + " Sekunden";
            }
        }else {
            return "§4ERROR: CALDATA. §cBitte kontaktiere einen Administrator!";
        }
    }

    public static void sendRequestConfirmation(ProxiedPlayer player, String rqfrom){
        TextComponent accept = new TextComponent("§2✔");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept " + rqfrom));
        TextComponent deny = new TextComponent("§4✘");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends deny " + rqfrom));
        TextComponent txt = new TextComponent(" §8|§r ");
        TextComponent msg = new TextComponent("§e"+rqfrom + "§8- ");
        msg.addExtra(accept);
        msg.addExtra(txt);
        msg.addExtra(deny);

        player.sendMessage(msg);
    }

    public static void sendConfirmation(ProxiedPlayer player, ProxiedPlayer from){
        TextComponent accept = new TextComponent("§8[§2Akzeptieren§8]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept " + from.getName()));
        TextComponent deny = new TextComponent("§8[§4Ablehnen§8]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends deny " + from.getName()));
        TextComponent txt = new TextComponent(" §8|§r ");
        TextComponent msg = new TextComponent("§cReqiuem §7• ");
        msg.addExtra(accept);
        msg.addExtra(txt);
        msg.addExtra(deny);

        player.sendMessage(msg);
    }

    public void sendHelp(ProxiedPlayer p, int page){
        if (page == 1) {
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends add <Spieler>"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends remove <Spieler>"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends accept <Spieler>"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends deny <Spieler>"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends jump <Spieler>"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§dSeite 1 von 2"));
        } else {
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends list"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends requests"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends clear"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends acceptall"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends denyall"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends togglerequest"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends togglejump"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends togglenotify"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§e/friends toggleswitch"));
            p.sendMessage(new TextComponent(friends.getPrefix() + "§dSeite 2 von 2"));
        }
    }
}
