package br.com.xg7network.xg7lobby.Modulo.Scores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import br.com.xg7network.xg7lobby.Modulo.Module;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;


import static br.com.xg7network.xg7lobby.XG7Lobby.score;

public class BossBar extends Module {

    org.bukkit.boss.BossBar boss;

    public BossBar(XG7Lobby plugin) {
        super(plugin);
    }
    int bossbar;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Criando BossBar...");
        String bossTitulo = ChatColor.translateAlternateColorCodes('&', score.getScore().getString("bossbar.título"));
        boss = Bukkit.createBossBar(bossTitulo,
                BarColor.valueOf(score.getScore().getString("bossbar.cor")),
                BarStyle.valueOf(score.getScore().getString("bossbar.estilo")));

        boss.setProgress(score.getScore().getDouble("bossbar.progresso"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Carregando BossBar...");


            bossbar = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {

                if (score.getScore().getBoolean("bossbar.ativado")) {
                    if (score.getScore().getBoolean("bossbar.ativado")) {
                        boss.addPlayer(p);
                    }
                }
            }

        }, 0, score.getScore().getInt("atualizacao_das_scores")).getTaskId();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Descarregando BossBar...");
        Bukkit.getScheduler().cancelTask(this.bossbar);
        for (Player p : Bukkit.getOnlinePlayers()) {
            boss.removePlayer(p);
        }
    }

}
