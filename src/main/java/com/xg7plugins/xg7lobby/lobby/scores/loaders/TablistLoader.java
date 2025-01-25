package com.xg7plugins.xg7lobby.lobby.scores.loaders;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.builder.ScoreBoardBuilder;
import com.xg7plugins.libs.xg7scores.builder.TablistBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TablistLoader extends ScoreLoader {
    public TablistLoader(Config config) {
        super(config, "tablist");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;

        List<List<String>> headerList = getMapList("headers").stream().map(map -> (List<String>) map.get("state")).collect(Collectors.toList());

        List<List<String>> footerList = getMapList("footers").stream().map(map -> (List<String>) map.get("state")).collect(Collectors.toList());

        return TablistBuilder.tablist("xg7lobby-tl")
                .header(headerList.stream().map(list -> String.join("\n", list)).collect(Collectors.toList()))
                .footer(footerList.stream().map(list -> String.join("\n", list)).collect(Collectors.toList()))
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
