package com.xg7plugins.xg7lobby.data.player;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.data.player.model.Warn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    public synchronized static PlayerData getPlayerData(UUID uuid) {
        if (CacheManager.getSqlCache().asMap().containsKey(uuid)) return CacheManager.getSqlCache().asMap().get(uuid);

        List<Map<String, Object>> playerDataMap = SQLHandler.select("SELECT * FROM players WHERE id = ?", uuid.toString());
        List<Map<String, Object>> mapList = SQLHandler.select("SELECT * FROM warns WHERE playerid = ? OR warns.playerid = ?", uuid.toString());

        playerDataMap.addAll(mapList);

        PlayerData data = null;

        List<Warn> warns = new ArrayList<>();

        for (Map<String, Object> row : playerDataMap) {
            if (data == null) {
                data = new PlayerData(UUID.fromString((String) row.get("id")), (Integer) row.get("ismuted") == 1, (Integer) row.get("isplayershide") == 1, (Integer) row.get("isbuildenabled") == 1, (Integer) row.get("isflying") == 1, (Integer) row.get("ispvpenabled") == 1, Long.parseLong(row.get("firstJoin").toString()), Long.parseLong(row.get("timeforunmute").toString()));
                continue;
            }

            System.out.println(data);


            if (row.get("warnid") != null) {
                Warn warn = new Warn((Integer) row.get("level"), (String) row.get("warn"), (long) row.get("whenw"), UUID.fromString((String) row.get("warnid")));
                warns.add(warn);
            }
        }

        if (data != null) {
            data.setInfractions(warns);
            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
        }

        return data;
    }

    public synchronized static PlayerData createPlayerData(UUID id) {
        PlayerData tempdata = getPlayerData(id);
        if (tempdata != null) return tempdata;

        PlayerData data = new PlayerData(id);

        SQLHandler.update("INSERT INTO players (id, isplayershide, ismuted, isbuildenabled, isflying, ispvpenabled, timeforunmute, firstJoin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", data.getId(), data.isPlayerHiding(), data.isMuted(), data.isBuildEnabled(), data.isFlying(), data.isPVPEnabled(), data.getTimeForUnmute(), data.getFirstJoin());

        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

        return data;
    }



}
