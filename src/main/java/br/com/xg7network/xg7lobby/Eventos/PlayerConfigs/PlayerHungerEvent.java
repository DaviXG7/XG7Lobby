package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerEvent implements Listener {

    private XG7Lobby pl;

    public PlayerHungerEvent(XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (pl.getConfig().getBoolean("PerderPontosDeComida")) {
                ((Player) e.getEntity()).setFoodLevel(20);
            }
        }
    }
}
