package com.xg7plugins.xg7lobby.events;

import org.bukkit.entity.Player;

public abstract interface PacketPlayEvent extends Event {

    String[] getPacketsNames();

    boolean isCancelled();

    interface PacketPlayInEvent extends PacketPlayEvent {
        void in(Player player, Object packet);
    }
    interface PacketPlayOutEvent extends PacketPlayEvent {
        void out(Player player, Object packet);
    }
}
