package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnMobs implements Listener {

    private XG7Lobby pl;

    public SpawnMobs (XG7Lobby pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        if  (pl.getConfig().getBoolean("mobSpawn")) {
            if (pl.getConfig().getStringList("mundos_ativados").contains(e.getLocation().getWorld().getName())) {
                e.setCancelled(false);
            }
        } else if (!pl.getConfig().getBoolean("mobSpawn")) {
            if (pl.getConfig().getStringList("mundos_ativados").contains(e.getLocation().getWorld().getName())) {
                e.setCancelled(true);
            }
        }
    }
}
