package com.xg7plugins.xg7lobby.events.air_events;

import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.events.LobbyEvent;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class FlyEvent implements LobbyEvent {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getCommandManager().getCommands().containsKey("xg7lobbyfly");
    }

    @EventHandler
    public void onToggleGamemode(PlayerGameModeChangeEvent event) {
        LobbyPlayer.cast(event.getPlayer().getUniqueId(), true).thenAccept(LobbyPlayer::fly);
    }

    @Override
    public void onWorldLeave(Player player, World newWorld) {
        player.setAllowFlight(
                player.getGameMode() == GameMode.CREATIVE ||
                        player.getGameMode() == GameMode.SPECTATOR
        );
    }
}
