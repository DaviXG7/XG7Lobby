package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;

public class CommandProcess implements Listener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("block-commands.enabled", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.command.*")) return;

        if (XG7Lobby.getInstance().getConfig("config").getList("block-commands.blocked-commands", String.class).orElse(new ArrayList<>()).contains(event.getMessage().split(" ")[0])) {
            event.setCancelled(true);
            Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "chat.prohibited-command").thenAccept(text -> text.send(event.getPlayer()));
        }
    }
}
