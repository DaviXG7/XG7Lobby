package com.xg7network.xg7lobby.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {

    private String id;
    private String playername;
    private boolean muted;
    private List<Warn> infractions = new ArrayList<>();
    private Date lastDayToUnmute;
    private Date firstJoin;

    public String getId() {
        return id;
    }

    public PlayerData(Player player) {
        this.playername = player.getName();
        this.id = player.getUniqueId().toString();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playername);
    }

    public void setPlayer(Player player) {
        this.playername = player.getName();
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public List<Warn> getInfractions() {
        return infractions;
    }

    public void setInfractions(List<Warn> infractions) {
        this.infractions = infractions;
    }

    public void removeInfraction(int index) {
        this.infractions.remove(index - 1);
    }

    public void addInfraction(String reason, Date date) {
        this.infractions.add(new Warn(reason, date));
    }

    public Date getLastDayToUnmute() {
        return lastDayToUnmute;
    }

    public void setLastDayToUnmute(Date lastDayToUnmute) {
        this.lastDayToUnmute = lastDayToUnmute;
    }

    public Date getFirstJoin() {
        return firstJoin;
    }

    public boolean hasJoined() {
        return getFirstJoin() != null;
    }

    public void setFirstJoin(Date firstJoin) {
        this.firstJoin = firstJoin;
    }
}
