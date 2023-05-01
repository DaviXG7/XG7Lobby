package br.com.xg7network.xg7lobby.Modulo.Scores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import br.com.xg7network.xg7lobby.Modulo.Module;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.event.Listener;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.score;

public class ScoreBoard extends Module implements Listener {

    int scoreb;
    org.bukkit.scoreboard.Scoreboard board;

    public ScoreBoard(XG7Lobby plugin) {
        super(plugin);
    }
    int scorePri = 0;

    public void Score(Player p){
        if (scorePri == 0) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Criando ScoreBoard...");
            scorePri++;
        }
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(score.getScore().getString("scoreboard.titulo").replace("&", "§"));
        List<String> linhas = score.getScore().getStringList("scoreboard.linhas");
        int size = linhas.size() + 1;
        for (String s : linhas) {
            size--;
            Team linha = board.registerNewTeam("linhas" + size);

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                s = PlaceholderAPI.setPlaceholders(p, s);
            }
            s = s.replace("&", "§");
            linha.addEntry(s);
            obj.getScore(s).setScore(size);
        }
            p.setScoreboard(board);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Carregando ScoreBoard...");

        if (score.getScore().getBoolean("scoreboard.ativado")) {

        scoreb = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    this.Score(p);
                    p.setScoreboard(board);
                }
            }, 0, score.getScore().getInt("atualizacao_das_scores")).getTaskId();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Descarregando ScoreBoard...");
        Bukkit.getScheduler().cancelTask(scoreb);
        scorePri = 0;
    }
}
