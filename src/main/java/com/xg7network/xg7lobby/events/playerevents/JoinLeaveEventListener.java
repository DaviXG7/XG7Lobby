package com.xg7network.xg7lobby.events.playerevents;

import com.xg7network.xg7lobby.config.ConfigManager;
import com.xg7network.xg7lobby.config.ConfigType;
import com.xg7network.xg7lobby.data.PlayerData;
import com.xg7network.xg7lobby.data.PlayersManager;
import com.xg7network.xg7lobby.inventories.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Collectors;

public class JoinLeaveEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = PlayersManager.createData(event.getPlayer());

        if (data.getFirstJoinLong() != 0) {
            data.setFirstJoin(System.currentTimeMillis());
            PlayersManager.update(player.getUniqueId().toString(), data);
        }


        ConfigManager.getConfig(ConfigType.CONFIG).getStringList("join-events.actions").stream().map(Action::new).collect(Collectors.toList()).forEach(action -> action.execute(player));




    }

}
