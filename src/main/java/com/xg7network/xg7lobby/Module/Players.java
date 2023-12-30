package com.xg7network.xg7lobby.Module;

import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class Players extends Module implements Listener {

    private static HashMap<UUID, Player> players = new HashMap<>();

    public Players(XG7Lobby plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {

        Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().stream().filter(PluginUtil::isInWorld).forEach(p -> players.put(p.getUniqueId(), p));
        }, 0, 5);

    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(p -> players.remove(p.getUniqueId()));
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        players.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {

        if (event.getTo() != null) {
            if (PluginUtil.isInWorld(event.getTo().getWorld()))
                players.put(event.getPlayer().getUniqueId(), event.getPlayer());
            else players.remove(event.getPlayer().getUniqueId());
        }

    }

    public static HashMap<UUID, Player> getPlayers() {
        return players;
    }
}
