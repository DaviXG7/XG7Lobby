package com.xg7network.xg7lobby.Module.Scores;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Bossbar implements Listener {


    private static BossBar boss;


    public Bossbar() {
    }

    public void createBossBar() {
        String bossTitulo = new TextUtil(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.title")).get();
        boss = Bukkit.createBossBar(bossTitulo,
                BarColor.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.color")),
                BarStyle.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.style")));

        boss.setProgress(configManager.getConfig(ConfigType.CONFIG).getDouble("scores.bossbar.progress"));
    }

    public boolean containsBossBar(Player player) {
        return boss.getPlayers().contains(player);
    }

    public void setBossBar(Player player) {
        if (!boss.getPlayers().contains(player)) {
            boss.addPlayer(player);
        }
    }

    public void removeBossBar(Player player) {
        boss.removePlayer(player);
    }



}
