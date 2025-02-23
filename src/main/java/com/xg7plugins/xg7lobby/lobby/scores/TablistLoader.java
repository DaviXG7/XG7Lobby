package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.data.config.Config;

import com.xg7plugins.modules.xg7scores.Score;
import com.xg7plugins.modules.xg7scores.builder.TablistBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.util.List;
import java.util.stream.Collectors;

public class TablistLoader extends ScoreLoader {
    public TablistLoader(Config config) {
        super(config, "tablist");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;

        List<List<String>> headerList = config.getMapList("headers").stream().map(map -> (List<String>) map.get("state")).collect(Collectors.toList());

        List<List<String>> footerList = config.getMapList("footers").stream().map(map -> (List<String>) map.get("state")).collect(Collectors.toList());

        return TablistBuilder.tablist("xg7lobby-tl")
                .header(headerList.stream().map(list -> String.join("\n", list)).collect(Collectors.toList()))
                .footer(footerList.stream().map(list -> String.join("\n", list)).collect(Collectors.toList()))
                .playerPrefix(config.getString("custom-player-prefix"))
                .playerSuffix(config.getString("custom-player-suffix"))
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
