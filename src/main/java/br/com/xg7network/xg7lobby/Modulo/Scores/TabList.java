package br.com.xg7network.xg7lobby.Modulo.Scores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import br.com.xg7network.xg7lobby.Modulo.Module;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static br.com.xg7network.xg7lobby.XG7Lobby.score;

public class TabList extends Module {

    int tabl;

    String rodape, cabecalho;

    public TabList(XG7Lobby plugin) {
        super(plugin);
    }
    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Carregando Tablist...");

        rodape = String.join("\n", score.getScore().getStringList("tablist.rodapé"));
        cabecalho = String.join("\n", score.getScore().getStringList("tablist.cabeçalho"));

            tabl = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                if (score.getScore().getBoolean("tablist.ativado")) {
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                        cabecalho = PlaceholderAPI.setPlaceholders(p, cabecalho);
                        rodape = PlaceholderAPI.setPlaceholders(p, rodape);
                        p.setPlayerListHeader(cabecalho.replace("&", "§"));
                        p.setPlayerListFooter(rodape.replace("&", "§"));
                    } else {
                        p.setPlayerListHeader(cabecalho.replace("&", "§"));
                        p.setPlayerListFooter(rodape.replace("&", "§"));
                    }
                }
            }
        }, 0, score.getScore().getInt("atualizacao_das_scores")).getTaskId();
    }

    @Override
    public void onDisable() {

        Bukkit.getScheduler().cancelTask(tabl);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Descarregando Tablist...");

    }

}
