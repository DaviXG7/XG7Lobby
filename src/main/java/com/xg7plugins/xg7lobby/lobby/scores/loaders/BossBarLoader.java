package com.xg7plugins.xg7lobby.lobby.scores.loaders;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.libs.xg7scores.builder.BossBarBuilder;
import com.xg7plugins.libs.xg7scores.scores.bossbar.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class BossBarLoader extends ScoreLoader {

    public BossBarLoader(Config config) {
        super(config, "bossbar");
    }

    @Override
    public Score load() {
        if (!isEnabled) return null;

        if (XG7Plugins.getMinecraftVersion() < 9) {
            boolean isPublic = config.get("bossbar.public", Boolean.class).orElse(false);

            BarColor color = config.get("bossbar.color", BarColor.class).orElse(BarColor.WHITE);
            BarStyle style = config.get("bossbar.style", BarStyle.class).orElse(BarStyle.SOLID);

            return BossBarBuilder.bossBar("xg7lobby-bb")
                    .title(getList("title"))
                    .progress(config.get("bossbar.progress", Float.class).orElse(1.0f))
                    .color(color)
                    .style(style)
                    .isPublic(isPublic)
                    .delay(delay)
                    .condition(condition)
                    .build(XG7Plugins.getInstance());
        }

        return BossBarBuilder.bossBar("xg7lobby-bb")
                .title(getList("title"))
                .progress(config.get("bossbar.progress", Float.class).orElse(1.0f))
                .delay(delay)
                .condition(condition)
                .build(XG7Plugins.getInstance());

    }
}
