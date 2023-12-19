package com.xg7network.xg7lobby.Module.Events.WorldEvents;

import com.xg7network.xg7lobby.Configs.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Cycles {


    public Cycles() {

        for (String worlds : configManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds")) {

            World world = Bukkit.getWorld(worlds);

            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle"));
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle"));

            if (world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) world.setTime(configManager.getConfig(ConfigType.CONFIG).getLong("time"));

        }

    }

}
