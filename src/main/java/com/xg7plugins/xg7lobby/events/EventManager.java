package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.ChatEvents.AntiSwearingEvent;
import com.xg7plugins.xg7lobby.events.ChatEvents.LockChatEvent;
import com.xg7plugins.xg7lobby.events.ChatEvents.MuteEvent;
import com.xg7plugins.xg7lobby.events.commandevents.*;
import com.xg7plugins.xg7lobby.events.commandevents.commandtabevents.CommandPreProcessEvent;
import com.xg7plugins.xg7lobby.events.commandevents.commandtabevents.PluginTabCompleteEvent;
import com.xg7plugins.xg7lobby.events.jumpevents.DoubleJumpEvent;
import com.xg7plugins.xg7lobby.events.jumpevents.LaunchpadEvent;
import com.xg7plugins.xg7lobby.events.menuevents.MenuClickEvent;
import com.xg7plugins.xg7lobby.events.menuevents.SelectorClickEvent;
import com.xg7plugins.xg7lobby.events.menuevents.SelectorEvent;
import com.xg7plugins.xg7lobby.events.playerevents.*;
import com.xg7plugins.xg7lobby.events.commandevents.commandtabevents.TabCompleteEvent;
import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
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
    private static final List<PacketPlayEvent> packetEvents = new ArrayList<>();

    @Getter
    private static List<String> worlds;

    public static void reload() {
        Log.info("Reloading events...");
        HandlerList.unregisterAll(XG7Lobby.getPlugin());
        new EventManager().init();
    }

    public void init() {
        events.add(new LobbyCommandEvent());
        events.add(new JoinEvent());
        events.add(new WorldLeaveEvent());
        events.add(new PlayerEvents());
        events.add(new BuildCommandEvent());
        events.add(new PVPCommandEvent());
        events.add(new LaunchpadEvent());
        events.add(new DoubleJumpEvent());
        events.add(new VanishCommandEvent());
        events.add(new ScoreEvents());
        events.add(new MenuClickEvent());
        events.add(new SelectorEvent());
        events.add(new SelectorClickEvent());
        events.add(new WarnsCommandEvent());
        events.add(new MuteEvent());
        events.add(new LockChatEvent());
        events.add(new AntiSwearingEvent());
        events.add(new CommandPreProcessEvent());

        worlds = Config.getList(ConfigType.CONFIG, "enabled-worlds");

        XG7Lobby.getPlugin().getServer().getPluginManager().registerEvents(this, XG7Lobby.getPlugin());

        events.stream().filter(Event::isEnabled).collect(Collectors.toList()).forEach(event -> XG7Lobby.getPlugin().getServer().getPluginManager().registerEvents(event, XG7Lobby.getPlugin()));

        Log.loading("Events loaded!");
    }

    public static void initPacketEvents() {
        Log.loading("Loading packet events...");
        packetEvents.add(new TabCompleteEvent());
        packetEvents.add(new PluginTabCompleteEvent());

        Bukkit.getOnlinePlayers().forEach(PacketEvents::create);
        Log.loading("Packets loaded!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (worlds.contains(event.getPlayer().getWorld().getName())) events.stream().filter(event1 -> event1 instanceof JoinQuitEvent && event1.isEnabled()).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onJoin(event));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (worlds.contains(event.getPlayer().getWorld().getName())) events.stream().filter(event1 -> event1 instanceof JoinQuitEvent && event1.isEnabled()).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onQuit(event));
    }
    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        if (worlds.contains(event.getFrom().getWorld().getName()) && !worlds.contains(event.getTo().getWorld().getName())) {
            events.stream().filter(event1 -> event1 instanceof JoinQuitEvent && event1.isEnabled()).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onWorldLeave(event.getPlayer()));
        }
        if (!worlds.contains(event.getFrom().getWorld().getName()) && worlds.contains(event.getTo().getWorld().getName())) {
            events.stream().filter(event1 -> event1 instanceof JoinQuitEvent && event1.isEnabled()).collect(Collectors.toList()).forEach(i -> ((JoinQuitEvent)i).onWorldJoin(event.getPlayer()));
        }
    }

}
