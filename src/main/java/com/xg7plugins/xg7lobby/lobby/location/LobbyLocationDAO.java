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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@AllArgsConstructor
public class LobbyLocationDAO implements DAO<String, List<LobbyLocation>> {

    private LobbyManager lobbyManager;

    @Override
    public CompletableFuture<Boolean> add(List<LobbyLocation> lobbyLocation) {
        if (lobbyLocation == null) return CompletableFuture.completedFuture(false);

        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean doesntExistSomeLobby = true;
                for (LobbyLocation location : lobbyLocation) {
                    if (lobbyManager.getLobbyLocationCache().containsKey(location.getId()).join()) {
                        doesntExistSomeLobby = false;
                        continue;
                    }
                    if (
                            XG7Plugins.getInstance().getDatabaseManager().getProcessor().exists(
                                    XG7Lobby.getInstance(),
                                    LobbyLocation.class,
                                    "id",
                                    location.getId()
                            ).join()
                    ) {
                        doesntExistSomeLobby = false;
                        continue;
                    }
                    Transaction.createTransaction(
                            XG7Lobby.getInstance(),
                            location,
                            Transaction.Type.INSERT
                    ).waitForResult();
                }
                return doesntExistSomeLobby;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));

    }

    @Override
    public CompletableFuture<List<LobbyLocation>> get(String id) {
        if (id == null) return null;

        return lobbyManager.getLobbyLocationCache().containsKey(id).thenComposeAsync(exists -> {
            if (exists) return lobbyManager.getLobbyLocationCache().get(id).thenApply(Collections::singletonList);

            try {

                LobbyLocation lobbyLocation = Query.selectFrom(XG7Lobby.getInstance(), LobbyLocation.class, id).onError(Throwable::printStackTrace).waitForResult().get(LobbyLocation.class);

                lobbyManager.getLobbyLocationCache().put(lobbyLocation.getId(), lobbyLocation);

                return CompletableFuture.completedFuture(Collections.singletonList(lobbyLocation));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } ,XG7Plugins.taskManager().getAsyncExecutors().get("database"));
    }

    public CompletableFuture<List<LobbyLocation>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                QueryResult result = Query.selectFrom(XG7Lobby.getInstance(), "lobbies").allColumns().waitForResult();
                List<LobbyLocation> lobbies = new ArrayList<>();
                while (result.hasNext()) {
                    try {
                        LobbyLocation lobbyLocation = result.get(LobbyLocation.class);
                        lobbies.add(lobbyLocation);
                        lobbyManager.getLobbyLocationCache().put(lobbyLocation.getId(), lobbyLocation);

                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }

                return lobbies;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));
    }

    @Override
    public CompletableFuture<Boolean> update(List<LobbyLocation> lobbyLocation) {
        if (lobbyLocation == null) return CompletableFuture.completedFuture(false);

        return CompletableFuture.supplyAsync(() -> {
            for (LobbyLocation location : lobbyLocation) {
                try {
                    Transaction.update(XG7Lobby.getInstance(), location).onError(Throwable::printStackTrace).waitForResult();
                    lobbyManager.getLobbyLocationCache().put(location.getId(), location);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                         InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }, XG7Plugins.taskManager().getAsyncExecutors().get("database"));
    }
}
