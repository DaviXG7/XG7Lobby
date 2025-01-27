package com.xg7plugins.xg7lobby.lobby.player;

import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.FKey;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Table(name = "warns")
public class Warn implements Entity {

    @Pkey
    private UUID id;
    @FKey(origin_table = LobbyPlayer.class, origin_column = "playerUUID")
    private UUID playerUUID;
    private int level;
    private String reason;
    private long date;

    private Warn() {}

    public Warn(UUID playerUUID, int level, String reason) {
        this.id = UUID.randomUUID();
        this.playerUUID = playerUUID;
        this.level = level;
        this.reason = reason;
        this.date = System.currentTimeMillis();
    }
}
