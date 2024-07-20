package com.xg7plugins.xg7lobby.events.ChatEvents;

import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LockChatEvent implements Event {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        if (Config.getBoolean(ConfigType.DATA, "chat-locked")) {
            if (event.getPlayer().hasPermission(PermissionType.CHAT.getPerm()) && Config.getBoolean(ConfigType.CONFIG, "ignore-adm-for-lock-chat")) return;
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, "chat.chat-locked"), event.getPlayer());
        }
    }
}
