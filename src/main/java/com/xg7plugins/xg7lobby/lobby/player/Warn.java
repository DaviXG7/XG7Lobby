package com.xg7plugins.xg7lobby.lobby.player;

import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.FKey;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Table(name = "warns")
public class Warn implements Entity {

    @Pkey
    private UUID id;
    @FKey(origin_table = LobbyPlayer.class, origin_column = "playerUUID")
    private UUID playerUUID;
    private int level;
    private String warn;
    private long date;
}
