package com.akutasan.friends.manager;

import com.akutasan.friends.Friends;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FriendManager {
    Friends friends;

    public FriendManager(Friends friends){
        this.friends = friends;
    }

    public void createTables(){
        friends.getMySQL().update("CREATE TABLE IF NOT EXISTS friends_Users(Name VARCHAR(16), UUID VARCHAR(64)" +
                ", FList VARCHAR(20000), FRequests VARCHAR(20000), FRequest VARCHAR(10), FJump VARCHAR(10), FOnline VARCHAR(10), FSwitch VARCHAR(10)" +
                ", FConnect BIGINT, FcOnline VARCHAR(10), FServer VARCHAR(50));");
    }

    public void registerPlayer(ProxiedPlayer player){
        if (!existPlayer(player.getUniqueId().toString())){
            friends.getMySQL().update("INSERT INTO friends_Users(Name, UUID, FList, FRequests, FRequest, FJump, FOnline, FSwitch, FConnect, FcOnline, FServer) VALUES" +
                    "('"+player.getName() + "','" + player.getUniqueId().toString() + "','','','true','true','true','true','"+System.currentTimeMillis()+"','true','lobby');");
        } else {
            friends.getMySQL().update("UPDATE friends_Users SET NAME='"+ player.getName()+"' WHERE UUID='"+player.getUniqueId().toString()+ "';");
        }
    }

    //Friends

    public String getFriendListRAW(String name){
        return String.valueOf(get(name, "Name", "FList", "friends_Users"));
    }

    public List<String> getFriendList(String name){
        String friendList = getFriendListRAW(name);
        List<String> toreturn = new ArrayList<>();
        if (friendList.isEmpty())
            return toreturn;
        String[] friends = friendList.split(";");
        toreturn.addAll(Arrays.asList(friends));
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
        toreturn.addAll(Arrays.asList(friends));
        return toreturn;
    }

    public int getRequests(String name){
        String requestlist = getRequestListRAW(name);
        if (requestlist.isEmpty())
            return 0;
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

        friends.getMySQL().update("UPDATE friends_Users SET FRequests='"+reqlist+"' WHERE NAME='"+name+"'");
    }

    public void removeFriend(String name, String friend){
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§cremoveFriend"));
        String friendlist = getFriendListRAW(name);
        friendlist = friendlist.replace(friend + ";", "");

        friends.getMySQL().update("UPDATE friends_Users SET FList='" + friendlist + "' WHERE NAME='"+name+"'");
    }

    public void removeRequest(String name, String request){
        String reqlist = getFriendListRAW(name);
        reqlist = reqlist.replace(request + ";", "");

        friends.getMySQL().update("UPDATE friends_Users SET FRequests='" + reqlist + "' WHERE NAME='"+name+"'");
    }

    //Friend list

    public HashMap<String, List<String>> getList(String name){
        List<String> friendlist = getFriendList(name);
        List<String> fl = new ArrayList<>();

        for (String uuid : friendlist){
            fl.add(getNamebyUUID(uuid, "friends_Users"));
        }

        List<String> offline = new ArrayList<>();
        List<String> online = new ArrayList<>();

        for (String entry : fl){
            if (ProxyServer.getInstance().getPlayer(entry) != null){
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

    //settings

    public boolean getSetting(String name, String type){
        return Boolean.parseBoolean(String.valueOf(get(name, "Name", type, "friends_Users")));
    }

    public void setSetting(String name, String type, String value){
        friends.getMySQL().update("UPDATE friends_Users SET "+ type + "='" + value + "' WHERE NAME='"+name+"';");
    }

    /* Methods to Check */

    public String getUUIDbyName(String playername, String database) {
        String i = "";
        try {
            ResultSet rs = friends.getMySQL()
                    .getResult("SELECT * FROM " + database + " WHERE Name= '" + playername + "'");

            if ((rs.next())) {
                rs.getString("UUID");
            }

            i = rs.getString("UUID");

        } catch (SQLException ignored) {

        }
        return i;
    }

    public String getNamebyUUID(String playername, String database) {
        String i = "";
        try {
            ResultSet rs = friends.getMySQL()
                    .getResult("SELECT * FROM " + database + " WHERE UUID= '" + playername + "'");

            if ((rs.next())) {
                rs.getString("Name");
            }

            i = rs.getString("Name");

        } catch (SQLException ignored) {

        }
        return i;
    }

    public boolean existPlayer(String uuid) {
        try {
            ResultSet rs = friends.getMySQL().getResult("SELECT * FROM friends_Users WHERE UUID='" + uuid + "'");

            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean existPlayerName(String name) {
        try {
            ResultSet rs = friends.getMySQL().getResult("SELECT * FROM friends_Users WHERE Name= '" + name + "'");

            if (rs.next()) {
                return rs.getString("Name") != null;
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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

        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§cCould not load friends.mysql-Stats Type."));
        return "ERROR";
    }

}