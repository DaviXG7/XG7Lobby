package com.xg7network.xg7lobby.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerData {

    private String id;
    private boolean playershide;
    private String playername;
    private boolean muted;
    private List<Warn> infractions = new ArrayList<>();
    private long lastDayToUnmute;
    private long firstJoin;

    public String getId() {
        return id;
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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public List<Warn> getInfractions() {
        return infractions;
    }

    public boolean isPlayershide() {
        return playershide;
    }

    public void setPlayershide(boolean playershide) {
        this.playershide = playershide;
    }

    public void setInfractions(List<Warn> infractions) {
        this.infractions = infractions;
    }

    public void removeInfraction(int index) {
        this.infractions.remove(index - 1);
    }

    public void addInfraction(String reason, long date) {
        this.infractions.add(new Warn(reason, date));
    }

    public long getLastDayToUnmute() {
        return lastDayToUnmute;
    }

    public void setLastDayToUnmute(long lastDayToUnmute) {
        this.lastDayToUnmute = lastDayToUnmute;
    }

    public String getFirstJoin() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(firstJoin);
    }
    public void setFirstJoin(long firstJoin) {
        this.firstJoin = firstJoin;
    }
}
