package com.xg7plugins.xg7lobby.data;

public enum ConfigType {

    CONFIG("config"),
    MESSAGES("messages"),
    DATA("data"),
    COMMANDS("commands"),
    SELECTOR("selector");

    private String config;

    ConfigType(String config) {
        this.config = config;
    }

    public String getConfig() {
        return this.config + ".yml";
    }

}
