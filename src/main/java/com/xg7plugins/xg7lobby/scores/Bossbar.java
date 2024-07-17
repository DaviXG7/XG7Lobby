package com.xg7plugins.xg7lobby.scores;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bossbar {

    private static final Map<UUID, BossBar> bossBars = new HashMap<>();

    public static void updateTitle() {

        for (Map.Entry<UUID, BossBar> boss : bossBars.entrySet()) {
            boss.getValue().setTitle(Text.getFormatedText(Bukkit.getPlayer(boss.getKey()), Config.getString(ConfigType.CONFIG, "bossbar.title")));
        }

    }

    public static void addPlayer(Player player) {
        BossBar bossBar = Bukkit.createBossBar((Text.getFormatedText(Bukkit.getPlayer(player.getUniqueId()), Config.getString(ConfigType.CONFIG, "bossbar.title"))),
                BarColor.valueOf(Config.getString(ConfigType.CONFIG, "bossbar.color")),
                BarStyle.valueOf(Config.getString(ConfigType.CONFIG, "bossbar.style")));

        bossBar.setProgress(Config.getDouble(ConfigType.CONFIG, "bossbar.progress"));
        bossBar.addPlayer(player);

        bossBars.put(player.getUniqueId(), bossBar);
    }
    public static void removePlayer(Player player) {
        bossBars.get(player.getUniqueId()).removePlayer(player);
        bossBars.remove(player.getUniqueId());
    }
}
