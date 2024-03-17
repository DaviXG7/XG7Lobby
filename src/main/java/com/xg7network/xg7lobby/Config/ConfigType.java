package com.xg7network.xg7lobby.Config;

public enum ConfigType {
    CONFIG("config"),
    MESSAGES(),
    DATA("data"),
    SELECTORS("selectors");

    private String type;
    ConfigType () {}
    ConfigType (String type) {
        this.type = type;
    }


    public String getFilePath() {
        return type + ".yml";
    }

    protected void setMessageFilePath(String messagePath) {
        if (this == MESSAGES) {
            this.type = type;
        }
    }
}
