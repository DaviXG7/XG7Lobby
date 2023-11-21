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
import java.util.Objects;

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
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM lobby");
                ResultSet rs = ps.executeQuery();
                World world;
                double x;
                double y;
                double z;
                float yaw;
                float pitch;

                if (rs.next()) {
                    world = Bukkit.getWorld(rs.getString("world"));
                    x = rs.getDouble("x");
                    y = rs.getDouble("y");
                    z = rs.getDouble("z");
                    yaw = rs.getFloat("yaw");
                    pitch = rs.getFloat("pitch");
                } else {
                    return null;
                }

                return location = new Location(world, x, y, z, yaw, pitch);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

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
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM lobby WHERE world = ?");
                preparedStatement.setString(1, location1.getWorld().getName());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {

                    PreparedStatement ps = connection.prepareStatement("UPDATE lobby SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE world = ?");
                    ps.setString(1, location1.getWorld().getName());
                    ps.setDouble(2, location1.getX());
                    ps.setDouble(3, location1.getY());
                    ps.setDouble(4, location1.getZ());
                    ps.setFloat(5, location1.getYaw());
                    ps.setFloat(6, location1.getPitch());
                    ps.setString(7, resultSet.getString("world"));
                    ps.executeUpdate();

                    sender.sendMessage(prefix + ChatColor.GREEN + "The lobby was successfully updated!");

                } else {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO lobby (world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setString(1, location1.getWorld().getName());
                    ps.setDouble(2, location1.getX());
                    ps.setDouble(3, location1.getY());
                    ps.setDouble(4, location1.getZ());
                    ps.setFloat(5, location1.getYaw());
                    ps.setFloat(6, location1.getPitch());
                    ps.executeUpdate();

                    sender.sendMessage(prefix + ChatColor.GREEN + "The lobby was successfully saved!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
