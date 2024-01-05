package com.xg7network.xg7lobby.Module.Events.Jumps;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class FlyManager extends Module implements Listener {
    public static HashMap<UUID, Boolean> canfly = new HashMap<>();

    public FlyManager(XG7Lobby plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) {

                player.setAllowFlight(false);
                player.setFlying(false);

            } else {

                if (player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm())) player.setAllowFlight(true);

            }
        }, 15);

    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (Players.getPlayers().containsKey(p.getUniqueId()))
                    if (p.hasPermission(PermissionType.DOUBLE_JUMP.getPerm())){
                        p.setAllowFlight(true);
                    }
            });
        }, 0, 5);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Players.getPlayers().containsKey(player.getUniqueId())) {
            if (player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm())) {
                canfly.put(player.getUniqueId(), false);
                player.setAllowFlight(true);
            }
        }


    }

    @Override
    public void onDisable() {

    }

}
