package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class CancelLeavesDecay implements Listener {

    private XG7Lobby pl;

    public CancelLeavesDecay (XG7Lobby pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        if (pl.getConfig().getStringList("mundos_ativados").contains(e.getBlock().getWorld().getName())) {
            if (!pl.getConfig().getBoolean("FolhasCaindo")) {
                e.setCancelled(true);
            }
        }
    }
}
