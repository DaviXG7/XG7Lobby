package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others;

import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Void implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (PluginUtil.isInWorld(event.getPlayer())) {

            String[] partes = Bukkit.getVersion().split("\\.");
            int vers = Integer.parseInt(partes[1]);
            if (vers >= 18)  {
                if (event.getTo().getY() <= -70) if (new LobbyLocation().getLocation() != null) event.getPlayer().teleport(new LobbyLocation().getLocation());
                else event.getPlayer().setHealth(0);
            } else if (event.getTo().getY() <= -6) if (new LobbyLocation().getLocation() != null) event.getPlayer().teleport(new LobbyLocation().getLocation());
            else event.getPlayer().setHealth(0);
        }
    }

}
