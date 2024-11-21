package com.xg7plugins.xg7lobby.actions;

public class ActionException extends Exception {

    public ActionException(ActionType type, String message) {
        super("Syntax error on " + type + "action! \n Content: " + message);
    }


}
