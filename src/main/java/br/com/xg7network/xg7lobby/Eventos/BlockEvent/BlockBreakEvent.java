package br.com.xg7network.xg7lobby.Eventos.BlockEvent;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class BlockBreakEvent implements Listener {

    private XG7Lobby pl;

    public BlockBreakEvent (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onBlockBreak (org.bukkit.event.block.BlockBreakEvent e) {
        if (pl.getConfig().getStringList("mundos_ativados").contains(e.getPlayer().getWorld().getName())) {
            if (!pl.getConfig().getBoolean("QuebrarBlocos")) {
                if (!e.getPlayer().hasPermission("xg7lobby.block.break")) {
                    if (!e.getPlayer().hasPermission("xg7lobby.admin")) {
                        e.setCancelled(true);
                        if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagens_permissão")) {
                            action.mandarAction(e.getPlayer(), mensagem.getMessage().getString("mensagens.permissao_quebrar"));
                        }
                    }
                }
            }
        }
    }
}
