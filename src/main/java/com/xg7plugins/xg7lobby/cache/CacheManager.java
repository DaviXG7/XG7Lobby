package com.xg7plugins.xg7lobby.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.utils.Log;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CacheManager {
    @Getter
    private static Cache<UUID, Long> selectorCache;
    @Getter
    private static Cache<UUID, Long> lobbyCache;
    @Getter
    private static Cache<UUID, PlayerData> sqlCache;

    public static void init() {
        selectorCache = CacheBuilder.newBuilder().expireAfterWrite(Config.getLong(ConfigType.SELECTORS, "cooldown"), TimeUnit.SECONDS).build();
        lobbyCache = CacheBuilder.newBuilder().expireAfterWrite(Config.getLong(ConfigType.CONFIG, "lobby.before-tp.cooldown-for-tp"), TimeUnit.SECONDS).build();
        sqlCache = CacheBuilder.newBuilder().expireAfterWrite(Config.getLong(ConfigType.CONFIG, "sql-cache-expires"), TimeUnit.MINUTES).build();
        Log.loading("Loaded!");
    }

    public static void put(UUID id, CacheType type, PlayerData data) {
        switch (type) {
            case LOBBY_COOLDOWN:
                lobbyCache.put(id, System.currentTimeMillis() + 5000);
                return;
            case SELECTOR_COOLDOWN:
                selectorCache.put(id, System.currentTimeMillis() + 5000);
                return;
            case SQL_QUERY:
                sqlCache.put(data.getId(), data);
        }
    }

    public static void remove(UUID id, CacheType type) {
        switch (type) {
            case LOBBY_COOLDOWN:
                lobbyCache.invalidate(id);
                lobbyCache.cleanUp();
                return;
            case SELECTOR_COOLDOWN:
                selectorCache.invalidate(id);
                selectorCache.cleanUp();
                return;
            case SQL_QUERY:
                sqlCache.invalidate(id);
                sqlCache.cleanUp();
        }
    }

    public static void reloadAll() {
        Log.info("Cleaning cache...");
        selectorCache.invalidateAll();
        lobbyCache.invalidateAll();
        sqlCache.invalidateAll();
        sqlCache.cleanUp();
        lobbyCache.cleanUp();
        selectorCache.cleanUp();
        Log.info("Loaded");
    }

}
