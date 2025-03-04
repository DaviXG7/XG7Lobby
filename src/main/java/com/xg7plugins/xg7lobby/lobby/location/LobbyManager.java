package com.xg7plugins.xg7lobby.lobby.location;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LobbyManager {

    @Getter
    private final LobbyLocationDAO dao;

    public LobbyManager() {
        this.dao = new LobbyLocationDAO();
    }

    public CompletableFuture<Void> saveLobby(LobbyLocation lobbyLocation) {
        return CompletableFuture.runAsync(() -> {
            if (lobbyLocation == null) return;
            if (XG7Plugins.getInstance().getDatabaseManager().getProcessor().exists(XG7Lobby.getInstance(), LobbyLocation.class, "id", lobbyLocation.getID()).join()) {
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

            List<LobbyLocation> cachedLobbies = XG7Plugins.getInstance().getDatabaseManager().getCachedEntities().asMap().join().values().stream().filter(entity -> entity instanceof LobbyLocation).map(entity -> (LobbyLocation) entity).collect(Collectors.toList());

            List<LobbyLocation> cachedLocalLobbies = cachedLobbies.stream().filter(location -> location.getServer().equals(XG7Plugins.serverInfo())).collect(Collectors.toList());

            if (!cachedLocalLobbies.isEmpty()) return cachedLocalLobbies.get(new Random().nextInt(cachedLocalLobbies.size()));

            List<LobbyLocation> lobbies = dao.getAll().join();

            if (lobbies.isEmpty()) return null;

            List<LobbyLocation> localLobbies = lobbies.stream().filter(location -> location.getServer().equals(XG7Plugins.serverInfo())).collect(Collectors.toList());

            if (!localLobbies.isEmpty()) return localLobbies.get(new Random().nextInt(localLobbies.size()));

            return lobbies.get(new Random().nextInt(lobbies.size()));

        });
    }

}
