package com.xg7network.xg7lobby.Player;

import java.util.Date;

public class Warn {

    private String warn;

    private Date when;

    public Warn(String warn, Date when) {
        this.warn = warn;
        this.when = when;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
