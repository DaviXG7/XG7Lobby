package com.xg7plugins.xg7lobby.events;

import org.bukkit.entity.Player;

public interface PacketPlayEvent extends Event {

    String[] getPacketsNames();

    interface PacketPlayInEvent extends PacketPlayEvent {
        Object in(Player player, Object packet);
    }
    interface PacketPlayOutEvent extends PacketPlayEvent {
        Object out(Player player, Object packet);
    }
}
