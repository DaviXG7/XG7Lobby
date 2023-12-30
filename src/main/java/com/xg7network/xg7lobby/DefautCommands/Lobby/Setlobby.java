package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Setlobby implements CommandExecutor {

    private static Location location;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {

            if (!PluginUtil.hasPermission(commandSender, PermissionType.SETLOBBY_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
                return true;

            Player player = (Player) commandSender;
            location = player.getLocation();
            save(commandSender);
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
        new LobbyLocation().setLocation(sender, location);
    }
}
