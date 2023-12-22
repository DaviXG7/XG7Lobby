package com.xg7network.xg7lobby.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Warn {

    private String warn;

    private long when;

    public Warn(String warn, long when) {
        this.warn = warn;
        this.when = when;
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

    public void setWhen(long when) {
        this.when = when;
    }
}
