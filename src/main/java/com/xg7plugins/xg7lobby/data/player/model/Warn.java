package com.xg7plugins.xg7lobby.data.player.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Warn {

    private int level;
    private String warn;
    private long date;
    private UUID id;

}
