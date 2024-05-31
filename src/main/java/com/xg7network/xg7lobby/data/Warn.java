package com.xg7network.xg7lobby.data;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.UUID;

@Getter
public class Warn {

    @Setter
    private String warn;
    private String id;
    private String playerid;
    private long when;

    public Warn(String playerid, String warn, long when) {
        this.warn = warn;
        this.when = when;
        this.playerid = playerid;
        this.id = UUID.randomUUID().toString();
    }

    public Warn(String playerid, String id, String warn, long when) {
        this.warn = warn;
        this.when = when;
        this.playerid = playerid;
        this.id = id;
    }

    public String getWhenFormatted() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(when);
    }

}
