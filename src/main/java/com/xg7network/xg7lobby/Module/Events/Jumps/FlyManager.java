package com.xg7network.xg7lobby.Module.Events.Jumps;

import com.xg7network.xg7lobby.Configs.ConfigType;
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

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

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

                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("double-jump.enabled")) {

                    if (player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm())) player.setAllowFlight(true);

                } else {
                    player.setAllowFlight(canfly.get(player.getUniqueId()));
                    player.setFlying(canfly.get(player.getUniqueId()));
                }


            }
        }, 15);

    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        if (Players.getPlayers().containsKey(p.getUniqueId()))
                            if (configManager.getConfig(ConfigType.CONFIG).getBoolean("double-jump.enabled")) {
                                p.setAllowFlight(p.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
                            } else {

                                p.setAllowFlight(false);
                                p.setFlying(false);
                            }

                    });
                }, 10);
        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(p -> {
        if (Players.getPlayers().containsKey(p.getUniqueId()))
            if (configManager.getConfig(ConfigType.CONFIG).getBoolean("double-jump.enabled")) {
                p.setAllowFlight(p.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
            }

        }), 0, 10);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Players.getPlayers().containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("double-jump.enabled")) {
                    canfly.put(player.getUniqueId(), false);
                    player.setAllowFlight(player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
                } else {
                    canfly.put(player.getUniqueId(), false);
                }
            }, 10);
        }


    }

    @Override
    public void onDisable() {

    }

}
