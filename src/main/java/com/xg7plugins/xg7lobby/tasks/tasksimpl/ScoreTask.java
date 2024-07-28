package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.scores.Bossbar;
import com.xg7plugins.xg7lobby.scores.ScoreBoard;
import com.xg7plugins.xg7lobby.scores.TabList;
import com.xg7plugins.xg7lobby.tasks.Task;
import org.bukkit.Bukkit;

public class ScoreTask extends Task {
    public ScoreTask() {
        super("xg7lscore", Config.getLong(ConfigType.CONFIG, "scores-update"));
    }

    @Override
    public void run() {

        Bukkit.getOnlinePlayers().stream().filter(player -> EventManager.getWorlds().contains(player.getWorld().getName())).forEach(player -> {

            if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) Bossbar.updateTitle();
            ScoreBoard.set(player);
            TabList.sendTablist(player, Config.getList(ConfigType.CONFIG, "tablist.header"), Config.getList(ConfigType.CONFIG, "tablist.footer"));

        });
    }
}
