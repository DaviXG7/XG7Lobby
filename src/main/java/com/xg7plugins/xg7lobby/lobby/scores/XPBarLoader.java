package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7scores.Score;
import com.xg7plugins.modules.xg7scores.builder.XPBarBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

public class XPBarLoader extends ScoreLoader {
    public XPBarLoader(Config config) {
        super(config, "xp-bar");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;
        return XPBarBuilder.XPBar("xg7lobby-xp")
                .setLevels(config.getList("levels"))
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
