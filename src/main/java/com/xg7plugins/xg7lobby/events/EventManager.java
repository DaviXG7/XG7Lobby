package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.commandevents.BuildCommandEvent;
import com.xg7plugins.xg7lobby.events.commandevents.LobbyCommandEvent;
import com.xg7plugins.xg7lobby.events.commandevents.PVPCommandEvent;
import com.xg7plugins.xg7lobby.events.jumpevents.DoubleJumpEvent;
import com.xg7plugins.xg7lobby.events.jumpevents.LaunchpadEvent;
import com.xg7plugins.xg7lobby.events.playerevents.PlayerEvents;
import com.xg7plugins.xg7lobby.events.playerevents.WorldLeaveEvent;
import com.xg7plugins.xg7lobby.events.playerevents.JoinEvent;
import com.xg7plugins.xg7lobby.utils.Log;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventManager implements Listener {

    private static final List<Event> events = new ArrayList<>();

    @Getter
    private static List<String> worlds;

    public void init() {
        events.add(new LobbyCommandEvent());
        events.add(new JoinEvent());
        events.add(new WorldLeaveEvent());
        events.add(new PlayerEvents());
        events.add(new BuildCommandEvent());
        events.add(new PVPCommandEvent());
        events.add(new LaunchpadEvent());
        events.add(new DoubleJumpEvent());

        worlds = Config.getList(ConfigType.CONFIG, "enabled-worlds");

        XG7Lobby.getPlugin().getServer().getPluginManager().registerEvents(this, XG7Lobby.getPlugin());

        events.stream().filter(Event::isEnabled).collect(Collectors.toList()).forEach(event -> XG7Lobby.getPlugin().getServer().getPluginManager().registerEvents(event, XG7Lobby.getPlugin()));

        Log.loading("Events loaded!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (worlds.contains(event.getPlayer().getWorld().getName())) events.stream().filter(event1 -> event1 instanceof JoinQuitEvent).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onJoin(event));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (worlds.contains(event.getPlayer().getWorld().getName())) events.stream().filter(event1 -> event1 instanceof JoinQuitEvent).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onQuit(event));
    }
    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        if (worlds.contains(event.getFrom().getWorld().getName()) && !worlds.contains(event.getTo().getWorld().getName())) {
            events.stream().filter(event1 -> event1 instanceof JoinQuitEvent).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onWorldLeave(event.getPlayer()));
        }
        if (!worlds.contains(event.getFrom().getWorld().getName()) && worlds.contains(event.getTo().getWorld().getName())) {
            events.stream().filter(event1 -> event1 instanceof JoinQuitEvent).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onWorldJoin(event.getPlayer()));
        }
    }

}
