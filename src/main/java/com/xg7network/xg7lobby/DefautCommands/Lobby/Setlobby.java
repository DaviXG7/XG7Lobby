package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Setlobby implements CommandExecutor {

    private static Location location;

    private static FileConfiguration data;
    private static File dataF;
    private XG7Lobby pl;
    private Connection connection;

    public Setlobby(XG7Lobby pl) {
        this.pl = pl;
        this.data = configManager.getConfig(ConfigType.DATA);
        this.dataF = new File(pl.getDataFolder(), "lobby.yml");

        if (XG7Lobby.connected) {
            this.connection = pl.getSqlConnect().getConnection();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            location = player.getLocation();
            if (strings.length == 1 && strings[0].equals("delete")) {
                delete(commandSender);
            } else {
                save(commandSender);
            }
        } else {
            if (strings.length == 4) {

                World world = Bukkit.getWorld(strings[0]);
                double x = Double.parseDouble(strings[1]);
                double y = Double.parseDouble(strings[2]);
                double z = Double.parseDouble(strings[3]);

                location = new Location(world, x, y, z);
                save(commandSender);

            } else if (strings.length == 6) {

                World world = Bukkit.getWorld(strings[0]);
                double x = Double.parseDouble(strings[1]);
                double y = Double.parseDouble(strings[2]);
                double z = Double.parseDouble(strings[3]);
                float yaw = Float.parseFloat(strings[4]);
                float ptich = Float.parseFloat(strings[5]);

                location = new Location(world, x, y, z, yaw, ptich);
                save(commandSender);

            } else commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "/setlobby §e(world, x, y, z) (world, x, y, z, yaw, pitch) §con console");
        }

        return true;
    }

    void save(CommandSender sender) {
        new LobbyLocation(pl).setLocation(data, dataF, sender, location);
    }

    void delete(CommandSender sender) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM lobby");
            ps.executeUpdate();
            sender.sendMessage(prefix + ChatColor.GREEN + "All lobbies have been deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
