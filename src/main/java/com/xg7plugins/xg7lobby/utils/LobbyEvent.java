package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.events.Event;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public abstract class LobbyEvent implements Event {

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        List<String> enabledWorlds = XG7Lobby.getInstance().getEnabledWorlds();
        if (enabledWorlds.contains(event.getFrom().getWorld().getName()) && !enabledWorlds.contains(event.getTo().getWorld().getName())) {
            onWorldLeave(event.getPlayer());
            return;
        }
        if (!enabledWorlds.contains(event.getFrom().getWorld().getName()) && enabledWorlds.contains(event.getTo().getWorld().getName())) {
            onWorldJoin(event.getPlayer());
        }
    }


    public void onWorldJoin(Player player) {}
    public void onWorldLeave(Player player) {}
}
