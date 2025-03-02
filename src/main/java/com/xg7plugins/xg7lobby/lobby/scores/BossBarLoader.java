package com.xg7plugins.xg7lobby.lobby.scores;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7scores.Score;
import com.xg7plugins.modules.xg7scores.builder.BossBarBuilder;
import com.xg7plugins.server.MinecraftVersion;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class BossBarLoader extends ScoreLoader {

    public BossBarLoader(Config config) {
        super(config, "bossbar");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;

        if (MinecraftVersion.isNewerThan(8)) {
            boolean isPublic = config.get("public", Boolean.class).orElse(false);

            BarColor color = config.get("color", BarColor.class).orElse(BarColor.WHITE);
            BarStyle style = config.get("style", BarStyle.class).orElse(BarStyle.SOLID);

            return BossBarBuilder.bossBar("xg7lobby-bb")
                    .title(config.getList("title"))
                    .progress(config.get("progress", Float.class).orElse(100f))
                    .color(color)
                    .style(style)
                    .isPublic(isPublic)
                    .delay(delay)
                    .condition(condition)
                    .build(XG7Plugins.getInstance());
        }

        return BossBarBuilder.bossBar("xg7lobby-bb")
                .title(config.getList("title"))
                .progress(config.get("progress", Float.class).orElse(100f))
                .delay(delay)
                .condition(condition)
                .build(XG7Plugins.getInstance());

    }
}
