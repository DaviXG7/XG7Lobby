package br.com.xg7network.xg7lobby.Eventos.BlockEvent;

import br.com.xg7network.xg7lobby.XG7Lobby;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class BlockInteractEvent implements Listener {

    private XG7Lobby pl;

    public BlockInteractEvent(XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos-ativados").contains(p.getWorld().getName())) {
            if (pl.getConfig().getBoolean("InteragirComBlocos")) {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().isInteractable()) {
                    e.setCancelled(true);
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_quebrar").replace("&", "§")));
                    } else {
                        e.getPlayer().sendMessage(mensagem.getMessage().getString("mensagens.permissao_interagir").replace("&", "§"));
                    }
                }
            }
        }
    }
}
