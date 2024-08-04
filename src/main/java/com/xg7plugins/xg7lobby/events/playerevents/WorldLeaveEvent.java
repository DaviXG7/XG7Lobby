package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.XSeries.XPotion;
import org.bukkit.entity.Player;


import java.util.Objects;

public class WorldLeaveEvent implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onWorldLeave(Player player) {
        Config.getList(ConfigType.CONFIG, "effects").stream().map(s -> Objects.requireNonNull(XPotion.valueOf(s.split(", ")[0].toUpperCase()).getPotionEffectType())).forEach(player::removePotionEffect);
        TaskManager.cancelTask("cooldown:lobby=" + player.getUniqueId());
        TaskManager.cancelTask("cooldown:pvp=" + player.getUniqueId());
        player.setMaxHealth(20);
        player.setHealth(20);
    }
}
