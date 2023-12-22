package com.xg7network.xg7lobby.Utils;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Player.PlayersManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class PlaceHolder extends PlaceholderExpansion {
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
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("warns")) return String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getInfractions().size());
        else if (params.equals("chat_locked")) return String.valueOf(configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked"));
        else if (params.equals("muted")) return String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).isMuted());
        else if (params.equals("time_for_unmute")) return String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getLastDayToUnmute());
        else if (params.equals("first_join")) return String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getFirstJoin());
        else if (params.equals("lobby_location")) return new LobbyLocation().getLocation() == null ? "null" : new LobbyLocation().getLocation().getWorld().getName() + ", " + new LobbyLocation().getLocation().getX() + ", " + new LobbyLocation().getLocation().getZ();
        else if (params.equals("players_hide")) return String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).isPlayershide());
        return null;
    }
}
