package com.xg7plugins.xg7lobby.lobby.scores.loaders;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.builder.ScoreBoardBuilder;
import com.xg7plugins.libs.xg7scores.builder.TablistBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.util.List;
import java.util.Map;

public class TablistLoader extends ScoreLoader {
    public TablistLoader(Config config) {
        super(config, "tablist");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;

        List<Map> headers = getMapList("headers");

        List<List<String>> headerList = headers.stream().map(map -> {
            List<String> list = (List<String>) map.get("header");
            return list;
        }).toList();


        return TablistBuilder.tablist("xg7lobby-tl")
                .(headers)
                .footers(footers)
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
