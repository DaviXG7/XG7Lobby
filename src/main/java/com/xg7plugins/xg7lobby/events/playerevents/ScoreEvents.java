package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import com.xg7plugins.xg7lobby.scores.Bossbar;
import com.xg7plugins.xg7lobby.scores.ScoreBoard;
import com.xg7plugins.xg7lobby.scores.TabList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreEvents implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "tablist.enabled") || Config.getBoolean(ConfigType.CONFIG, "scoreboard.enabled") || Config.getBoolean(ConfigType.CONFIG, "bossbar.enabled");
    }

    @Override
    public void onWorldJoin(Player player) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1]) >= 9) Bossbar.addPlayer(player);
        ScoreBoard.set(player);
        TabList.sendTablist(player, Config.getList(ConfigType.CONFIG, "tablist.header"), Config.getList(ConfigType.CONFIG, "tablist.footer"));
    }

    @Override
    public void onWorldLeave(Player player) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1]) >= 9) Bossbar.removePlayer(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        TabList.sendTablist(player, null, null);
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1]) >= 9) Bossbar.addPlayer(event.getPlayer());
        ScoreBoard.set(event.getPlayer());
        TabList.sendTablist(event.getPlayer(), Config.getList(ConfigType.CONFIG, "tablist.header"), Config.getList(ConfigType.CONFIG, "tablist.footer"));
    }

    @Override
    public void onQuit(PlayerQuitEvent event) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1]) >= 9) Bossbar.removePlayer(event.getPlayer());
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        TabList.sendTablist(event.getPlayer(), null, null);
    }
}
