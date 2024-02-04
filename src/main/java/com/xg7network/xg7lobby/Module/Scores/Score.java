package com.xg7network.xg7lobby.Module.Scores;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private static final String title = configManager.getConfig(ConfigType.CONFIG).getString("scores.scoreboard.title");

    private static final List<String> lines = configManager.getConfig(ConfigType.CONFIG).getStringList("scores.scoreboard.lines");

    public Score(Player player) {

        this.player = player;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String title2 = TextUtil.get(title, player);
        objective.setDisplayName(title2);
        int size = lines.size() + 1;


        for (String line : lines) {
            --size;
            Team linha = this.scoreboard.registerNewTeam("linha: " + size);
            String text = TextUtil.get(line, player);
            StringBuilder entry = new StringBuilder();
            for (int i = 0; i < size; i++) {
                entry.append("§r");
            }
            linha.addEntry(entry.toString());

            linha.setPrefix(text.split(" /// ")[0]);
            try {
                linha.setSuffix(text.split(" /// ")[1]);
            } catch (Exception ignored) {
            }

            objective.getScore(entry.toString()).setScore(size);

        }

        this.player.setScoreboard(scoreboard);

    }


    public void updateScore() {
        int size = lines.size() + 1;
        for (String line : lines) {
            size--;
            Team linha = this.scoreboard.getTeam("linha: " + size);
            String text = TextUtil.get(line, player);
            linha.setPrefix(text.split(" /// ")[0]);
            try {
                linha.setSuffix(text.split(" /// ")[1]);
            } catch (Exception ignored) {
                linha.setSuffix("");
            }


        }
        this.player.setScoreboard(scoreboard);
    }

}