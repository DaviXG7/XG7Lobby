package com.xg7plugins.xg7lobby.scores;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScoreBoard {

    public static void set(Player player) {

        Scoreboard scoreboard = player.getScoreboard();

        if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        Objective objective = scoreboard.getObjective("xg7lscore");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("xg7lscore", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        objective.setDisplayName(Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "scoreboard.title")));

        int index = Config.getList(ConfigType.CONFIG, "scoreboard.lines").size() + 1;

        for (String s : Config.getList(ConfigType.CONFIG, "scoreboard.lines")) {
            index--;

            String entry = IntStream.range(0, index).mapToObj(i -> "§r").collect(Collectors.joining());

            Team team = scoreboard.getTeam("xg7lscore:Team=" + index) != null ? scoreboard.getTeam("xg7lscore:Team=" + index) : scoreboard.registerNewTeam("xg7lscore:Team=" + index);

            s = Text.getFormatedText(player, s);

            String prefix = s.substring(0, Math.min(s.length(), 16));
            String suffix = null;
            if (s.length() > 16) {
                suffix = s.substring(16, Math.min(s.length(), 32));
                if (prefix.charAt(15) == '§') {
                    prefix = prefix.substring(0, 15);
                    suffix = "§" + suffix;
                }
                suffix = ChatColor.getLastColors(prefix) + suffix;
            }

            team.setPrefix(prefix);
            if (suffix != null) team.setSuffix(suffix);

            if (!team.getEntries().contains(entry)) {
                team.addEntry(entry);
                objective.getScore(entry).setScore(index);
            }
        }

        player.setScoreboard(scoreboard);
    }

}
