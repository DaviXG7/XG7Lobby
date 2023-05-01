package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

import static br.com.xg7network.xg7lobby.XG7Lobby.data;

public class PlayeronVoid implements Listener {

    private XG7Lobby pl;

    public PlayeronVoid(XG7Lobby pl) {
        this.pl = pl;
    }

    int camada = -64;

    @EventHandler
    public void onVoid(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (pl.getConfig().getBoolean("CancelarMortePeloVoid")) {
                if (Bukkit.getServer().getVersion().contains("1.18")) {

                    Location loc = p.getLocation();
                    if (loc.getY() < camada) {
                        if (data.getData().getString("lobby.Mundo") != null) {
                            World w = Bukkit.getWorld(Objects.requireNonNull(data.getData().getString("lobby.Mundo")));
                            p.teleport(new Location(
                                    w, data.getData().getDouble("lobby.X"),
                                    data.getData().getDouble("lobby.Y"),
                                    data.getData().getDouble("lobby.Z"),
                                    (float) data.getData().getDouble("lobby.Yaw"),
                                    (float) data.getData().getDouble("lobby.Pitch")
                            ));
                        } else {
                            p.teleport(p.getWorld().getSpawnLocation());
                        }
                    }
                } else {
                    Location loc = p.getLocation();
                    if (loc.getY() < 0) {
                        if (data.getData().get("lobby.Mundo") != null) {
                            World w = Bukkit.getWorld(Objects.requireNonNull(data.getData().getString("lobby.Mundo")));
                            p.teleport(new Location(
                                    w, data.getData().getDouble("lobby.X"),
                                    data.getData().getDouble("lobby.Y"),
                                    data.getData().getDouble("lobby.Z"),
                                    (float) data.getData().getDouble("lobby.Yaw"),
                                    (float) data.getData().getDouble("lobby.Pitch")
                            ));
                        } else {
                            p.teleport(p.getWorld().getSpawnLocation());
                        }
                    }
                }
            }
        }
    }
}
