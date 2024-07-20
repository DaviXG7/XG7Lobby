package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class Placeholders extends PlaceholderExpansion {
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
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        switch (params) {
            case "first_join":
                return new SimpleDateFormat("dd/MM/aa HH:mm").format(PlayerManager.getPlayerData(player.getUniqueId()).getFirstJoin());
            case "players_hiding":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).isPlayerHiding());
            case "pvp_enabled":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).isPVPEnabled());
            case "fly_enabled":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).isFlying());
            case "build_enabled":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).isBuildEnabled());
            case "muted":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).isMuted());
            case "warns":
                return String.valueOf(PlayerManager.getPlayerData(player.getUniqueId()).getInfractions().size());
            default:
                return "";
        }
    }






}
