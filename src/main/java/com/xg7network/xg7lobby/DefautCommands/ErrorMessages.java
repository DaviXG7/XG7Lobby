package com.xg7network.xg7lobby.DefautCommands;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.ChatColor;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public enum ErrorMessages {

    NO_PEMISSION(configManager.getConfig(ConfigType.MESSAGES).getString("commands.no-permission")),
    MISSING_ARGS(configManager.getConfig(ConfigType.MESSAGES).getString("commands.missing-args")),
    NOT_PLAYER(configManager.getConfig(ConfigType.MESSAGES).getString("commands.not-player")),
    NOT_IN_WORLD(configManager.getConfig(ConfigType.MESSAGES).getString("commands.out-world")),
    PLAYER_DOESNOT_EXIST(configManager.getConfig(ConfigType.MESSAGES).getString("commands.player-doesnot-exist")),
    PLAYER_IS_NOT_ONLINE(configManager.getConfig(ConfigType.MESSAGES).getString("commands.player-not-online")),
    SYNTAX_ERROR(configManager.getConfig(ConfigType.MESSAGES).getString("commands.systax-error"));


    private String s;

    ErrorMessages(String s) {
        this.s = s;
    }

    public String getMessage() {
        return TextUtil.get(s);
    }
}
