package com.xg7plugins.xg7lobby.events.commandevents;

import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishCommandEvent implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onWorldJoin(Player player) {
        if (PlayerManager.getPlayerData(player.getUniqueId()).isPlayerHiding()) Bukkit.getOnlinePlayers().forEach(player::hidePlayer);
    }

    @Override
    public void onWorldLeave(Player player) {
        Bukkit.getOnlinePlayers().forEach(player1 -> {
            player1.showPlayer(player);
            player.showPlayer(player1);
        });
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;
        Bukkit.getOnlinePlayers().forEach(player1 -> {

            if (PlayerManager.getPlayerData(player1.getUniqueId()).isPlayerHiding()) player1.hidePlayer(event.getPlayer());
            if (PlayerManager.getPlayerData(event.getPlayer().getUniqueId()).isPlayerHiding()) event.getPlayer().hidePlayer(player1);
        }

        );
    }

    @Override
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getOnlinePlayers().forEach(event.getPlayer()::showPlayer);
    }
}
