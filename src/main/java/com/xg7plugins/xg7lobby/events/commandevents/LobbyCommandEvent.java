package com.xg7plugins.xg7lobby.events.commandevents;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LobbyCommandEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "before-tp.dont-move");
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (CacheManager.getLobbyCache().asMap().containsKey(event.getPlayer().getUniqueId())) {
            if (
                    event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                    event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                    event.getFrom().getBlockZ() != event.getTo().getBlockZ()
            )
            {
                Player player = event.getPlayer();
                Text.send(Config.getString(ConfigType.MESSAGES, "lobby.on-tp-cancel"), player);
                TaskManager.cancelTask("cooldown:lobby=" + player.getUniqueId());
                CacheManager.remove(player.getUniqueId(), CacheType.LOBBY_COOLDOWN);
            }
        }
    }
}
