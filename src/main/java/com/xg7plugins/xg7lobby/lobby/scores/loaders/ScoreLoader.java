package com.xg7plugins.xg7lobby.lobby.scores.loaders;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.ScoreCondition;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ScoreLoader {

    @Getter
    protected final boolean isEnabled;
    protected final Config config;
    private String path;
    protected final long delay;
    protected final ScoreCondition condition = player -> XG7Lobby.getInstance().isInWorldEnabled(player);

    public ScoreLoader(Config config, String path) {
        this.config = config;
        this.path = path;
        this.isEnabled = config.get(path + ".enabled", Boolean.class).orElse(false);
        this.delay = config.getTime(path + ".update-time").orElse(30000L);
    }

    public abstract Score load();

    protected List<String> getList(String path) {
        return config.getList(this.path + "." + path, String.class).orElse(Collections.singletonList("Error"));
    }
    protected List<Map> getMapList(String path) {
        return config.getList(this.path + "." + path, Map.class).orElse(Collections.singletonList(Collections.singletonMap("Error", "Error")));
    }



}
