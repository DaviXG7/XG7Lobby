package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Log {
    @Setter
    public static boolean isEnabled = false;

    public static void severe(String message) {
        Bukkit.getLogger().severe(ChatColor.RED + "[XG7Lobby ERROR] " + ChatColor.RESET + message);
    }

    public static void fine(String message) {
        if (isEnabled) Bukkit.getLogger().fine(ChatColor.GREEN + "[XG7Lobby SUCSESS] " + ChatColor.RESET + message);
    }

    public static void info(String message) {
        if (isEnabled) Bukkit.getLogger().info(ChatColor.YELLOW + "[XG7Lobby DEBUG] " + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().log(Level.WARNING, ChatColor.GOLD + "[XG7Lobby ALERT] " + ChatColor.RESET + message);
    }

    public static void loading(String message) {
        Bukkit.getLogger().info(ChatColor.BLUE + "[XG7" + ChatColor.DARK_AQUA + "Lob" + ChatColor.AQUA + "by] " + ChatColor.DARK_GRAY + "| " + ChatColor.RESET + " " + message);
    }

}
