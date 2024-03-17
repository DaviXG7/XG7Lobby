package com.xg7network.xg7lobby.Config;

public enum ConfigType {
    CONFIG("config"),
    MESSAGES("messages"),
    DATA("data"),
    SELECTORS("selectors");

    String type;
    ConfigType (String type) {
        this.type = type;
    }

    public String getFilePath() {
        return type + ".yml";
    }
}
