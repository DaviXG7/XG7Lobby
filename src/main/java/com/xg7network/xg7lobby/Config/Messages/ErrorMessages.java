package com.xg7network.xg7lobby.Config.Messages;

import com.xg7network.xg7lobby.Config.ConfigManager;
import com.xg7network.xg7lobby.Config.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;

public enum ErrorMessages {

    NO_PEMISSION(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.no-permission")),
    MISSING_ARGS(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.missing-args")),
    NOT_PLAYER(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.not-player")),
    NOT_IN_WORLD(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.out-world")),
    PLAYER_DOESNOT_EXIST(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.player-doesnot-exist")),
    PLAYER_IS_NOT_ONLINE(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.player-not-online")),
    SYNTAX_ERROR(ConfigManager.getConfig(ConfigType.MESSAGES).getString("commands.systax-error"));


    private String s;

    ErrorMessages(String s) {
        this.s = s;
    }

    public String getMessage() {
        return TextUtil.get(s);
    }
}
