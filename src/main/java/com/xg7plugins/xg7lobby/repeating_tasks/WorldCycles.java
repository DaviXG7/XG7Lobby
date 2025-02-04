package com.xg7plugins.xg7lobby.repeating_tasks;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.tasks.TaskState;
import com.xg7plugins.utils.reflection.ReflectionClass;
import com.xg7plugins.utils.reflection.ReflectionObject;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class WorldCycles extends Task {
    public WorldCycles() {
        super(XG7Lobby.getInstance(), "world-cycles", false, true, XG7Lobby.getInstance().getConfig("config").getTime("world-task-delay").orElse(30000L), TaskState.RUNNING, null);
    }

    @Override
    public void run() {
        Config config = XG7Lobby.getInstance().getConfig("config");

        Bukkit.getWorlds().forEach(world -> {
            if (!XG7Lobby.getInstance().isWorldEnabled(world)) return;

            boolean dayLightCycle = config.get("day-cycle", Boolean.class).orElse(false);
            boolean weatherCycle = config.get("weather-cycle", Boolean.class).orElse(false);

            String time = config.get("time", String.class).orElse("12PM");
            boolean storm = config.get("storm", Boolean.class).orElse(false);

            if (!dayLightCycle) {
                if (XG7Plugins.getMinecraftVersion() > 12) world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                else world.setGameRuleValue("doDaylightCycle", "false");
                world.setTime(translateTimeToMinecraftTicks(time));
            }

            if (!weatherCycle) {
                if (XG7Plugins.getMinecraftVersion() > 12) world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                else world.setGameRuleValue("doWeatherCycle", "false");
                world.setStorm(storm);
            }

        });
    }

    private long translateTimeToMinecraftTicks(String timeInput) {
        Integer hours = null;
        Integer minutes = null;

        try {
            LocalTime time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
            hours = time.getHour();
            minutes = time.getMinute();
        } catch (DateTimeParseException ignored) {
        }

        try {
            LocalTime time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("hh:mm a"));
            hours = time.getHour();
            minutes = time.getMinute();
        } catch (DateTimeParseException ignored) {
        }

        if (hours == null || minutes == null) throw new IllegalArgumentException("Formato de hora inválido! Use 00:00 - 23:59 ou 12 AM - 11:59 PM.");

        int ticksFromHour = hours * 1000;
        int ticksFromMinute = (int) (minutes * (1000.0 / 60));
        return ticksFromHour + ticksFromMinute;

    }

}

