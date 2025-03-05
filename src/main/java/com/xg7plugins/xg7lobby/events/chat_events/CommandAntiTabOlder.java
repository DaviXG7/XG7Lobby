package com.xg7plugins.xg7lobby.events.chat_events;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import com.xg7plugins.events.PacketListener;
import com.xg7plugins.events.packetevents.PacketEventHandler;
import com.xg7plugins.events.packetevents.PacketEventType;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PacketEventHandler(
        packet = PacketEventType.PLAY_SERVER_TAB_COMPLETE
)
public class CommandAntiTabOlder implements PacketListener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("block-commands.enabled", Boolean.class).orElse(false);
    }


    public void onPacketSend(PacketSendEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("xg7lobby.command.*")) return;

        List<String> commandsToBlock = XG7Lobby.getInstance().getConfig("config")
                .getList("block-commands.blocked-commands", String.class)
                .orElse(new ArrayList<>());

        WrapperPlayServerTabComplete wrapper = new WrapperPlayServerTabComplete(event);
        wrapper.getCommandMatches().removeIf(commandMatch -> commandsToBlock.contains(commandMatch.getText()));
    }


}
