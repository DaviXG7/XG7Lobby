package com.xg7network.xg7lobby.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class PlayerData {

    private String id;
    private boolean playershide;
    private boolean muted;
    private List<Warn> infractions = new ArrayList<>();
    private long lastDayToUnmute;
    private long firstJoin;

    private String playername;

    public String getId() {
        return id;
    }

    public PlayerData(String id, boolean playershide, boolean muted, List<Warn> infractions, long lastDayToUnmute, long firstJoin) {
        this.id = id;
        this.playershide = playershide;
        this.muted = muted;
        this.infractions = infractions;
        this.lastDayToUnmute = lastDayToUnmute;
        this.firstJoin = firstJoin;
        this.playername = Bukkit.getOfflinePlayer(UUID.fromString(id)).getName();
    }

    public PlayerData(Player player) {
        this.playername = player.getName();
        this.id = player.getUniqueId().toString();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(id));
    }

    public String getName() {
        return this.playername;
    }

    public void setPlayer(Player player) {
        this.playername = player.getName();
    }

    public boolean removeInfraction(String id) {
        for (Warn warn : this.infractions) {
            if (warn.getId().equals(id)) {
                this.infractions.remove(warn);

                return PlayersManager.deleteWarn(this.id, warn.getId());
            }
        }
        return false;
    }

    public void addInfraction(String reason, long date) {
        this.infractions.add(new Warn(id, reason, date));
    }

    public String getFirstJoin() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(firstJoin);
    }

    public long getFirstJoinLong() {
        return firstJoin;
    }
}
