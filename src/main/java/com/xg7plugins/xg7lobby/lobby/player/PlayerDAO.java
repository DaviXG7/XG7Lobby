package com.xg7plugins.xg7lobby.lobby.player;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.database.query.Query;
import com.xg7plugins.data.database.query.Transaction;
import com.xg7plugins.utils.DAO;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDAO implements DAO<UUID, LobbyPlayer> {

    @Override
    public CompletableFuture<Boolean> add(LobbyPlayer player) {
        if(player == null || player.getPlayerUUID() == null) return CompletableFuture.completedFuture(false);
        return XG7Plugins.getInstance().getDatabaseManager().getProcessor().exists(
                XG7Lobby.getInstance(), LobbyPlayer.class, "playerUUID", player.getPlayerUUID()
        ).thenApply(exists -> {
            if (exists) return false;
            try {
                Transaction.createTransaction(
                        XG7Lobby.getInstance(),
                        player,
                        Transaction.Type.INSERT
                ).waitForResult();
                XG7Plugins.getInstance().getDatabaseManager().cacheEntity(XG7Lobby.getInstance(),player.getPlayerUUID().toString(), player);
                return true;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    public CompletableFuture<LobbyPlayer> get(UUID uuid) {
        if (uuid == null) return null;

        return XG7Plugins.getInstance().getDatabaseManager().containsCachedEntity(XG7Lobby.getInstance(),uuid.toString()).thenComposeAsync(exists -> {
            if (exists) return XG7Plugins.getInstance().getDatabaseManager().getCachedEntity(XG7Lobby.getInstance(),uuid.toString());
            try {
                return CompletableFuture.completedFuture(Query.selectFrom(XG7Lobby.getInstance(), LobbyPlayer.class, uuid).onError(Throwable::printStackTrace).waitForResult().get(LobbyPlayer.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } ,XG7Plugins.taskManager().getAsyncExecutors().get("database"));


    }

    @Override
    public CompletableFuture<Boolean> update(LobbyPlayer player) {
        if (player == null) return CompletableFuture.completedFuture(false);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Transaction.update(XG7Lobby.getInstance(), player).onError(Throwable::printStackTrace).waitForResult();
                XG7Plugins.getInstance().getDatabaseManager().cacheEntity(XG7Lobby.getInstance(),player.getPlayerUUID().toString(), player);

                return true;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));

    }

}
