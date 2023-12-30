package com.xg7network.xg7lobby.Module.Events.Jumps;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class DoubleJump implements Listener {

    public static HashMap<UUID, Player> isJumping = new HashMap<>();

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SPECTATOR && player.getGameMode() != GameMode.CREATIVE) {
            if (PluginUtil.isInWorld(player)) {
                if (!FlyManager.canfly.containsKey(player.getUniqueId()))
                    FlyManager.canfly.put(player.getUniqueId(), false);
                if (!FlyManager.canfly.get(player.getUniqueId())) {
                    if (!FlyManager.canfly.get(player.getUniqueId())) {
                        if (player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm())) {
                            player.setAllowFlight(false);
                            String s = configManager.getConfig(ConfigType.CONFIG).getString("double-jump.sound");
                            PluginUtil.playSound(player, s);
                            player.setVelocity(player.getEyeLocation().getDirection().multiply(configManager.getConfig(ConfigType.CONFIG).getDouble("double-jump.strength")).setY(configManager.getConfig(ConfigType.CONFIG).getDouble("double-jump.jump")));

                            isJumping.put(player.getUniqueId(), player);
                            event.setCancelled(true);
                        } else event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (isJumping.containsKey(player.getUniqueId())) {
            if (!player.isOnGround()) player.setAllowFlight(false);
            else {
                isJumping.remove(player.getUniqueId());
                player.setAllowFlight(true);
            }

        }
    }
}
