package com.akutasan.friendsgui.manager;

import com.akutasan.friendsgui.FriendsGUI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Methods {

    private HashMap<String, Integer> pageindex = new HashMap<>();
    FriendsGUI friends;

    public Methods(FriendsGUI friends){
        this.friends = friends;
    }

    public void loadFriendInventory(Player player, int page){


        List<String> friendlist;
        if (page == 1) {
            friendlist = loadFriends(player, 0, 36);
        } else {
            friendlist = loadFriends(player, ((page -1) * 36) + 1, (page*36));
        }

        pageindex.put(player.getName(), page);

        Inventory inventory = Bukkit.createInventory(null, 54, "§dFreundesliste");

        for (int fs = 0; fs < 36; fs++) {

            if (fs >= friendlist.size()) {
                inventory.setItem(fs, new ItemStack(Material.AIR));
            } else {
                String friend = friendlist.get(fs);
                ItemStack item = this.friends.getApi().createHead(friend, null, "§d" + friend);
                ItemMeta itemMeta = item.getItemMeta();
                ArrayList<String> lore = new ArrayList<>(fs);
                if (this.friends.getMethods().getSetting(friend, "FcOnline")) {

                    lore.add("§7Online auf §e" + get(friend, "Name", "FServer", "friends_Users"));
                    itemMeta.setDisplayName("§d"+friend);
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    inventory.setItem(fs, item);

                } else {
                    ItemStack offlineP = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
                    lore.add("§7Zul. Online: §e"+getLastTimeOnline((long) get(friend, "Name", "FConnect", "friends_Users")));
                    itemMeta.setDisplayName("§d"+friend);
                    itemMeta.setLore(lore);
                    offlineP.setItemMeta(itemMeta);
                    inventory.setItem(fs, offlineP);

                }

            }
        }

        //inventory
        inventory.setItem(38, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(39, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(41, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(42, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(36, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(37, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(40, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(43, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(44, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));

        //Navigation

        if (page != 1){
            inventory.setItem(45, friends.getApi().CreateItemwithMaterial(Material.ARROW, 0, 1, "§7◀ Zurück", null));
        } else {
            inventory.setItem(45, friends.getApi().CreateItemwithMaterial(Material.BARRIER, 0, 1, "§c✘", null));
        }
        inventory.setItem(46, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(47, friends.getApi().CreateItemwithMaterial(Material.COMMAND, 0, 1, "§7Einstellungen", null));
        inventory.setItem(48, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));

        ArrayList<String> lore = new ArrayList<>();

        int size = getFriends(player.getName());
        if (size == 1){
            lore.add("§7Du hast §eeinen §7Freund");
        } else {
            lore.add("§7Du hast §e"+ size +" §7Freund");
        }

        inventory.setItem(49, friends.getApi().createHead(player.getName(), lore, "§d" + player.getName()));
        inventory.setItem(50, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(51, friends.getApi().CreateItemwithMaterial(Material.BOOK_AND_QUILL, 0, 1, "§7Anfragen (§e"+getRequests(player.getName()) + "§7)", null));
        inventory.setItem(52, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(53, friends.getApi().CreateItemwithMaterial(Material.ARROW, 0, 1, "§7▶ Vor", null));


        player.openInventory(inventory);
    }

    public List<String> loadFriends(Player player, int from, int to){

        HashMap<String, List<String>> f = getList(player.getName());
        List<String> friendlist = combine(f.get("online"), f.get("offline"));

        List<String> toreturn = new ArrayList<>();

        for (int i = from; i < to; i++){
            if (friendlist.size() > i){
                toreturn.add(friendlist.get(i));
            }
        }

        return toreturn;
    }

    public void loadOptions(Player player, String target){
        Inventory inventory = Bukkit.createInventory(null, 18, "§d" + target);

        inventory.setItem(0, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));

        if (Bukkit.getPlayer(target) != null) {
            inventory.setItem(2, friends.getApi().CreateItemwithMaterial(Material.CAKE, 0, 1, "§7In eine §5Party §7einladen", null));
            inventory.setItem(4, friends.getApi().CreateItemwithMaterial(Material.RABBIT_FOOT, 0, 1, "§7Zum §2Freund sprigen", null));
            inventory.setItem(6, friends.getApi().CreateItemwithMaterial(Material.FIREBALL, 0, 1, "§7Freundschaft §cauflösen", null));
        } else {
            inventory.setItem(4, friends.getApi().CreateItemwithMaterial(Material.FIREBALL, 0, 1, "§7Freundschaft §cauflösen", null));
        }
        inventory.setItem(8, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(9, friends.getApi().createHead(target, null, "§d" + target));
        inventory.setItem(10, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(11, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(12, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(13, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(14, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(15, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(16, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(17, friends.getApi().CreateItemwithMaterial(Material.ARROW, 0, 1, "§7◀ Zurück", null));

        player.openInventory(inventory);
    }

    public void loadRequestInventory(Player player){

        Inventory inventory = Bukkit.createInventory(null, 54, "§dAnfragen §8["+getRequests(player.getName()) + "§8]");

        for (String request : getRequestList(player.getName())){
            String name = getNamebyUUID(request, "friends_Users");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7Was möchtest du machen?");
            lore.add("§8-§7Anfrage §aannehmen §7(Linksklick)");
            lore.add("§8-§7Anfrage §cablehnen §7(Rechtsklick)");
            inventory.addItem(friends.getApi().createHead(name, lore, "§9" + name));
        }

        inventory.setItem(38, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(39, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(41, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(42, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(36, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(37, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(40, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(43, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(44, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));

        inventory.setItem(46, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(48, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(45, friends.getApi().CreateItemwithMaterial(Material.BARRIER, 1, 1, "§c✘", null));
        inventory.setItem(47, friends.getApi().CreateItemwithID(351, 1, 1, "§cAlle ablehnen", null));

        ArrayList<String> lore = new ArrayList<>();
        int size = getRequests(player.getName());
        if (size == 1){
            lore.add("§7Du hast §eeine §7offene §7Freundschaftsanfrage");
        } else {
            lore.add("§7Du hast §e"+size + " §7offene Freundschaftsanfragen");
        }

        inventory.setItem(49, friends.getApi().createHead(player.getName(), lore, "§d" + player.getName()));

        inventory.setItem(50, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(51, friends.getApi().CreateItemwithID(351, 10, 1, "§aAlle annehmen", null));
        inventory.setItem(52, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 8, 1, "§3", null));
        inventory.setItem(53, friends.getApi().CreateItemwithMaterial(Material.ARROW, 0, 1, "§7◀ Zurück", null));

        player.openInventory(inventory);

    }

    public void loadSettingsInventory(Player player){

        Inventory inventory  = Bukkit.createInventory(null, 27, "§dEinstellungen");

        inventory.setItem(0, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));

        ArrayList<String> lr = new ArrayList<>();
        lr.add("§7Stelle ein, ob du Freundschaftsanfragen");
        lr.add("§7erhalten möchtest");
        ItemStack request = friends.getApi().CreateItemwithMaterial(Material.BOOK_AND_QUILL, 0, 1, "§dFreundschaftsanfragen", null);
        inventory.setItem(2, request);

        ArrayList<String> lj = new ArrayList<>();
        lj.add("§7Stelle ein, ob deine Freunde zu dir sprigen können");
        ItemStack jump = friends.getApi().CreateItemwithMaterial(Material.RABBIT_FOOT, 0, 1, "§dZu dir springen", null);
        inventory.setItem(3, jump);

        ArrayList<String> ln = new ArrayList<>();
        ln.add("§7Stelle ein, ob du Online/Offline Nachrichten");
        ln.add("§7erhalten möchtest");
        ItemStack online = friends.getApi().CreateItemwithMaterial(Material.PAPER, 0, 1, "§dOnline/Offline Nachrichten", null);
        inventory.setItem(5, online);

        ArrayList<String> ls = new ArrayList<>();
        ls.add("§7Stelle ein, ob du Server-Wechsel Nachrichten erhalten möchtest");
        ItemStack servers = friends.getApi().CreateItemwithMaterial(Material.GLASS_BOTTLE, 0, 1, "§dServer-Wechsel Nachrichten", null);
        inventory.setItem(6, servers);

        inventory.setItem(8, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(9, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));

        boolean rq = getSetting(player.getName(), "FRequest");
        boolean jmp = getSetting(player.getName(), "FJump");
        boolean on = getSetting(player.getName(), "FOnline");
        boolean swtch = getSetting(player.getName(), "FSwitch");

        inventory.setItem(11, friends.getApi().CreateItemwithID(351, (rq ? 10 :1), 1,(rq ? "§a✔" : "§c✘"), lr));
        inventory.setItem(12, friends.getApi().CreateItemwithID(351, (jmp ? 10 :1), 1,(jmp ? "§a✔" : "§c✘"), lj));
        inventory.setItem(14, friends.getApi().CreateItemwithID(351, (on ? 10 :1), 1,(on ? "§a✔" : "§c✘"), ln));
        inventory.setItem(15, friends.getApi().CreateItemwithID(351, (swtch ? 10 :1), 1,(swtch ? "§a✔" : "§c✘"), ls));

        inventory.setItem(17, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(18, friends.getApi().createHead(player.getName(), null, "§d" + player.getName() + "'s Einstellungen"));
        inventory.setItem(19, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(20, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(21, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(22, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(23, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(24, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(25, friends.getApi().CreateItemwithMaterial(Material.STAINED_GLASS_PANE, 15, 1, "§3", null));
        inventory.setItem(26, friends.getApi().CreateItemwithMaterial(Material.ARROW, 0, 1, "§7◀ Zurück", null));

        player.openInventory(inventory);
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

    public ArrayList<String> combine(List<String> first, List<String> second){

        ArrayList<String> toreturn = new ArrayList<>();

        for (String entry : first){
            toreturn.add(entry);
        }
        for (String entry : second){
            toreturn.add(entry);
        }
        return toreturn;
    }

    public int getPage(Player player){
        return pageindex.get(player.getName());
    }

    //Settings
    public boolean getSetting(String name, String type){
        return Boolean.parseBoolean(String.valueOf(get(name, "Name", type, "friends_Users")));
    }

    public void setSetting(String name, String type, String value){
        friends.getMySQL().update("UPDATE friends_Users SET "+ type + "='" + value + "' WHERE NAME='"+name+"';");
    }

    //Friends

    public String getFriendListRAW(String name){
        return String.valueOf(get(name, "Name", "Flist", "friends_Users"));
    }

    public List<String> getFriendList(String name){
        String friendList = getFriendListRAW(name);
        List<String> toreturn = new ArrayList<>();
        if (friendList.isEmpty())
            return toreturn;
        String[] friends = friendList.split(";");
        for (int i = 0; i < friends.length; i++){
            toreturn.add(friends[i]);
        }
        return toreturn;
    }

    public int getFriends(String name){
        String friendList = getFriendListRAW(name);
        if (friendList.isEmpty())
            return 0;
        String[] friends = friendList.split(";");
        return friends.length;
    }

    //Request

    public String getRequestListRAW(String name){
        return String.valueOf(get(name, "Name", "FRequests", "friends_Users"));
    }

    public List<String> getRequestList(String name){
        String requestlist = getRequestListRAW(name);
        List<String> toreturn = new ArrayList<>();
        if (requestlist.isEmpty())
            return toreturn;
        String[] friends = requestlist.split(";");
        for (int i = 0; i < friends.length; i++){
            toreturn.add(friends[i]);
        }
        return toreturn;
    }

    public int getRequests(String name){
        String requestlist = getRequestListRAW(name);
        if (requestlist.isEmpty()){
            return 0;
            }
        String[] req = requestlist.split(";");
        return req.length;
    }

    //Friend Methods

    public void addFriend(String name, String newfriend){
        String friendlist = getFriendListRAW(name);
        friendlist = friendlist + newfriend + ";";

        friends.getMySQL().update("UPDATE friends_Users SET FList='"+friendlist+"' WHERE NAME='"+name+"'");
    }

    public void addRequest(String name, String request){
        String reqlist = getRequestListRAW(name);
        reqlist = reqlist + request + ";";

        friends.getMySQL().update("UPDATE friends_Users SET FRequests='"+reqlist+"' WHERE NAME '"+name+"'");
    }

    public void removeFriend(String name, String friend){
        String friendlist = getFriendListRAW(name);
        friendlist = friendlist.replace(friend + ";", "");

        friends.getMySQL().update("UPDATE friends_Users SET FRequests='" + friendlist + "' WHERE NAME='"+name+"'");
    }

    public void removeRequest(String name, String request){
        String reqlist = getFriendListRAW(name);
        reqlist = reqlist.replace(request + ";", "");

        friends.getMySQL().update("UPDATE friends_Users SET FRequests='" + reqlist + "' WHERE NAME='"+name+"'");
    }

    public HashMap<String, List<String>> getList(String name){
        List<String> friendlist = getFriendList(name);
        List<String> fl = new ArrayList<>();

        for (String uuid : friendlist){
            fl.add(getNamebyUUID(uuid, "friends_Users"));
        }

        List<String> offline = new ArrayList<>();
        List<String> online = new ArrayList<>();

        for (String entry : fl){
            if (Bukkit.getPlayer(entry) != null){
                online.add(entry);
            } else {
                offline.add(entry);
            }
        }

        Collections.sort(offline);
        Collections.sort(online);

        HashMap<String, List<String>> hash = new HashMap<>();
        hash.put("offline", offline);
        hash.put("online", online);

        return hash;
    }

    public String getUUIDbyName(String playername, String database) {
        String i = "";
        try {
            ResultSet rs = friends.getMySQL()
                    .getResult("SELECT * FROM " + database + " WHERE Name= '" + playername + "'");

            if ((!rs.next()) || (String.valueOf(rs.getString("UUID")) == null))
                ;

            i = rs.getString("UUID");

        } catch (SQLException e) {

        }
        return i;
    }

    public String getNamebyUUID(String playername, String database) {
        String i = "";
        try {
            ResultSet rs = friends.getMySQL()
                    .getResult("SELECT * FROM " + database + " WHERE UUID= '" + playername + "'");

            if ((!rs.next()) || (String.valueOf(rs.getString("Name")) == null))
                ;

            i = rs.getString("Name");

        } catch (SQLException e) {

        }
        return i;
    }

    public Object get(String whereresult, String where, String select, String database) {

        ResultSet rs = friends.getMySQL()
                .getResult("SELECT " + select + " FROM " + database + " WHERE " + where + "='" + whereresult + "'");
        try {
            if(rs.next()) {
                Object v = rs.getObject(select);
                return v;
            }
        } catch (SQLException e) {
            return "ERROR";
        }

        System.out.println(new TextComponent("§cCould not load friends.mysql-Stats Type."));
        return "ERROR";
    }

    private static String getRankCol(Player player) {
        if (player.hasPermission("Score.Admin")) {
            return "§4";
        } else if (player.hasPermission("Score.SrDeveloper")) {
            return "§c";
        } else if (player.hasPermission("Score.Developer")) {
            return "§b";
        } else if (player.hasPermission("Score.SrModerator")) {
            return "§c";
        } else if (player.hasPermission("Score.Moderator")) {
            return "§9";
        } else if (player.hasPermission("Score.SrBuilder")) {
            return "§c";
        } else if (player.hasPermission("Score.Supporter")) {
            return "§3";
        } else if (player.hasPermission("Score.Builder")) {
            return "§e";
        }  else if (player.hasPermission("Score.Social")) {
            return "§5";
        } else if (player.hasPermission("Score.MVP")) {
            return "§d";
        } else if (player.hasPermission("Score.VIP")) {
            return "§a";
        } else if (player.hasPermission("Score.Premium")) {
            return "§6";
        } else if (player.hasPermission("Score.Spieler")){
            return "§7";
        } else {
            return "§7";
        }
    }
}
