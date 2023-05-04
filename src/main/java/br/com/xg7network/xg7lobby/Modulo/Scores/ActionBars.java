package br.com.xg7network.xg7lobby.Modulo.Scores;

import br.com.xg7network.xg7lobby.XG7Lobby;

import br.com.xg7network.xg7lobby.Modulo.Module;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static br.com.xg7network.xg7lobby.XG7Lobby.score;

public class ActionBars extends Module {

    int actionbar;

    public ActionBars(XG7Lobby plugin) {
         super(plugin);
    }

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Carregando ActionBar...");



            actionbar =Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (score.getScore().getBoolean("actionbar.ativado")) {
                        String action = score.getScore().getString("actionbar.texto").replace("&", "§");
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            action = PlaceholderAPI.setPlaceholders(p, action);
                        }
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(action));
                    }
                }
        }, 0, score.getScore().getInt("atualizacao_das_scores")).getTaskId();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Descarregando ActionBars...");
        Bukkit.getScheduler().cancelTask(actionbar);

    }
}
