package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTask extends Task {

    public WorldTask() {
        super("xg7lworld", Config.getLong(ConfigType.CONFIG, "world-task-delay"));
    }

    @Override
    public void run() {
        EventManager.getWorlds().forEach(wn -> {
            World world = Bukkit.getWorld(wn);
            if (world == null) return;
            world.setStorm(Config.getBoolean(ConfigType.CONFIG, "storm"));
            if (!Config.getBoolean(ConfigType.CONFIG, "day-cycle")) world.setTime(Config.getLong(ConfigType.CONFIG, "time"));
        });
    }
}
