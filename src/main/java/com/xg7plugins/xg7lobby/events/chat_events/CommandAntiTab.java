package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandAntiTab implements Listener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("block-commands.enabled", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onTab(PlayerCommandSendEvent event) {

        if (event.getPlayer().hasPermission("xg7lobby.command.*")) return;

        List<String> commandsToBlock = XG7Lobby.getInstance().getConfig("config").getList("block-commands.blocked-commands", String.class).orElse(new ArrayList<>());

        event.getCommands().removeAll(commandsToBlock);


    }
}
