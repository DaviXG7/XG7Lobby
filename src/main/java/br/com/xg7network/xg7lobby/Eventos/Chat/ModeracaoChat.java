package br.com.xg7network.xg7lobby.Eventos.Chat;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class ModeracaoChat implements Listener {

    private XG7Lobby pl;

    public ModeracaoChat (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onBadWords(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getBoolean("BloquearPalavrões.ativado")) {
            String mensagemSuspeita = e.getMessage();
            if (pl.getConfig().getStringList("BloquearPalavrões.palavras_bloqueadas").contains(mensagemSuspeita)){
                e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.MensagemBloqueada").replace("&", "§")));
                } else {
                    p.sendMessage(mensagem.getMessage().getString("mensagens.MensagemBloqueada").replace("&", "§"));
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getBoolean("BloquearComandos.ativado")) {
            String comandobloqueado = e.getMessage();
            if (pl.getConfig().getStringList("BloquearComandos.comandos_bloqueados").contains(comandobloqueado)){
                e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.MensagemBloqueada").replace("&", "§")));
                } else {
                    p.sendMessage(mensagem.getMessage().getString("mensagens.MensagemBloqueada").replace("&", "§"));
                }
            }
        }
    }
}
