package com.xg7plugins.xg7lobby.events.ChatEvents;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.COMMANDS, "commands.xg7lobbymute.enabled");
    }

    public static String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append("s");
        }

        return result.toString().trim();
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());

        if (data.isMuted()) {
            if (data.getTimeForUnmute() - System.currentTimeMillis() < 0 && data.getTimeForUnmute() != 0) {
                data.setMuted(false);
                data.setTimeForUnmute(0);
                CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
                SQLHandler.update("UPDATE players SET ismuted = ?, timeforunmute = ? WHERE id = ?", data.isMuted(), data.getTimeForUnmute(), data.getId());
                return;
            }
            event.setCancelled(true);
            if (data.getTimeForUnmute() != 0) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-tempmute-chat").replace("[TIME]", formatDuration(data.getTimeForUnmute() - System.currentTimeMillis())), event.getPlayer());
                return;
            }
            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-mute-chat"), event.getPlayer());
        }
    }
}
