package com.akutasan.partyplugin.manager;

import com.akutasan.partyplugin.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class PlayerParty
{
    private ProxiedPlayer leader;
    private List<ProxiedPlayer> invitations;
    private List<ProxiedPlayer> players;

    public PlayerParty(ProxiedPlayer leader)
    {
        this.leader = leader;
        this.players = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

    public boolean isLeader(ProxiedPlayer p) {
        return this.leader == p;
    }

    public List<ProxiedPlayer> getPlayers() {
        return this.players;
    }

    public ProxiedPlayer getLeader()
    {
        return this.leader;
    }

    public List<ProxiedPlayer> getInvitations()
    {
        return this.invitations;
    }

    public boolean hasRequest(ProxiedPlayer p)
    {
        return this.invitations.contains(p);
    }

    public boolean isInParty(ProxiedPlayer p)
    {
        if (isLeader(p)) {
            return true;
        }
        return this.players.contains(p);
    }

    public boolean addPlayer(ProxiedPlayer p)
    {
        if ((!this.players.contains(p)) && (this.invitations.contains(p)))
        {
            this.players.add(p);
            this.invitations.remove(p);
            return true;
        }
        return false;
    }

    public boolean removePlayer(ProxiedPlayer p) {
        if (this.players.contains(p)) {
            this.players.remove(p);
            return true;
        } else {
            return false;
        }
    }

    public void removeInvite(ProxiedPlayer p)
    {
        this.invitations.remove(p);
    }

    public ServerInfo getServer()
    {
        return this.leader.getServer().getInfo();
    }

    public void invite(final ProxiedPlayer p)
    {
        this.invitations.add(p);
        p.sendMessage(new TextComponent(Main.partyprefix + "§a" + getLeader().getName() + " §ahat §edich in §aseine §aParty §aeingeladen!"));
        TextComponent accept = new TextComponent(Main.partyprefix + "§7Klicke §e§lhier §7um §a§lanzunehmen!");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + getLeader().getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/party accept §6" + getLeader().getName()).create()));
        TextComponent deny = new TextComponent(Main.partyprefix + "§7Klicke §e§lhier §7um §c§labzulehnen!");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + getLeader().getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c/party deny §6"+getLeader().getName()).create()));
        p.sendMessage(accept);
        p.sendMessage(deny);
        BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                if (PlayerParty.this.hasRequest(p))
                {
                    PlayerParty.this.invitations.remove(p);
                    p.sendMessage(new TextComponent(Main.partyprefix + "§cDeine Einladung §cist §cabgelaufen"));
                    PlayerParty.this.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§cDie Einladung zu §e" + p.getName() + " §cist abgelaufen"));
                }
                PlayerParty.this.start(p);
            }
        }, 5L, TimeUnit.MINUTES);
    }

    private void start(final ProxiedPlayer p)
    {
        BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                PlayerParty party = PartyManager.getParty(p);
                if ((party != null) && (party.getPlayers().size() == 0))
                {
                    PartyManager.deleteParty(p);
                    party.getLeader().sendMessage(new TextComponent(Main.partyprefix + "§cDie Party §cwird wegen zu §cwenig §cMitgliedern §caufgelöst"));
                    p.sendMessage(new TextComponent(Main.partyprefix + "§cDie Party §cwurde §caufgelöst"));
                }
            }
        }, 2L, TimeUnit.MINUTES);
    }

    public static String getRankName(ProxiedPlayer player){
        IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        assert user != null;
        return CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user).getName();
    }

    public static String getRankCol(ProxiedPlayer player) {
        switch (getRankName(player)) {
            case "Owner":
                return "§4";
            case "Admin":
            case "SrDeveloper":
            case "SrModerator":
            case "SrBuilder":
                return "§c";
            case "Developer":
                return "§b";
            case "Moderator":
                return "§9";
            case "Supporter":
                return "§3";
            case "Builder":
                return "§e";
            case "Social":
                return "§5";
            case "MVP":
                return "§d";
            case "VIP":
                return "§a";
            case "Premium":
                return "§6";
            default:
                return "§7";
        }
    }
}
