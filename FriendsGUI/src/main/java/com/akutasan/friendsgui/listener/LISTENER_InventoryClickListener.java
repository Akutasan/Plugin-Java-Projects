package com.akutasan.friendsgui.listener;

import com.akutasan.friendsgui.FriendsGUI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class LISTENER_InventoryClickListener implements Listener {

    FriendsGUI friends;

    public LISTENER_InventoryClickListener(FriendsGUI friends){
        this.friends = friends;
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getClickedInventory() == null){
            return;
        }

        if (event.getInventory().getName().equals("§dFreundesliste")){
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.ARROW){
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7▶ Vor")){
                    friends.getMethods().loadFriendInventory(player, friends.getMethods().getPage(player) + 1);
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7◀ Zurück")){
                    friends.getMethods().loadFriendInventory(player, friends.getMethods().getPage(player) - 1);
                }
            } else if (event.getCurrentItem().getType() == Material.COMMAND){
//                player.sendMessage(friends.getPrefix() + "§cDieses Feature ist noch in Entwicklung!");
                friends.getMethods().loadSettingsInventory(player);
            } else if (event.getCurrentItem().getType() == Material.BOOK_AND_QUILL){
                friends.getMethods().loadRequestInventory(player);
            } else if (event.getCurrentItem().getType() == Material.SKULL_ITEM){
                String target = event.getCurrentItem().getItemMeta().getDisplayName();
                target = target.replace("§d", "");
                if (target.equals(player.getName())){
                    return;
                }

                friends.getMethods().loadOptions(player, target);
            }
        } else if(event.getInventory().getName().startsWith("§dEinstellungen")){
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == Material.ARROW){
                friends.getMethods().loadFriendInventory(player, 1);
                return;
            }
            if (event.getCurrentItem().getItemMeta().hasLore()) {
                if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("§7Stelle ein, ob du Freundschaftsanfragen")) {

                    boolean status = friends.getMethods().getSetting(player.getName(), "FRequest");

                    if (status) {
                        friends.getMethods().setSetting(player.getName(), "FRequest", "false");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 1, 1, "§c✘", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    } else {
                        friends.getMethods().setSetting(player.getName(), "FRequest", "true");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 10, 1, "§a✔", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    }

                } else if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("§7Stelle ein, ob deine Freunde")) {

                    boolean status = friends.getMethods().getSetting(player.getName(), "FJump");

                    if (status) {
                        friends.getMethods().setSetting(player.getName(), "FJump", "false");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 1, 1, "§c✘", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    } else {
                        friends.getMethods().setSetting(player.getName(), "FJump", "true");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 10, 1, "§a✔", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    }

                } else if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("§7Stelle ein, ob du Online")) {

                    boolean status = friends.getMethods().getSetting(player.getName(), "FOnline");

                    if (status) {
                        friends.getMethods().setSetting(player.getName(), "FOnline", "false");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 1, 1, "§c✘", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    } else {
                        friends.getMethods().setSetting(player.getName(), "FOnline", "true");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 10, 1, "§a✔", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    }

                } else if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("§7Stelle ein, ob du Server-Wechsel")) {

                    boolean status = friends.getMethods().getSetting(player.getName(), "FSwitch");

                    if (status) {
                        friends.getMethods().setSetting(player.getName(), "FSwitch", "false");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 1, 1, "§c✘", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    } else {
                        friends.getMethods().setSetting(player.getName(), "FSwitch", "true");
                        event.getInventory().setItem(event.getSlot(), friends.getApi().CreateItemwithID(351, 10, 1, "§a✔", (ArrayList<String>) event.getCurrentItem().getItemMeta().getLore()));
                    }

                }
            }
        } else if (event.getInventory().getName().startsWith("§dAnfragen")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                String target = event.getCurrentItem().getItemMeta().getDisplayName();
                target = target.replace("§d", "");

                if (event.getClick().equals(ClickType.LEFT)) {
                    event.getInventory().removeItem(event.getCurrentItem());
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("friends");
                    out.writeUTF("accept");
                    out.writeUTF(target);
                    player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                } else if (event.getClick().equals(ClickType.RIGHT)) {
                    event.getInventory().removeItem(event.getCurrentItem());
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("friends");
                    out.writeUTF("deny");
                    out.writeUTF(target);
                    player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                }

            } else if (event.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                friends.getMethods().loadRequestInventory(player);
            } else if (event.getCurrentItem().getTypeId() == 351){
                if (event.getCurrentItem().getData().getData() == 10){
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("friends");
                    out.writeUTF("acceptall");
                    out.writeUTF("");
                    player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                } else if (event.getCurrentItem().getData().getData() == 1){
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("friends");
                    out.writeUTF("denyall");
                    out.writeUTF("");
                    player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                }
            } else if (event.getCurrentItem().getType() == Material.ARROW){
                friends.getMethods().loadFriendInventory(player, friends.getMethods().getPage(player));
            }

        }else if (event.getInventory().getName().startsWith("§d")){
            event.setCancelled(true);
            String target =event.getInventory().getName();
            target = target.replace("§d", "");
            if (event.getCurrentItem().getType() == Material.RABBIT_FOOT){
                if (friends.getMethods().getSetting(target, "FcOnline")) {
                    boolean status = friends.getMethods().getSetting(player.getName(), "FJump");
                    if (status){

                        String server = (String) friends.getMethods().get(target, "Name", "FServer", "friends_Users");

                        friends.getApi().connect(player, server);
                        player.sendMessage(friends.getPrefix() + "§7Du wirst mit §e"+server+" §7verbunden!");

                    } else {
                        player.sendMessage(friends.getPrefix() + "§cDu kannst dem Spieler nicht nachsprigen!");
                    }
                } else {
                    player.sendMessage(friends.getPrefix() + "§cDieser Spieler ist nicht online!");
                }
            } else if (event.getCurrentItem().getType() == Material.CAKE){
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("party");
                out.writeUTF("invite");
                out.writeUTF(target);
                player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                player.closeInventory();
            } else if (event.getCurrentItem().getType() == Material.FIREBALL){
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("friends");
                out.writeUTF("remove");
                out.writeUTF(target);
                player.sendPluginMessage(friends, "BungeeCord", out.toByteArray());
                player.closeInventory();
            } else if (event.getCurrentItem().getType() == Material.ARROW){
                friends.getMethods().loadFriendInventory(player, 1);
            }
        }
    }
    }
