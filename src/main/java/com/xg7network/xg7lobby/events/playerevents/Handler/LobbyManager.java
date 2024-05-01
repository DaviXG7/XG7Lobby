package com.xg7network.xg7lobby.events.playerevents.Handler;

import com.xg7network.xg7lobby.utils.Other.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class LobbyManager implements Listener {
    private final List<UUID> playersInWorld = new ArrayList<>();
    private List<PlayersHandler> playersHandlers = new ArrayList<>();
    public void addListeners(PlayersHandler... handlers) {

        Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(playersInWorld::add);
        Arrays.stream(handlers).forEach(handler -> {
            handler.init();
            playersHandlers.add(handler);
        });

    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (PluginUtil.isInWorld(player)) {
            playersInWorld.add(player.getUniqueId());
            playersHandlers.forEach(playersHandler -> playersHandler.onJoin(player));
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playersInWorld.remove(player.getUniqueId());
        playersHandlers.forEach(playersHandler -> playersHandler.onQuit(player));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        Player player = event.getPlayer();
        if (PluginUtil.isInWorld(event.getFrom().getWorld()) && !PluginUtil.isInWorld(event.getTo().getWorld())) {
            playersInWorld.remove(player.getUniqueId());
            playersHandlers.forEach(playersHandler -> playersHandler.onQuit(player));
            return;
        }
        if (!PluginUtil.isInWorld(event.getFrom().getWorld()) && PluginUtil.isInWorld(event.getTo().getWorld())) {
            playersInWorld.add(player.getUniqueId());
            playersHandlers.forEach(playersHandler -> playersHandler.onJoin(player));
        }


        }




}
