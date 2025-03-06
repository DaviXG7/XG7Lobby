package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7scores.Score;
import com.xg7plugins.modules.xg7scores.builder.ScoreBoardBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;

public class ScoreboardLoader extends ScoreLoader {

    private final boolean sidebarEnabled;
    private final boolean healthDisplayEnabled;
    private final ScoreConfig belowNameConfig;

    public ScoreboardLoader(Config config) {
        super(config, "scoreboard");

        this.sidebarEnabled = config.get("scoreboard.enabled", Boolean.class).orElse(false);
        this.healthDisplayEnabled = config.get("health-display.enabled", Boolean.class).orElse(false);

        this.isEnabled = sidebarEnabled || healthDisplayEnabled;

        this.belowNameConfig = new ScoreConfig(config, "health-display");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;
        return ScoreBoardBuilder.scoreBoard("xg7lobby-sb")
                .title(config.getList("title"))
                .lines(config.getList("lines"))
                .delay(delay)
                .condition(condition)
                .allowSideBar(sidebarEnabled)
                .allowHealthDisplay(healthDisplayEnabled)
                .healthDisplaySuffix(belowNameConfig.getString("suffix"))
                .build(XG7Lobby.getInstance());
    }
}
