package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class PlayerDropPickupEvent implements Listener {

    private XG7Lobby pl;

    public PlayerDropPickupEvent (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void PlayerDropEvent (PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (!pl.getConfig().getBoolean("JogarItens")) {
                if (!p.hasPermission("xg7lobby.admin")) {
                    e.setCancelled(true);
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_jogar_itens").replace("&", "§")));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("permissao_jogar_itens").replace("&", "§"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerPickupEvent (EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (pl.getConfig().getBoolean("PegarItens")) {
                    if (!p.hasPermission("xg7lobby.admin")) {
                        e.setCancelled(true);
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_pegar_itens").replace("&", "§")));
                        } else {
                            p.sendMessage(mensagem.getMessage().getString("permissao_pegar_itens").replace("&", "§"));
                        }
                    }
                }
            }
        }
    }
}
