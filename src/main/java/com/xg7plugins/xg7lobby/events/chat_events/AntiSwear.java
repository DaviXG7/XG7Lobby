package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class AntiSwear implements Listener {

    private final HashMap<String, Integer> tolerance = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("anti-swearing.enabled", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.chat.swear")) return;



    }
}
