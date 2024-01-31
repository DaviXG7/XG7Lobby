package com.xg7network.xg7lobby.Module.Events.WorldEvents;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.getPlugin;

public class Cycles implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldLoad(WorldLoadEvent event) {

        if (PluginUtil.isInWorld(event.getWorld())) {
            event.getWorld().setGameRuleValue("doDaylightCycle", "false");
            event.getWorld().setGameRuleValue("doWeatherCycle", "false");

            if (!Boolean.valueOf(event.getWorld().getGameRuleValue("doDaylightCycle"))) Bukkit.getScheduler().runTaskLater(getPlugin(), () -> event.getWorld().setTime(configManager.getConfig(ConfigType.CONFIG).getLong("time")), 50l);
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        event.setCancelled(!configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs") && PluginUtil.isInWorld(event.getLocation().getWorld()));
    }

}
