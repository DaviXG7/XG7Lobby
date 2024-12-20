package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.data.database.EntityProcessor;
import com.xg7plugins.data.database.Query;
import com.xg7plugins.utils.DAO;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.model.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlayerDAO extends DAO<UUID, PlayerData> {

    public void add(PlayerData playerData) throws ExecutionException, InterruptedException {
        if(playerData == null || playerData.getPlayerUUID() == null) return;
        if (EntityProcessor.exists(XG7Lobby.getInstance(),PlayerData.class, "playerUUID", playerData.getPlayerUUID())) return;

        EntityProcessor.insetEntity(XG7Lobby.getInstance(), playerData);

        XG7Lobby.getInstance().getLog().info("PlayerData added to database: " + playerData.getPlayerUUID());
    }

    public CompletableFuture<PlayerData> get(UUID uuid) {
        if (uuid == null) return CompletableFuture.completedFuture(null);

        return Query.getEntity(XG7Lobby.getInstance(), "SELECT * FROM PlayerData INNER JOIN Warn ON PlayerData.playerUUID = Warn.playerUUID WHERE PlayerData.playerUUID = ?", uuid, PlayerData.class);
    }
    public CompletableFuture<Void> update(PlayerData playerData) {
        if (playerData == null) return null;

        return CompletableFuture.runAsync(() -> Query.update(XG7Lobby.getInstance(), playerData));
    }


}
