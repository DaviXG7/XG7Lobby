package br.com.xg7network.xg7lobby.Eventos.JoinEvent;

import br.com.xg7network.xg7lobby.XG7Lobby;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

import static br.com.xg7network.xg7lobby.XG7Lobby.data;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class TpJoinLobby implements Listener {

    private XG7Lobby pl;

    public TpJoinLobby (XG7Lobby pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onJointp(PlayerJoinEvent pje) {
        if (pl.getConfig().getBoolean("tp_quando_entrar")) {
            if (data.getData().getString("lobby.Mundo") != null) {
                String mundo = data.getData().getString("lobby.Mundo");

                World w;
                if (mundo != null) {
                    w = Bukkit.getWorld(mundo);
                    pje.getPlayer().teleport(new Location(w,
                            data.getData().getDouble("lobby.X"),
                            data.getData().getDouble("lobby.Y"),
                            data.getData().getDouble("lobby.Z"),
                            (float) data.getData().getDouble("lobby.Yaw"),
                            (float) data.getData().getDouble("lobby.Pitch"))
                    );
                }
            } else {

                if(mensagem.getMessage().getBoolean("mensagens.ativar_aviso_lobby")) {
                    if (pje.getPlayer().hasPermission("xg7lobby.admin")) {
                        String avisoLobby = mensagem.getMessage().getString("mensagens.aviso_lobby_admin").replace("&", "§");
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                avisoLobby = PlaceholderAPI.setPlaceholders(p, avisoLobby);
                            }
                        }
                        pje.getPlayer().sendMessage(avisoLobby);
                    } else {
                        String avisoLobby = mensagem.getMessage().getString("mensagens.aviso_lobby").replace("&", "§");
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                avisoLobby = PlaceholderAPI.setPlaceholders(p, avisoLobby);
                            }
                        }
                        pje.getPlayer().sendMessage(avisoLobby);

                    }
                }
            }
        }
    }
}
