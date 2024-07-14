package com.xg7plugins.xg7lobby.data.player.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private long firstJoin;
    private long timeForUnmute;
    private UUID id;
    private boolean isPlayerHiding;
    private boolean isMuted;
    private boolean isBuildEnabled;
    private boolean isFlying;
    private boolean isPVPEnabled;
    private List<Warn> infractions;

    public PlayerData(UUID id, boolean isMuted, boolean isPlayerHiding, boolean isBuildEnabled, boolean isFlying, boolean isPVPEnabled, long firstJoin, long timeForUnmute) {
        this.id = id;
        this.isMuted = isMuted;
        this.isFlying = isFlying;
        this.isBuildEnabled = isBuildEnabled;
        this.isPVPEnabled = isPVPEnabled;
        this.isPlayerHiding = isPlayerHiding;
        this.timeForUnmute = timeForUnmute;
        this.firstJoin = firstJoin;
    }
    public PlayerData(UUID id) {
        this.id = id;
        this.isPlayerHiding = false;
        this.isMuted = false;
        this.firstJoin = 0;
        this.timeForUnmute = 0;
        this.infractions = new ArrayList<>();
    }

}
