package br.com.xg7network.xg7lobby.Eventos.QuitEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Mensagem implements Listener {
    @EventHandler
    public void onQuitMessage(PlayerQuitEvent pqe) {
        if (mensagem.getMessage().getBoolean("mensagens.ativar_SairMensagem")) {
            String leaveMessage = mensagem.getMessage().getString("mensagens.QuandoOJogadorSairMensagem").replace("&", "§");
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    leaveMessage = PlaceholderAPI.setPlaceholders(p, leaveMessage);
                }
            }
            pqe.getPlayer().sendMessage(leaveMessage);
        } else {
            pqe.setQuitMessage(null);
        }
    }
}
