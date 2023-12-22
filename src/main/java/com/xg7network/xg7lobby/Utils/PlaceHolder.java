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
        return switch (params) {
            case "warns" ->
                    String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getInfractions().size());
            case "chat_locked" -> String.valueOf(configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked"));
            case "muted" -> String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).isMuted());
            case "time_for_unmute" ->
                    String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getLastDayToUnmute());
            case "first_join" -> String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).getFirstJoin());
            case "lobby_location" ->
                    new LobbyLocation().getLocation() == null ? "null" : new LobbyLocation().getLocation().getWorld().getName() + ", " + (int) new LobbyLocation().getLocation().getX() + ", " + (int) new LobbyLocation().getLocation().getY() + ", " + (int) new LobbyLocation().getLocation().getZ();
            case "players_hide" ->
                    String.valueOf(PlayersManager.getData(player.getUniqueId().toString()).isPlayershide());
            default -> null;
        };
    }
}
