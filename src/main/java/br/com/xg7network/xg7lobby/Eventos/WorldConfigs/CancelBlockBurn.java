package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class CancelBlockBurn implements Listener {

    private XG7Lobby pl;

    public CancelBlockBurn (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {

        if (pl.getConfig().getStringList("mundos_ativados").contains(e.getBlock().getWorld().getName())) {
            if (pl.getConfig().getBoolean("QueimaDeBlocos")) {
                e.setCancelled(true);
            }
        }
    }
}
