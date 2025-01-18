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
    @Getter
    private final ObjectCache<String, LobbyLocation> lobbyLocationCache;
    private static final TypeToken<List<LobbyLocation>> listType = new TypeToken<List<LobbyLocation>>() {};


    public LobbyManager(XG7Lobby lobby) {

        defaults = lobby.getConfig("config");

        this.lobbyLocationCache = new ObjectCache<>(
                lobby,
                defaults.getTime("lobby-location-cache-expires").orElse(60 * 10 * 1000L),
                false,
                "lobby-cache",
                false,
                String.class,
                LobbyLocation.class
        );

        this.dao = new LobbyLocationDAO(this);
    }

    public void load() {
        List<LobbyLocation> localLobbies = Arrays.asList(XG7Plugins.getInstance().getJsonManager()
                .load(XG7Lobby.getInstance(), "lobbies.json", LobbyLocation[].class).join());

        dao.add(localLobbies).thenAccept(existsSomeLobby -> {
            if (!existsSomeLobby) return;


            dao.update(localLobbies);
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

        save();
    }

    public void save() {
        dao.getAll().thenAccept(lobbies -> {
            System.out.println(lobbies);
            List<LobbyLocation> localLobbies = lobbies
                    .stream().filter(lobby -> lobby.getServer().equals(XG7Lobby.getInstance().getServerInfo()))
                    .collect(Collectors.toList());
            XG7Plugins.getInstance().getJsonManager().saveJson(XG7Lobby.getInstance(), "lobbies.json", localLobbies);
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }
    public CompletableFuture<Void> saveLobby(LobbyLocation lobbyLocation) {
        if (!defaults.get("multi-lobby-locations", Boolean.class).orElse(false)) {
            XG7Plugins.getInstance().getJsonManager().saveJson(XG7Lobby.getInstance(), "lobbies.json", Collections.singletonList(lobbyLocation));
        }

        return CompletableFuture.runAsync(() -> {
            List<LobbyLocation> localLobbies = XG7Plugins.getInstance().getJsonManager()
                    .load(XG7Lobby.getInstance(), "lobbies.json", listType).join();

            if (!defaults.get("multi-lobby-locations", Boolean.class).orElse(false)) localLobbies.clear();
            else localLobbies.remove(lobbyLocation);

            localLobbies.add(lobbyLocation);
            XG7Plugins.getInstance().getJsonManager().saveJson(XG7Lobby.getInstance(), "lobbies.json", localLobbies).join();

            dao.add(Collections.singletonList(lobbyLocation)).thenAccept(exists -> {
                if (exists) return;

                dao.update(Collections.singletonList(lobbyLocation));
            }).exceptionally(e -> {
                throw new RuntimeException(e);
            }).join();
        });
    }

    public CompletableFuture<LobbyLocation> getLobby(String id) {
        return CompletableFuture.supplyAsync(() -> {
            if (lobbyLocationCache.containsKey(id).join()) {
                return lobbyLocationCache.get(id).join();
            }
            if (!defaults.get("multi-lobby-locations", Boolean.class).orElse(false)) {
                return XG7Plugins.getInstance().getJsonManager()
                        .load(XG7Lobby.getInstance(), "lobbies.json", LobbyLocation[].class)
                        .thenApply(lobbies -> Arrays.asList(lobbies).get(0)).join();
            }
            return dao.get(id).thenApply(lobbies -> lobbies.get(0)).join();
        });
    }
    public CompletableFuture<LobbyLocation> getALobbyByPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            LobbyLocation[] localLobbies = XG7Plugins.getInstance().getJsonManager()
                    .load(XG7Lobby.getInstance(), "lobbies.json", LobbyLocation[].class).join();
            if (localLobbies.length == 0) return null;


            if (!defaults.get("multi-lobby-locations", Boolean.class).orElse(false)) {
                return localLobbies[0];
            }

            for (LobbyLocation lobby : localLobbies) {
                if (lobby.getLocation().getWorldName().equals(player.getWorld().getName())) {
                    return lobby;
                }
            }
            return localLobbies[(new Random().nextInt(localLobbies.length))];
        });
    }

}
