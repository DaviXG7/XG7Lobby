package com.xg7plugins.xg7lobby.lobby.player;

import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.FKey;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Table(name = "warns")
@ToString
public class Warn implements Entity<UUID,Warn> {

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

    @Override
    public boolean equals(Warn warn) {
        return warn.getID().equals(this.id) && warn.getPlayerUUID().equals(this.playerUUID);
    }

    @Override
    public UUID getID() {
        return id;
    }
}
