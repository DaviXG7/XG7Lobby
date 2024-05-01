package com.xg7network.xg7lobby.config;

import com.xg7network.xg7lobby.utils.Text.TextUtil;

public enum Messages {

    PERM_CMD("no-perm-messages.permission-command"),
    PERM_DROP("no-perm-messages.permission-drop"),
    PERM_PICKUP("no-perm-messages.permission-pickup"),
    PERM_INTERACT("no-perm-messages.permission-interact"),
    PERM_PLACE("no-perm-messages.no-perm-messages.permission-place"),
    PERM_BREAK("no-perm-messages.permission-break"),
    PERM_ATTACK("no-perm-messages.permission-attack"),

    ERROR_MISSING_ARGS("error-messages.missing-args"),
    ERROR_NOT_PLAYER("error-messages.not-player"),
    ERROR_NOT_IN_WORLD("error-messages.out-world"),
    ERROR_PLAYER_DOESNOT_EXIST("error-messages.player-doesnot-exist"),
    ERROR_PLAYER_IS_NOT_ONLINE("error-messages.player-not-online"),
    ERROR_SYNTAX_ERROR("error-messages.systax-error"),

    CMD_LOBBY_WARN("commands.lobby-warn"),
    CMD_ADM_LOBBY_WARN("commands.adm-lobby-warn"),
    CMD_EFLY_OTHER("commands.fly-enabled-other"),
    CMD_DFLY_OTHER("commands.fly-disabled-other"),
    CMD_EFLY("commands.fly-enabled"),
    CMD_DFLY("commands.fly-disabled"),
    CMD_EBUILD("commands.build-enabled"),
    CMD_DBUILD("commands.build-disabled"),
    CMD_BUILD_WARN("commands.build-warn"),
    CMD_GAMEMODE_PLAYER("commands.gamemode-player"),
    CMD_GAMEMODE_TARGET("commands.gamemode-target"),
    CMD_WHEN_MUTED("commands.when-muted"),
    CMD_ON_MUTE("commands.on-mute"),
    CMD_ON_MUTE_WITH_TIME("commands.on-mute-with-time"),
    CMD_ON_LOCK_CHAT("commands.on-lock-chat"),
    CMD_ON_HIDE("commands.on-hide"),
    CMD_ON_SHOW("commands.on-show"),
    CMD_ON_WARN("commands.on-warn"),
    CMD_ON_WARN_REMOVED("commands.on-warn-remove"),

    EVENT_ON_COOLDOWN("events.on-cooldown"),
    EVENT_ON_HIDE("events.on-hide"),
    EVENT_ON_SHOW("events.on-show"),
    EVENT_BADWORD("events.badword"),
    EVENT_COMMAND_BLOCK("events.command-block"),
    EVENT_SPAM("events.spam"),
    EVENT_ON_SPAM_MUTE("events.on-spam-mute"),
    EVENT_MESSAGE_COOLDOWN("events.message-cooldown"),
    EVENT_REPEATED_MESSAGE("events.repeated-message"),
    EVENT_QUICK_MESSAGES("events.quick-messages"),
    EVENT_CHAT_LOCKED("events.chat-locked"),
    EVENT_ON_LOBBY_TELEPORT("events.on-lobby-teleport"),
    EVENT_LOCCY_COOLDOWN_MESSAGE("events.lobby-cooldown-message"),
    EVENT_TELEPORT_CANCELLED("events.teleport-cancelled"),
    EVENT_JOIN_MESSAGE("join.join-message"),
    EVENT_LEAVE_MESSAGE("join.leave-message"),
    EVENT_FIRST_JOIN_MESSAGE("join.first-join-message");

    private String s;

    Messages(String s) {
        this.s = s;
    }

    public String getMessage() {
        return TextUtil.get(ConfigManager.getConfig(ConfigType.MESSAGES).getString(s));
    }

}
