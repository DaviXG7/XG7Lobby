package com.xg7network.xg7lobby;

import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TaskManager {
    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    public static void cancelTask(String name) {
        tasks.get(name).cancel();
        tasks.remove(name);
    }
    public static void addTask(String name, BukkitTask task) {
        tasks.put(name, task);
    }
}
