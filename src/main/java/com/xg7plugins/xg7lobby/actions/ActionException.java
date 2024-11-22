package com.xg7plugins.xg7lobby.actions;

public class ActionException extends RuntimeException {

    public ActionException(String action, String message) {
        super("Syntax error on " + action + "action! \n Content: " + message);
    }


}
