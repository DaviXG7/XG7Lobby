package com.xg7plugins.xg7lobby.lobby.location;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import com.xg7plugins.server.ServerInfo;
import com.xg7plugins.utils.location.Location;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.io.IOException;

//Futuramente colocarei mais coisas
@Getter
@Setter
@AllArgsConstructor
@Table(name = "lobbies")
public class LobbyLocation implements Entity<String, LobbyLocation> {
    @Pkey
    private String id;
    private ServerInfo server;
    private Location location;

    private LobbyLocation() {}

    public void teleport(Player player) {
        if (!XG7Plugins.serverInfo().equals(server)) {
            try {
                server.connectPlayer(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        player.teleport(location.getBukkitLocation());
    }

    @Override
    public boolean equals(Object lobbyLocation) {
        if (this == lobbyLocation) return true;
        if (lobbyLocation == null || getClass() != lobbyLocation.getClass()) return false;

        LobbyLocation that = (LobbyLocation) lobbyLocation;

        return id.equals(that.id);
    }

    @Override
    public boolean equals(LobbyLocation lobbyLocation) {
        return id.equals(lobbyLocation.getID());
    }

    @Override
    public String getID() {
        return id;
    }
}
