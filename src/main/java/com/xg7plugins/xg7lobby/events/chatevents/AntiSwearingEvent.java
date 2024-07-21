package com.xg7plugins.xg7lobby.events.chatevents;

import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiSwearingEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "anti-swearing.enabled");
    }

    @EventHandler
    public void onSwear(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(PermissionType.CHAT_SWEAR.getPerm())) return;

        for (String w : Config.getList(ConfigType.CONFIG, "anti-swearing.blocked-words")) {
            if (event.getMessage().toLowerCase().contains(w.toLowerCase())) {
                event.setCancelled(true);
                Text.send(Config.getString(ConfigType.MESSAGES, "chat.on-swearing"), event.getPlayer());
                return;
            }
        }
    }
}
