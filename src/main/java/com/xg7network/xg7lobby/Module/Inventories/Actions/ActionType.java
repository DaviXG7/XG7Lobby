package com.xg7network.xg7lobby.Module.Inventories.Actions;

public enum ActionType {

    TITLE,
    SUBTITLE,
    TITSUBTIT,
    OPEN,
    CLOSE,
    GAMEMODE,
    TP,
    BROADCAST,
    SUMMON,
    EFFECT,
    COMMAND,
    CONSOLE,
    FLY,
    MESSAGE,
    SOUND,
    SWAP,
    ACTIONBAR,
    HIDE,
    SHOW;

    public ActionType[] getTypes() {
        return values();
    }


}
