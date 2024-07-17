package com.xg7plugins.xg7lobby.utils;

import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Log {
    @Setter
    public static boolean isEnabled = false;

    public static void severe(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[XG7Lobby ERROR] §r" + message);
    }

    public static void fine(String message) {
        if (isEnabled) Bukkit.getConsoleSender().sendMessage("§a[XG7Lobby SUCSESS] §r" + message);
    }

    public static void info(String message) {
        if (isEnabled) Bukkit.getConsoleSender().sendMessage("§e[XG7Lobby DEBUG] §r" + message);
    }

    public static void warn(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[XG7Lobby ALERT] §r" + message);
    }

    public static void loading(String message) {
        Bukkit.getConsoleSender().sendMessage("§9[XG7§3Lob§bby] §8| §r" + message);
    }

}
