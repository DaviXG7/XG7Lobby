package com.xg7network.xg7lobby.data;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class Warn {

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

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public String getWhen() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(when);
    }
    public long getWhenInMills() {
        return this.when;
    }

    public String getId() {
        return id;
    }
}
