package com.xg7plugins.xg7lobby.lobby.scores.loaders;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.builder.ScoreBoardBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

import java.util.Collections;

public class ScoreboardLoader extends ScoreLoader {
    public ScoreboardLoader(Config config) {
        super(config, "scoreboard");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;
        return ScoreBoardBuilder.scoreBoard("xg7lobby-sb")
                .title(getList("title"))
                .lines(getList("lines"))
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
