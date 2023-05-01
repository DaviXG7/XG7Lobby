package br.com.xg7network.xg7lobby.MOTD;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;

public class PingEvent implements Listener {

    private XG7Lobby pl;

    public PingEvent(XG7Lobby pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        if (pl.getConfig().getBoolean("motd.ativado")) {
            String motd = String.join("\n",  pl.getConfig().getStringList("motd.texto"));
            e.setMotd(motd.replace("&", "§"));
            try {
                    e.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
            } catch (Exception ex) {
                if (pl.getConfig().getBoolean("aviso_imagem")) {
                        Bukkit.getConsoleSender().sendMessage("Cuidado! Seu servidor não tem imagem, tente colocar um arquivo com o nome icon.png");
                }
            }
        }
    }
}
