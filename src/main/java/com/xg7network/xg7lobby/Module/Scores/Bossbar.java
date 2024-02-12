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
    private Player player;


    public Bossbar(Player player) {
        this.player = player;
    }

    public void createBossBar() {
        boss = Bukkit.createBossBar(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.title"), player),
                BarColor.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.color")),
                BarStyle.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.style")));

        boss.setProgress(configManager.getConfig(ConfigType.CONFIG).getDouble("scores.bossbar.progress"));
        boss.addPlayer(player);
    }

    public void updateTitle(String title) {

        boss.setTitle(TextUtil.get(title, player));

    }

    public void removeBossBar() {
        boss.removePlayer(player);
    }



}
