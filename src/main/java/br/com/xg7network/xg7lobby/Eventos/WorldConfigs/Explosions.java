package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Explosions implements Listener {
    private XG7Lobby pl;

    public Explosions(XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (this.pl.getConfig().getBoolean("CancelarExplosões") && this.pl.getConfig().getStringList("mundos_ativados").contains(e.getEntity().getWorld().getName())) {
            e.setCancelled(true);
        }

    }
}
