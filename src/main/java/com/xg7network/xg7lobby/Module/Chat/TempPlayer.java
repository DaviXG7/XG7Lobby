package com.xg7network.xg7lobby.Module.Chat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TempPlayer {

    private Player player;
    private String lastMessage;
    private List<String> spam = new ArrayList<>();

    public TempPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getSpam() {
        return spam;
    }

    public void addSpam(String spamrequest) {
        spam.add(spamrequest);
    }
}
