package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Sql.SqlUtil;
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
    private Connection connection;

    public LobbyLocation(XG7Lobby pl) {
        if (XG7Lobby.connected) {
            this.connection = pl.getSqlConnect().getConnection();
        }
    }

    public Location getLocation() {
        if (XG7Lobby.connected) {
            List<Object> locationobj = SqlUtil.get(connection, "SELECT * FROM lobby", Arrays.asList("world", "x", "y", "z", "yaw", "pitch"));

            World world = Bukkit.getWorld((String) locationobj.get(0));
            double x = (Double) locationobj.get(1);
            double y = (Double) locationobj.get(2);
            double z = (Double) locationobj.get(3);
            float yaw = (Float) locationobj.get(4);
            float pitch = (Float) locationobj.get(5);

            return location = new Location(world, x, y, z, yaw, pitch);

        } else {
            if (configManager.getConfig(ConfigType.DATA).getString("lobby.world") == null) {
                return null;
            }

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

    }

    public void setLocation(FileConfiguration  data, File dataF, CommandSender sender, Location location1) {

        if (XG7Lobby.connected) {

            List<Object> object = SqlUtil.get(connection, "SELECT * FROM lobby WHERE world = ?", new ArrayList<>(), Collections.singletonList(SqlUtil.get(connection, "SELECT * FROM lobby", "world")));

            List<Object> objects = new ArrayList<>();
            objects.add(location1.getWorld().getName());
            objects.add(location1.getX());
            objects.add(location1.getY());
            objects.add(location1.getZ());
            objects.add(location1.getYaw());
            objects.add(location1.getPitch());
            if (object != null) {


                SqlUtil.set(connection, "UPDATE lobby SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE world = ?", objects);

                sender.sendMessage(prefix + ChatColor.GREEN + "The lobby was successfully updated!");


            } else {


                SqlUtil.set(connection, "INSERT INTO lobby (world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?)", objects);

                sender.sendMessage(prefix + ChatColor.GREEN + "The lobby was successfully saved!");
            }

        } else {

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
}
