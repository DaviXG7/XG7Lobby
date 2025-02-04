package com.xg7plugins.xg7lobby.lobby.location;

import com.google.gson.reflect.TypeToken;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.cache.ObjectCache;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LobbyManager {

    private final LobbyLocationDAO dao;
    private final Config defaults;

    public LobbyManager(XG7Lobby lobby) {

        defaults = lobby.getConfig("config");

        this.dao = new LobbyLocationDAO();
    }

    public CompletableFuture<Void> saveLobby(LobbyLocation lobbyLocation) {
        return CompletableFuture.runAsync(() -> {
            if (lobbyLocation == null) return;
            if (XG7Plugins.getInstance().getDatabaseManager().getProcessor().exists(XG7Lobby.getInstance(), LobbyLocation.class, "id", lobbyLocation.getId()).join()) {
                dao.update(lobbyLocation).join();
                return;
            }
            dao.add(lobbyLocation).join();

        });
    }

    public CompletableFuture<LobbyLocation> getLobby(String id) {
        return dao.get(id);
    }
    public CompletableFuture<LobbyLocation> getRandomLobby() {
        return CompletableFuture.supplyAsync(() -> {

            List<LobbyLocation> lobbies = dao.getAll().join();

            if (lobbies.isEmpty()) return null;

            List<LobbyLocation> localLobbies = lobbies.stream().filter(location -> location.getServer().equals(XG7Lobby.getInstance().getServerInfo())).collect(Collectors.toList());

            if (!localLobbies.isEmpty()) return localLobbies.get(new Random().nextInt(localLobbies.size()));

            return null;


        });
    }

}
