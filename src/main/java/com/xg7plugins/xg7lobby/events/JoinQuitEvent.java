package com.xg7plugins.xg7lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface JoinQuitEvent extends Event {
    default void onWorldJoin(Player player) {}
    default void onWorldLeave(Player player) {}
    default void onJoin(PlayerJoinEvent event) {}
    default void onQuit(PlayerQuitEvent event) {}
}
