package com.xg7plugins.xg7lobby.events.menuevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import com.xg7plugins.xg7lobby.menus.SelectorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class SelectorEvent implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.SELECTOR, "enabled");
    }

    @Override
    public void onWorldJoin(Player player) {
        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());

        if (!data.isBuildEnabled() && !data.isPVPEnabled()) SelectorManager.open(player);
    }

    @Override
    public void onWorldLeave(Player player) {
        SelectorManager.getMenu().close(player);
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());

        if (!data.isBuildEnabled() && !data.isPVPEnabled()) SelectorManager.open(event.getPlayer());
    }
}
