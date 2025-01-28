package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.events.PacketListener;
import com.xg7plugins.events.packetevents.PacketEventHandler;
import com.xg7plugins.utils.reflection.nms.PacketEvent;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.util.ArrayList;
import java.util.List;

public class CommandAntiTabOlder implements PacketListener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("block-commands.enabled", Boolean.class).orElse(false);
    }

    @PacketEventHandler(
            packet = "PacketPlayOutTabComplete"
    )
    public void onTabComplete(PacketEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.command.*")) return;

        List<String> commandsToBlock = XG7Lobby.getInstance().getConfig("config").getList("block-commands.blocked-commands", String.class).orElse(new ArrayList<>());

        String[] suggestionsArray = event.getPacket().getField("a");

        List<String> suggestions = new ArrayList<>();

        for (String suggestion : suggestionsArray) if (!commandsToBlock.contains(suggestion)) suggestions.add(suggestion);

        event.getPacket().setField("a", suggestions.toArray(new String[0]));
    }

}
