package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class PlayerAttackEvent implements Listener {

    private XG7Lobby pl;

    public PlayerAttackEvent (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player vic = (Player) e.getEntity();
            Player p = (Player) e.getDamager();
            if (pl.getConfig().getStringList("mundos-ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("Ataque")) {
                    if (!p.hasPermission("xg7lobby.admin")) {
                        e.setCancelled(true);
                        if (mensagem.getMessage().getBoolean("ativar_permissao_mensagem")) {
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_permissao_atacar").replace("&", "§")));
                            } else {
                                p.sendMessage(mensagem.getMessage().getString("mensagens.permissao_permissao_atacar").replace("&", "§"));
                            }
                        }
                    }
                }
                if (pl.getConfig().getBoolean("LevarDanoPorAtaque")) {
                    e.setCancelled(!vic.isInvulnerable());
                }
            }
        }
    }
}
