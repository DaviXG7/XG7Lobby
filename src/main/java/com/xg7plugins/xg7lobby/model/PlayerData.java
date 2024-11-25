package com.xg7plugins.xg7lobby.model;


import com.xg7plugins.data.database.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class PlayerData implements Entity {

    @PKey
    private UUID playerUUID;
    private long firstJoin;
    private boolean isPlayerHiding;
    private boolean isMuted;
    private long timeForUnmute;
    private boolean isBuildEnabled;
    private boolean isFlying;
    private boolean isPVPEnabled;
    private List<Warn> infractions;

    private PlayerData() {}

    public PlayerData (UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.firstJoin = System.currentTimeMillis();
    }

}
