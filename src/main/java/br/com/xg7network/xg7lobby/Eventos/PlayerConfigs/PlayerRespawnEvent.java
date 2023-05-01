package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static br.com.xg7network.xg7lobby.XG7Lobby.data;

public class PlayerRespawnEvent implements Listener {

    private XG7Lobby pl;

    public PlayerRespawnEvent(XG7Lobby pl) {
        this.pl = pl;
    }

    String s = data.getData().getString("lobby.Mundo");
    World w = Bukkit.getWorld(s);

    @EventHandler
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (pl.getConfig().getBoolean("tp_quando_morrer")) {
                if (data.getData().get("lobby.Mundo") != null) {

                    p.teleport(new Location(w,
                            data.getData().getDouble("lobby.X"),
                            data.getData().getDouble("lobby.Y"),
                            data.getData().getDouble("lobby.Z"),
                            (float) data.getData().getDouble("lobby.Yaw"),
                            (float) data.getData().getDouble("lobby.X")
                    ));
                } else {
                    p.teleport(w.getSpawnLocation());
                }
            }
        }

    }
}
