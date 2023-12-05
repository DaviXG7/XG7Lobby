package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class LobbyLocation {
    private Location location;
    public Location getLocation() {
        if (configManager.getConfig(ConfigType.DATA).getString("lobby.world") == null) return null;

            ConfigurationSection section = configManager.getConfig(ConfigType.DATA).getConfigurationSection("lobby");
            return location = new Location(
                    Bukkit.getWorld(Objects.requireNonNull(section.getString("world"))),
                    section.getDouble("x"),
                    section.getDouble("y"),
                    section.getDouble("z"),
                    (float) section.getDouble("yaw"),
                    (float) section.getDouble("pitch")

            );

    }

    public void setLocation(FileConfiguration  data, File dataF, CommandSender sender, Location location1) {

            data.set("lobby.world", location1.getWorld().getName());
            data.set("lobby.x", location1.getX());
            data.set("lobby.y", location1.getY());
            data.set("lobby.z", location1.getZ());
            data.set("lobby.yaw", location1.getYaw());
            data.set("lobby.pitch", location1.getPitch());

            try {
                data.save(dataF);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            sender.sendMessage(prefix + ChatColor.GREEN + "The lobby was successfully saved in " + ChatColor.YELLOW +
                    configManager.getConfig(ConfigType.DATA).getString("lobby.world") + ", " +
                    (int) configManager.getConfig(ConfigType.DATA).getDouble("lobby.x") + ", " +
                    (int) configManager.getConfig(ConfigType.DATA).getDouble("lobby.y") + ", " +
                    (int) configManager.getConfig(ConfigType.DATA).getDouble("lobby.z") + ".");

    }
}
