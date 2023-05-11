package br.com.xg7network.xg7lobby.Eventos.JoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.centralizar;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Mensagem implements Listener {

    @EventHandler
    public void onJoinMessage(PlayerJoinEvent pje) {
        if (mensagem.getMessage().getBoolean("mensagens.ativar_EntrarMensagem")) {
            String joinMessage = mensagem.getMessage().getString("mensagens.QuandoOJogadorEntrarMensagem").replace("&", "§");
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    joinMessage = PlaceholderAPI.setPlaceholders(p, joinMessage);
                }
            }
            pje.setJoinMessage(joinMessage);
        } else {
            pje.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onJoinPessoalMessage(PlayerJoinEvent pje) {
        if (mensagem.getMessage().getBoolean("mensagens.ativar_EntrarMensagemPessoal")) {
            List<String> joinPessoalMessage = mensagem.getMessage().getStringList("mensagens.QuandoOJogadorEntrarMensagemPessoal");
            String s = centralizar.centralizarTexto(joinPessoalMessage);
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    s = PlaceholderAPI.setPlaceholders(p, s);
                }
            }
            pje.getPlayer().sendMessage(s.replace("&", "§"));

        }
    }
    @EventHandler
    public void onJoinTitle (PlayerJoinEvent pje) {
        if (mensagem.getMessage().getBoolean("mensagens.ativar_EntrarTitulo")) {
            String joinTitle = mensagem.getMessage().getString("mensagens.QuandoOJogadorEntrarTitulo").replace("&", "§");
            String joinSubTitle = mensagem.getMessage().getString("mensagens.QuandoOJogadorEntrarSubTitulo").replace("&", "§");
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    joinTitle = PlaceholderAPI.setPlaceholders(p, joinTitle);
                    joinSubTitle = PlaceholderAPI.setPlaceholders(p, joinSubTitle);
                }
            }
            pje.getPlayer().sendTitle(joinTitle, joinSubTitle);
        }
    }

}