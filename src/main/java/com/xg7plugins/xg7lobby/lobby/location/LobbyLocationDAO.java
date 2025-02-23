package com.xg7plugins.xg7lobby.lobby.location;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.database.query.Query;
import com.xg7plugins.data.database.query.QueryResult;
import com.xg7plugins.data.database.query.Transaction;
import com.xg7plugins.utils.DAO;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class LobbyLocationDAO implements DAO<String, LobbyLocation> {

    @Override
    public CompletableFuture<Boolean> add(LobbyLocation location) {
        if(location == null || location.getID() == null) return CompletableFuture.completedFuture(false);
        return XG7Plugins.getInstance().getDatabaseManager().getProcessor().exists(
                XG7Lobby.getInstance(), LobbyLocation.class, "id", location.getID()
        ).thenApply(exists -> {
            if (exists) return false;
            try {
                Transaction.createTransaction(
                        XG7Lobby.getInstance(),
                        location,
                        Transaction.Type.INSERT
                ).waitForResult();
                XG7Plugins.getInstance().getDatabaseManager().cacheEntity(XG7Lobby.getInstance(),location.getID(), location);
                return true;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    public CompletableFuture<LobbyLocation> get(String id) {
        if (id == null) return null;

        return XG7Plugins.getInstance().getDatabaseManager().containsCachedEntity(XG7Lobby.getInstance(),id).thenComposeAsync(exists -> {
            if (exists) return XG7Plugins.getInstance().getDatabaseManager().getCachedEntity(XG7Lobby.getInstance(),id);
            try {
                return CompletableFuture.completedFuture(Query.selectFrom(XG7Lobby.getInstance(), LobbyLocation.class, id).onError(Throwable::printStackTrace).waitForResult().get(LobbyLocation.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } ,XG7Plugins.taskManager().getAsyncExecutors().get("database"));


    }

    public CompletableFuture<List<LobbyLocation>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                QueryResult result = Query.selectFrom(XG7Lobby.getInstance(), "lobbies").allColumns().onError(Throwable::printStackTrace).waitForResult();
                List<LobbyLocation> locations = new ArrayList<>();
                while (result.hasNext()) {
                    locations.add(result.get(LobbyLocation.class));
                }
                return locations;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));
    }

    @Override
    public CompletableFuture<Boolean> update(LobbyLocation location) {
        if (location == null) return CompletableFuture.completedFuture(false);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Transaction.update(XG7Lobby.getInstance(), location).onError(Throwable::printStackTrace).waitForResult();
                XG7Plugins.getInstance().getDatabaseManager().cacheEntity(XG7Lobby.getInstance(),location.getID(), location);

                return true;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));

    }
}
