package com.xg7network.xg7lobby.Module.Scores;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Score {

    private Player player;

    private Scoreboard scoreboard;


    public Score(Player player) {

        this.player = player;

    }

    public void createScore() {

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(new TextUtil(configManager.getConfig(ConfigType.CONFIG).getString("scores.scoreboard.title")).get(player));
        List<String> linhas = configManager.getConfig(ConfigType.CONFIG).getStringList("scores.scoreboard.lines");
        int size = linhas.size() + 1;

        for (String s : linhas) {
            --size;
            Team linha = board.registerNewTeam("linhas" + size);
            s = s.replace("PLAYER", player.getName());

            s = new TextUtil(s).get(player);

            linha.addEntry(s);
            obj.getScore(s).setScore(size);
        }

        this.scoreboard = board;
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard);
    }


}
