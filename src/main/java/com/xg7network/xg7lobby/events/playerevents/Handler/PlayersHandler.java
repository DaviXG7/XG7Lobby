package com.xg7network.xg7lobby.events.playerevents.Handler;

import org.bukkit.entity.Player;

public interface PlayersHandler {
    void init();
    void onJoin(Player player);
    void onQuit(Player player);




}
