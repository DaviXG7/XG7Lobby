package com.xg7plugins.xg7lobby.events.air_events;

import com.cryptomorin.xseries.XSound;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;
import java.util.UUID;

public class MultiJumpEvent implements Listener {

    private final HashMap<UUID, Integer> jumpingPlayers = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("multi-jumps.enabled", Boolean.class).orElse(false);
    }


    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        LobbyPlayer player = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();

        if (player.isFlying() || (jumpingPlayers.containsKey(player.getPlayerUUID()) && jumpingPlayers.get(player.getPlayerUUID()) == 0) || player.getPlayer().getGameMode() == GameMode.CREATIVE || player.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        event.setCancelled(true);

        Config config = XG7Lobby.getInstance().getConfig("config");

        double power = config.get("multi-jumps.power", Double.class).orElse(0.5);
        double height = config.get("multi-jumps.height", Double.class).orElse(1.0);

        String[] soundString = config.get("multi-jumps.sound", String.class).orElse("ENTITY_BAT_TAKEOFF, 1, 1").split(", ");

        Sound sound = XSound.matchXSound(soundString[0]).orElse(XSound.ENTITY_SLIME_JUMP).get();

        float volume = Float.parseFloat(soundString[1]);
        float pitch = Float.parseFloat(soundString[2]);

        player.getPlayer().setVelocity(player.getPlayer().getLocation().getDirection().multiply(power).setY(height));

        player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), sound, volume, pitch);

        jumpingPlayers.putIfAbsent(player.getPlayerUUID(), config.get("multi-jumps.limit", Integer.class).orElse(2));

        jumpingPlayers.put(player.getPlayerUUID(), jumpingPlayers.get(player.getPlayer().getUniqueId()) - 1);

        if (jumpingPlayers.get(player.getPlayerUUID()) == 0) player.getPlayer().setAllowFlight(false);

        Text.fromLang(player.getPlayer(),XG7Lobby.getInstance(), "multi-jump-left").thenAccept(text -> text.replace("jumps", jumpingPlayers.getOrDefault(player.getPlayerUUID(), config.get("multi-jumps.limit", Integer.class).orElse(2)) + "").send(player.getPlayer()));

    }

    @EventHandler(isOnlyInWorld = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!jumpingPlayers.containsKey(event.getPlayer().getUniqueId())) return;

        if (event.getPlayer().isOnGround()) {
            event.getPlayer().setAllowFlight(true);
            jumpingPlayers.remove(event.getPlayer().getUniqueId());
        }

    }
}
