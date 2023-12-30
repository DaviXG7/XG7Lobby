package com.xg7network.xg7lobby.Module.Events.WorldEvents;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.getPlugin;

public class Cycles implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {

        if (PluginUtil.isInWorld(event.getWorld())) {
            event.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle"));
            event.getWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle"));

            if (!event.getWorld().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) Bukkit.getScheduler().runTaskLater(getPlugin(), () -> event.getWorld().setTime(configManager.getConfig(ConfigType.CONFIG).getLong("time")), 50l);
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        event.setCancelled(!configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"));
    }

}
