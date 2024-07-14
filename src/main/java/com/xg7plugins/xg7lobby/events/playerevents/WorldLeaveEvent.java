package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class WorldLeaveEvent implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return !Config.getList(ConfigType.CONFIG, "effects-task.effects").isEmpty();
    }

    @Override
    public void onWorldLeave(Player player) {
        for (String s : Config.getList(ConfigType.CONFIG, "effects-task.effects")) {
            player.removePotionEffect(Objects.requireNonNull(PotionEffectType.getByName(s.split(", ")[0])));
        }

        player.setMaxHealth(20);
    }
}
