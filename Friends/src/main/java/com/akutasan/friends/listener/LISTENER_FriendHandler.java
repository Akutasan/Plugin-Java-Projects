package com.akutasan.friends.listener;

import com.akutasan.friends.Friends;
import com.akutasan.friends.commands.CMD_Friend;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class LISTENER_FriendHandler implements Listener {

    Friends friends;

    public LISTENER_FriendHandler(Friends friends){
        this.friends = friends;
    }


    @EventHandler
    public void onJoin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();



        System.out.println("Vor Register");
        friends.getFriendManager().registerPlayer(player);
        System.out.println("Nach Register");

        player.sendMessage(new TextComponent(friends.getPrefix() + "§7Es sind §e"+friends.getFriendManager().getFriends(player.getName())+ " §7deiner Freunde online!"));



        int requests = friends.getFriendManager().getRequests(player.getName());
        if (requests != 0){
            if (requests == 1){
                player.sendMessage(new TextComponent(friends.getPrefix() + "§7Du hast noch §eeine §7offene §7Freundschaftsanfrage!"));
            } else {
                player.sendMessage(new TextComponent(friends.getPrefix() + "§7Du hast noch §e"+requests+" §7offene §7Freundschaftsanfragen!"));
            }
        }

        friends.getMySQL().update("UPDATE friends_Users SET FcOnline='true' WHERE UUID='"+ player.getUniqueId().toString()+"';");
        friends.getMySQL().update("UPDATE friends_Users SET FServer='true' WHERE UUID='"+ player.getUniqueId().toString()+"';");



        List<String> list = friends.getFriendManager().getFriendList(player.getName());
        for (String friend : list){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));
            if (target != null){
                if (friends.getFriendManager().getSetting(target.getName(), "FOnline")){
                    target.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §7ist nun §aonline!"));
                }
            }
        }


    }

    @EventHandler
    public void onDisc(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        long millis = System.currentTimeMillis();

        friends.getMySQL().update("UPDATE friends_Users SET FConnect='"+millis+"' WHERE UUID='"+ player.getUniqueId().toString()+"'");
        friends.getMySQL().update("UPDATE friends_Users SET FcOnline='false' WHERE UUID='"+ player.getUniqueId().toString()+"'");

        List<String> friendlist = friends.getFriendManager().getFriendList(player.getName());
        for (String friend : friendlist){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));
            if (target != null){
                if (friends.getFriendManager().getSetting(target.getName(), "FOnline")){
                    target.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §7ist nun §coffline!"));
                }
            }
        }

    }

    @EventHandler
    public void onServerSwitch(ServerConnectedEvent event){

        ProxiedPlayer player = event.getPlayer();
        friends.getMySQL().update("UPDATE friends_Users SET FServer='lobby' WHERE UUID='"+ player.getUniqueId().toString()+"';");

        List<String> friendlist = friends.getFriendManager().getFriendList(player.getName());

        for (String friend : friendlist){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));

            if (target != null){
                if (friends.getFriendManager().getSetting(target.getName(), "FSwitch")){
                    target.sendMessage(new TextComponent(friends.getPrefix() + "§e" + event.getPlayer().getName() + " §7spielt nun auf §e"+event.getServer().getInfo().getName()+"§7!"));
                }
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event){
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (event.getCursor().startsWith("/friends add")){
            event.getSuggestions().clear();
            for (ProxiedPlayer target : ProxyServer.getInstance().getPlayers()){
                if (player != target && friends.getFriendManager().getSetting(target.getName(), "FRequest") && !friends.getFriendManager().getFriendListRAW(target.getName()).contains(player.getUniqueId().toString()) && !friends.getFriendManager().getRequestListRAW(target.getName()).contains(player.getUniqueId().toString())){
                    event.getSuggestions().add(player.getName());
                }
            }

        } else if (event.getCursor().startsWith("/friends remove")){
            event.getSuggestions().clear();
            for (String friend : friends.getFriendManager().getRequestList(player.getName())){
                event.getSuggestions().add(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));
            }
        } else if (event.getCursor().startsWith("/friends accept")){
            event.getSuggestions().clear();
            for (String friend : friends.getFriendManager().getRequestList(player.getName())){
                event.getSuggestions().add(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));
            }
        } else if (event.getCursor().startsWith("/friends deny")){
            event.getSuggestions().clear();
            for (String friend : friends.getFriendManager().getRequestList(player.getName())){
                event.getSuggestions().add(friends.getFriendManager().getNamebyUUID(friend, "friends_Users"));
            }
        } else if (event.getCursor().startsWith("/friends jump")){
            event.getSuggestions().clear();
            for (String friend : friends.getFriendManager().getRequestList(player.getName())){
                String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");
                if (ProxyServer.getInstance().getPlayer(name) != null) {
                    if (friends.getFriendManager().getSetting(name, "FJump")) {
                        event.getSuggestions().add(name);
                    }
                }
            }
        } else if(event.getCursor().startsWith("/friends")) {
            event.getSuggestions().clear();
            event.getSuggestions().add("add");
            event.getSuggestions().add("remove");
            event.getSuggestions().add("jump");
            event.getSuggestions().add("accept");
            event.getSuggestions().add("deny");
        }
    }


    @EventHandler
    public void onMessage(PluginMessageEvent event){
        if (event.getTag().equalsIgnoreCase("BungeeCord")){

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String channel = in.readUTF();
                if (channel.equalsIgnoreCase("friends")){
                    String input = in.readUTF();
                    String target = in.readUTF();
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
                    switch (input) {
                        case "add": {

                            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);
                            if (friend == null) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler ist momentan nicht online!"));
                                return;
                            }

                            if (friend == null) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler exestiert nicht oder ist offline!"));
                                return;
                            }
                            if (player.getName().equals(friend.getName()) || player == friend) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst dich nicht selber anfreunden!"));
                                return;
                            }
                            if (friends.getFriendManager().getRequestListRAW(player.getName()).contains(friend.getUniqueId().toString())) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast die Freundschaftsanfrage von §e" + friend.getName() + " §aangenommen!"));
                                friends.getFriendManager().removeRequest(player.getName(), friend.getUniqueId().toString());
                                friends.getFriendManager().addFriend(player.getName(), friend.getUniqueId().toString());
                                friends.getFriendManager().addFriend(friend.getName(), player.getUniqueId().toString());
                                if (friend != null) {
                                    friend.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + "§ahat deine Freundschaftsanfrage angenommen!"));
                                }
                                return;
                            }
                            if (friends.getFriendManager().getRequestListRAW(friend.getName()).contains(player.getUniqueId().toString())) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast §e" + friend + " §cschon eine §cFreundschaftsanfrage gesendet!"));
                                return;
                            }
                            if (!friends.getFriendManager().getSetting(friend.getName(), "FRequest")) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu kannst diesem Spieler keine Freundschaftsanfragen schicken!"));
                                return;
                            }
                            if (friends.getFriendManager().getFriends(friend.getName()) >= 100) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDer Spieler hat sein Freundschaftslimit erreicht!"));
                                return;
                            }
                            if (friends.getFriendManager().getFriendListRAW(friend.getName()).contains(player.getUniqueId().toString())) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu bist bereits mit diesem Spieler befreundet!"));
                                return;
                            }
                            player.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast " + friend.getName() + "§aeine Freundschaftsanfrage geschickt!"));
                            friend.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §ahat dir eine Freunschaftsanfrage geschickt!"));
                            CMD_Friend.sendConfirmation(friend, player);
                            friends.getFriendManager().addRequest(friend.getName(), player.getUniqueId().toString());

                            break;
                        }
                        case "remove":


                            if (friends.getFriendManager().existPlayerName(target)) {
                                String uuid = friends.getFriendManager().getUUIDbyName(target, "friends_Users");

                                if (friends.getFriendManager().getFriendListRAW(player.getName()).contains(uuid)) {

                                    friends.getFriendManager().removeFriend(target, player.getUniqueId().toString());
                                    friends.getFriendManager().removeFriend(player.getName(), uuid);

                                    player.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast §e" + target + " §aerfolgreich aus §adeiner §aFreundesliste §aentfernt!"));
                                    ProxiedPlayer f = ProxyServer.getInstance().getPlayer(target);
                                    if (f != null) {
                                        f.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §chat dich aus seiner Freundesliste entfernt!"));
                                    }
                                } else {
                                    player.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler ist §cexestiert nicht §coder ist nicht §cin §cdeiner §cFreundesliste!"));
                                }
                            } else {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDieser Spieler ist in nicht unserer Datenbank!"));
                            }
                            break;
                        case "accept": {
                            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);
                            String searchUUID;
                            if (friend != null) {
                                searchUUID = friend.getUniqueId().toString();
                            } else {
                                searchUUID = friends.getFriendManager().getUUIDbyName(target, "friends_Users");
                            }

                            if (friends.getFriendManager().getRequestListRAW(player.getName()).contains(searchUUID) && !searchUUID.isEmpty()) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§aDu hast die Freundschaftsanfrage von §e" + target + " §aangenommen!"));
                                friends.getFriendManager().removeRequest(player.getName(), searchUUID);
                                friends.getFriendManager().addFriend(player.getName(), searchUUID);
                                friends.getFriendManager().addFriend(target, player.getUniqueId().toString());
                                if (friend != null) {
                                    friend.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + "§ahat deine Freundschaftsanfrage angenommen!"));
                                }
                            } else {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast keine Anfragen von §e" + target));
                            }
                            break;
                        }
                        case "deny": {
                            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);
                            String searchUUID;
                            if (friend != null) {
                                searchUUID = friend.getUniqueId().toString();
                            } else {
                                searchUUID = friends.getFriendManager().getUUIDbyName(target, "friends_Users");
                            }

                            if (friends.getFriendManager().getRequestListRAW(player.getName()).contains(searchUUID) && !searchUUID.isEmpty()) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast die Freundschaftsanfrage von §e" + target + " §cabgelehnt!"));
                                friends.getFriendManager().removeRequest(player.getName(), searchUUID);
                                if (friend != null) {
                                    friend.sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + "§chat deine Freundschaftsanfrage abgelehnt!"));
                                }
                            } else {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast keine Anfragen von §e" + target));
                            }
                            break;
                        }
                        case "acceptall": {
                            List<String> requestList = friends.getFriendManager().getRequestList(player.getName());
                            if (requestList.size() == 0) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast zurzeit keine Freundschaftsanfragen!"));
                                return;
                            }
                            for (String friend : requestList) {
                                String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");
                                friends.getFriendManager().addFriend(name, player.getUniqueId().toString());
                                friends.getFriendManager().addFriend(player.getName(), friend);

                                player.sendMessage(new TextComponent(friends.getPrefix() + "§aDu bist nun mit §e" + name + " §afreundet!"));
                                if (ProxyServer.getInstance().getPlayer(name) != null) {
                                    ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §ahat deine Freundschaftsanfrage angenommen!"));
                                }
                            }
                            break;
                        }
                        case "denyall": {
                            List<String> requestList = friends.getFriendManager().getRequestList(player.getName());
                            if (requestList.size() == 0) {
                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast zurzeit keine Freundschaftsanfragen!"));
                                return;
                            }
                            for (String friend : requestList) {
                                String name = friends.getFriendManager().getNamebyUUID(friend, "friends_Users");
                                friends.getFriendManager().removeRequest(name, player.getUniqueId().toString());
                                friends.getFriendManager().removeRequest(player.getName(), friend);

                                player.sendMessage(new TextComponent(friends.getPrefix() + "§cDu hast die Freundschaftsanfrage von §e" + name + " §cabgelehnt!"));
                                if (ProxyServer.getInstance().getPlayer(name) != null) {
                                    ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent(friends.getPrefix() + "§e" + player.getName() + " §chat deine Freundschaftsanfrage abgelehnt!"));
                                }
                            }

                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
