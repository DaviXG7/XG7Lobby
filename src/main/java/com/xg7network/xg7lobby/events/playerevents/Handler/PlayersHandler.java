package com.xg7network.xg7lobby.events.playerevents.Handler;

import org.bukkit.entity.Player;

public interface PlayersHandler {
    boolean isEnabled();
    void init();
    void onJoinWorld(Player player);
    void onQuitWorld(Player player);
    default boolean isPlayerInWorld(Player player) {
        return PlayerManager.getPlayersInWorld().contains(player.getUniqueId());
    }


}
