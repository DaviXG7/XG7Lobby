package com.xg7network.xg7lobby.Player;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public class PlayerData {

    private Player player;
    private boolean muted;
    private int infractions;
    private List<String> warns;
    private Date lastDayToUnmute;

    public PlayerData(Player player, boolean muted, int infractions, List<String> warns, Date lastDayToUnmute) {

        this.player = player;
        this.muted = muted;
        this.infractions = infractions;
        this.warns = warns;
        this.lastDayToUnmute = lastDayToUnmute;


    }



}
