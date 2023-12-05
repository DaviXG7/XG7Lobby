package com.xg7network.xg7lobby.DefautCommands;

import org.bukkit.ChatColor;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public enum ErrorMessages {

    NO_PEMISSION("You don't have permission to use this command!"),
    MISSING_ARGS("The right way to use the command is: "),
    NOT_PLAYER("Only players can execute this command!"),
    NOT_IN_WORLD("You cannot execute this command in a disabled world!"),
    PLAYER_DOESNOT_EXIST("This player does not exist!"),
    PLAYER_IS_NOT_ONLINE("This player is not online"),
    SYNTAX_ERROR("This command was written incorrectly!");


    private String s;

    ErrorMessages(String s) {
        this.s = s;
    }

    public String getMessage() {
        return prefix + ChatColor.RED + s;
    }
}
