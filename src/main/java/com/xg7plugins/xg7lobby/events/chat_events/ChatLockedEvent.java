package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLockedEvent implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.chat.ignore-lock")) return;
        if (XG7Plugins.serverInfo().getAtribute("chat-locked", Boolean.class).orElse(false)) {
            if (XG7Lobby.getInstance().getConfig("config").get("lock-chat-only-on-lobby", Boolean.class).orElse(false) && !XG7Lobby.getInstance().isInWorldEnabled(event.getPlayer())) return;

            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "chat.locked").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
        }
    }
}
