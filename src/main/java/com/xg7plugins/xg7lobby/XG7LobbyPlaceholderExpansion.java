package com.xg7plugins.xg7lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.playerdata.PlayerData;
import com.xg7plugins.tasks.TaskState;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

public class XG7LobbyPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "xg7lobby";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DaviXG7";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        if (lobbyPlayer == null) {
            return null;
        }

        switch (identifier) {
            case "chat_locked":
                return XG7Plugins.serverInfo().getAtribute("lobbyChatLocked", Boolean.class).orElse(false) + "";
            case "random_lobby_location":
                return XG7Lobby.getInstance().getLobbyManager().getRandomLobby().join().toString();
            case "player_warns":
                return lobbyPlayer.getInfractions().size() + "";
            case "player_is_hiding":
                return lobbyPlayer.isPlayerHiding() + "";
            case "player_is_muted":
                return lobbyPlayer.isMuted() + "";
            case "player_time_for_unmute":
                return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(lobbyPlayer.getTimeForUnmute());
            case "player_is_build_enabled":
                return lobbyPlayer.isBuildEnabled() + "";
            case "player_is_flying":
                return lobbyPlayer.isFlying() + "";
            case "player_is_in_pvp":
                return XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(player) + "";
            case "players_in_global_pvp":
                return XG7Lobby.getInstance().getGlobalPVPManager().getInPVP().size() + "";
            case "player_global_pvp_kills":
                return lobbyPlayer.getGlobalPVPKills() + "";
            case "player_global_pvp_deaths":
                return lobbyPlayer.getGlobalPVPDeaths() + "";
            case "player_global_pvp_kdr":
                return (lobbyPlayer.getGlobalPVPDeaths() == 0 ? lobbyPlayer.getGlobalPVPKills() : lobbyPlayer.getGlobalPVPKills() / lobbyPlayer.getGlobalPVPDeaths()) + "";
        }

        return null;
    }
}
