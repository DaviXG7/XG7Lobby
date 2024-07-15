package com.xg7plugins.xg7lobby.events.jumpevents;

import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.*;

public class DoubleJumpEvent implements JoinQuitEvent {
    public static final Set<UUID> isJumping = new HashSet<>();

    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "double-jump.enabled");
    }

    @Override
    public void onWorldLeave(Player player) {
        player.setAllowFlight(!player.getGameMode().equals(GameMode.CREATIVE) || !player.getGameMode().equals(GameMode.SPECTATOR));
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;
        event.getPlayer().setAllowFlight(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !event.getPlayer().getGameMode().equals(GameMode.SPECTATOR));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().hasPermission(PermissionType.DOUBLE_JUMP.getPerm())) return;
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();

        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (Config.getBoolean(ConfigType.CONFIG, "pvp.disable-double-jump") && PlayerManager.getPlayerData(player.getUniqueId()).isPVPEnabled()) return;

        if (!PlayerManager.getPlayerData(player.getUniqueId()).isFlying() && isJumping.contains(player.getUniqueId()) && player.isOnGround()) {
            player.setAllowFlight(true);
            isJumping.remove(player.getUniqueId());
        }
    }
    @EventHandler
    public void onToggleFly(PlayerToggleFlightEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();

        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) return;

        if (!PlayerManager.getPlayerData(player.getUniqueId()).isFlying() && !isJumping.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            isJumping.add(player.getUniqueId());
            player.setVelocity(player.getEyeLocation().getDirection().multiply(Config.getDouble(ConfigType.CONFIG, "double-jump.strength")).setY(Config.getDouble(ConfigType.CONFIG, "double-jump.jump")));
            String[] sound = Config.getString(ConfigType.CONFIG, "double-jump.sound").split(", ");
            player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
        }

    }

}
