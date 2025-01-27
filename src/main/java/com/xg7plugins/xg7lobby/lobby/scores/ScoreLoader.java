package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.ScoreCondition;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class ScoreLoader {

    @Getter
    protected final boolean isEnabled;
    protected final ScoreConfig config;
    protected final long delay;
    protected final ScoreCondition condition = player -> XG7Lobby.getInstance().isInWorldEnabled(player);

    public ScoreLoader(Config config, String path) {
        this.config = new ScoreConfig(config, path);
        this.isEnabled = config.get(path + ".enabled", Boolean.class).orElse(false);
        this.delay = config.getTime(path + ".update-time").orElse(30000L);
    }

    public abstract Score load();

    @AllArgsConstructor
    protected static class ScoreConfig {

        private Config config;
        private String path;

        public String getString(String path) {
            return config.get(this.path + "." + path, String.class).orElse("Error");
        }
        public List<String> getList(String path) {
            return config.getList(this.path + "." + path, String.class).orElse(Collections.singletonList("Error"));
        }
        public List<Map> getMapList(String path) {
            return config.getList(this.path + "." + path, Map.class).orElse(Collections.singletonList(Collections.singletonMap("Error", "Error")));
        }

        public <T> Optional<T> get(String path, Class<T> type) {
            return config.get(this.path + "." + path, type);
        }

    }



}
