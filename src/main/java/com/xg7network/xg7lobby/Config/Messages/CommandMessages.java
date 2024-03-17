package com.xg7network.xg7lobby.Config.Messages;

// E - enabeld D - disabled
public enum CommandMessages {

    LOBBY_WARN(),
    ADM_LOBBY_WARN,
    EFLY_OTHER,
    DFLY_OTHER,
    EFLY,
    DFLY,
    EBUILD,
    DBUILD,
    BUILD_WARN,
    GAMEMODE_PLAYER,
    GAMEMODE_TARGET,
    WHEN_MUTED,
    ON_MUTE,
    ON_MUTE_WITH_TIME,
    ON_LOCK_CHAT,
    ON_HIDE,
    ON_SHOW,
    ON_WARN,
    ON_WARN_REMOVED;

    String message;

}
