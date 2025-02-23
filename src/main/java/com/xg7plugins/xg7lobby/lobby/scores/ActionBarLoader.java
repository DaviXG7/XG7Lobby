package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7scores.Score;
import com.xg7plugins.modules.xg7scores.builder.ActionBarBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

public class ActionBarLoader extends ScoreLoader {
    public ActionBarLoader(Config config) {
        super(config, "actionbar");
    }

    @Override
    public Score load() {

        if (!isEnabled) return null;

        return ActionBarBuilder.actionBar("xg7lobby-ab")
                .text(config.getList("text"))
                .delay(delay)
                .condition(condition)
                .build(XG7Lobby.getInstance());
    }
}
