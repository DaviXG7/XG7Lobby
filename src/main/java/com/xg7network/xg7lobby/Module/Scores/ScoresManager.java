package com.xg7network.xg7lobby.Module.Scores;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.xg7network.xg7lobby.XG7Lobby.*;

public class ScoresManager extends Module implements Listener {
    private static Bossbar bossbar;

    private static HashMap<UUID, Score> scores = new HashMap<>();

    private static boolean boss = configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.bossbar.enabled");

    private XG7Lobby pl;

    public ScoresManager(XG7Lobby pl) {
        super(pl);
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                scores.remove(player.getUniqueId());
                new Tablist(player, null, null).sendTabList();
                if (boss && bossbar != null) bossbar.removeBossBar(player);

            } else {
                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.scoreboard.enabled")) {
                    Score scoreBoard = new Score(event.getPlayer());

                    scores.put(event.getPlayer().getUniqueId(), scoreBoard);
                }
            }
        }, 15);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.scoreboard.enabled")) {

            Score scoreBoard = new Score(event.getPlayer());
            scores.put(event.getPlayer().getUniqueId(), scoreBoard);

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (boss && bossbar != null) bossbar.removeBossBar(event.getPlayer());
    }

    @Override
    public void onEnable() {
        bossbar = new Bossbar();
        if (boss) {
            try {
                Class.forName("org.bukkit.boss.BarColor");
                bossbar.createBossBar();
            } catch (ClassNotFoundException e) {
                boss = false;
                System.out.println(prefix + ChatColor.RED + "Your version do not suports BossBars! Maybe in the future it will be supported so wait until the next update");
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.scoreboard.enabled")) {

                Score scoreBoard = new Score(p);
                scores.put(p.getUniqueId(), scoreBoard);

            }
        }
        Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {

            for (Player p : Bukkit.getOnlinePlayers()) {

                if (Players.getPlayers().containsKey(p.getUniqueId())) {
                    if (configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.scoreboard.enabled")) {

                        if (scores.containsKey(p.getUniqueId())) scores.get(p.getUniqueId()).updateScore();
                    }
                    if (configManager.getConfig(ConfigType.CONFIG).getBoolean("scores.tablist.enabled")) {
                        new Tablist(p, configManager.getConfig(ConfigType.CONFIG).getStringList("scores.tablist.header"),
                                configManager.getConfig(ConfigType.CONFIG).getStringList("scores.tablist.footer"))
                                .sendTabList();
                    }

                    if (boss) {
                        bossbar.updateTitle(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("scores.bossbar.title")));
                        if (!bossbar.containsBossBar(p)) bossbar.setBossBar(p);
                    }

                }

            }
        }, 0L, configManager.getConfig(ConfigType.CONFIG).getInt("scores.update"));


    }

    @Override
    public void onDisable() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (boss) bossbar.removeBossBar(p);
            scores.remove(p.getUniqueId());
        }


    }



}