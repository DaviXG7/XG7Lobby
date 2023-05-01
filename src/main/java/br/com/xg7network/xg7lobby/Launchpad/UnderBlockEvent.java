package br.com.xg7network.xg7lobby.Launchpad;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class UnderBlockEvent implements Listener {

    private XG7Lobby pl;

    public UnderBlockEvent(XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void underBlock(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location blockunder = p.getLocation();
        blockunder.setY(blockunder.getY() - 1);
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (pl.getConfig().getBoolean("launchPad.ativado")) {
                if (pl.getConfig().getBoolean("launchPad.umbloco")) {
                    if (pl.getConfig().getBoolean("launchPad.placadepressao")) {
                        if (p.getLocation().getBlock().getType().equals(Material.valueOf(pl.getConfig().getString("launchPad.bloco")))) {
                            p.setVelocity(p.getEyeLocation().getDirection().multiply(pl.getConfig().getDouble("launchPad.força")).setY(pl.getConfig().getDouble("launchPad.pulo")));
                        }

                    } else {
                        if (blockunder.getBlock().getType().equals(Material.valueOf(pl.getConfig().getString("launchPad.bloco")))) {
                            p.setVelocity(p.getEyeLocation().getDirection().multiply(pl.getConfig().getDouble("launchPad.força")).setY(pl.getConfig().getDouble("launchPad.pulo")));
                        }
                    }
                } else {
                    if (p.getLocation().getBlock().getType().equals(Material.valueOf(pl.getConfig().getString("launchPad.blocodecima"))) && blockunder.getBlock().getType().equals(Material.valueOf(pl.getConfig().getString("launchPad.blocodebaixo")))) {
                        p.setVelocity(p.getEyeLocation().getDirection().multiply(pl.getConfig().getDouble("launchPad.força")).setY(pl.getConfig().getDouble("launchPad.pulo")));
                    }
                }
            }
        }
    }
}
