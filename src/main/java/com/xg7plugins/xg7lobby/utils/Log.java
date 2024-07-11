package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Log {
    @Setter
    public static boolean isEnabled = false;

    public static void severe(String message) {
        Bukkit.getLogger().severe("[XG7Lobby ERROR] " + message);
    }

    public static void fine(String message) {
        if (isEnabled) Bukkit.getLogger().fine("[XG7Lobby SUCSESS] " + message);
    }

    public static void info(String message) {
        if (isEnabled) Bukkit.getLogger().info("[XG7Lobby DEBUG] " + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().log(Level.WARNING, "[XG7Lobby ALERT] " + message);
    }

    public static void loading(String message) {
        Bukkit.getLogger().info("[XG7Lobby] " + message);
    }

}
