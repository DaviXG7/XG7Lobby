package com.xg7network.xg7lobby.Configs;

public enum ConfigType {

    CONFIG("config"),
    MESSAGES("messages"),
    DATA("data/data"),
    SELECTORS("selectors");

    private String config;

    ConfigType(String config) {
        this.config = config;
    }

    public String getConfig() {
        return this.config + ".yml";
    }


}
