package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.event.player.PlayerMoveEvent;

public class LobbyCooldownEvent implements Listener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("lobby-teleport-cooldown.dont-move", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.command.lobby.bypass-cooldown")) return;
        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("lobby-cooldown-before", event.getPlayer())) {
            if (
                    event.getFrom().getBlockX() != event.getTo().getBlockX()
                            || event.getFrom().getBlockY() != event.getTo().getBlockY()
                            || event.getFrom().getBlockZ() != event.getTo().getBlockZ()
            ) {
                XG7Plugins.getInstance().getCooldownManager().removePlayer("lobby-cooldown-before", event.getPlayer().getUniqueId());
            }
        }

    }
}
