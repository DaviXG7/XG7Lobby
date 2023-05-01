package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class Time implements Listener {

    private XG7Lobby pl;

    public Time (XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void time (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (!pl.getConfig().getBoolean("CicloDoDia")) {
                p.getWorld().setTime(pl.getConfig().getInt("TempoDoDia"));
                p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            } else {
                p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }
    }
}
