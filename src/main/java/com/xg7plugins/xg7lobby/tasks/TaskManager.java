package com.xg7plugins.xg7lobby.tasks;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private final static HashMap<String, Integer> tasksRunning = new HashMap<>();

    public static void initTimerTasks() {

        List<Task> taskList = new ArrayList<>();

        if (!Config.getList(ConfigType.CONFIG, "effects-task.effects").isEmpty()) {
            taskList.add(
                    new Task("player-events", Config.getLong(ConfigType.CONFIG, "effects-task.delay")) {
                @Override
                public void run() {
                    Bukkit.getOnlinePlayers().stream().filter(player -> EventManager.getWorlds().contains(player.getWorld().getName())).forEach(player -> {
                        if (Config.getBoolean(ConfigType.CONFIG, "hunger-loss")) player.setFoodLevel(20);
                        player.setMaxHealth(Config.getDouble(ConfigType.CONFIG, "max-hearths") * 4);
                        for (String effect : Config.getList(ConfigType.CONFIG, "effects-task.effects")) {
                            String[] effectSplit = effect.split(", ");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effectSplit[0]), 400, Integer.parseInt(effectSplit[1]) - 1));
                        }
                    });
                }
            });
        }

        taskList.forEach(TaskManager::addTask);

    }

    public static void addTask(Task task) {
        int taskid = Bukkit.getServer().getScheduler().runTaskTimer(
                XG7Lobby.getPlugin(),
                task::run,
                0,
                task.getDelay()
        ).getTaskId();

        tasksRunning.put(task.getName(), taskid);
    }

    public static void addTaskAsync(Task task) {
        int taskid = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(
                XG7Lobby.getPlugin(),
                task::run,
                0,
                task.getDelay()
        ).getTaskId();

        tasksRunning.put(task.getName(), taskid);
    }

    public static void cancelTask(String name) {
        Bukkit.getScheduler().cancelTask(tasksRunning.get(name));
        tasksRunning.remove(name);
    }

    public static void cancelAll() {
        Log.info("Cancelling tasks...");
        tasksRunning.forEach((key, value) -> Bukkit.getScheduler().cancelTask(value));
        tasksRunning.clear();
        Log.info("Cancelled!");
    }
}