package br.com.xg7network.xg7lobby.Eventos.WorldConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CancelWeather implements Listener {

    private XG7Lobby pl;

    public CancelWeather (XG7Lobby pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onWheather(WeatherChangeEvent e) {
        if (!pl.getConfig().getBoolean("CicloDaChuva")){
            if (pl.getConfig().getStringList("mundos_ativados").contains(e.getWorld().getName())) {
                e.setCancelled(true);
            }
        }
    }
}
