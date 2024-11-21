package com.xg7plugins.xg7lobby.model;

import com.xg7plugins.data.database.Entity;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Warn implements Entity {

    @PKey
    private UUID id;
    @FKey(table = "PlayerData", reference = "playerUUID")
    private UUID playerUUID;
    private int level;
    private String warn;
    private long date;
}
