package com.xg7plugins.xg7lobby.tasks;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.jumpevents.DoubleJumpEvent;
import com.xg7plugins.xg7lobby.menus.SelectorManager;
import com.xg7plugins.xg7lobby.scores.Bossbar;
import com.xg7plugins.xg7lobby.tasks.tasksimpl.*;
import com.xg7plugins.xg7lobby.utils.Log;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private final static HashMap<String, Integer> tasksRunning = new HashMap<>();

    public static void initTimerTasks() {

        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerData data = PlayerManager.createPlayerData(p.getUniqueId());
            if (EventManager.getWorlds().contains(p.getWorld().getName())) {
                if (Config.getBoolean(ConfigType.SELECTOR, "enabled")) {
                    if (!data.isPVPEnabled() && !data.isBuildEnabled()) SelectorManager.open(p);
                }
                if (Config.getBoolean(ConfigType.CONFIG, "double-jump.enabled")) {
                    p.setAllowFlight(p.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
                    DoubleJumpEvent.isJumping.add(p.getUniqueId());
                }
                if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) < 9) {
                    Log.warn(" Your version do not support bossbars!");
                    return;
                }
                if (Config.getBoolean(ConfigType.CONFIG, "bossbar.enabled")) Bossbar.addPlayer(p);
            }
        });

        List<Task> taskList = new ArrayList<>();

        taskList.add(new PlayerEventsTask());
        taskList.add(new ScoreTask());
        taskList.add(new WorldTask());
        taskList.add(new AutoBroadcast());
        if (Config.getBoolean(ConfigType.CONFIG, "warn-for-version")) taskList.add(new WarnVersion());
        if (Config.getBoolean(ConfigType.CONFIG, "anti-spam.enabled")) taskList.add(new ChatTask());

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
        if (tasksRunning.get(name) == null) return;
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