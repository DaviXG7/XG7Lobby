package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.Enums.ConfigType;
import com.xg7plugins.xg7lobby.Enums.PermissionType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.SubCommand;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetLobbyCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbysetlobby";
    }

    @Override
    public String getDescription() {
        return "Puts the lobby location";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbysetlobby (On console [x,y,z] or [x,y,z,yaw,pitch])";
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.SET_LOBBY;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            Config.set(ConfigType.DATA, "spawn-location.world", location.getWorld().getName());
            Config.set(ConfigType.DATA, "spawn-location.x", location.getX());
            Config.set(ConfigType.DATA, "spawn-location.y", location.getY());
            Config.set(ConfigType.DATA, "spawn-location.z", location.getZ());
            Config.set(ConfigType.DATA, "spawn-location.yaw", location.getYaw());
            Config.set(ConfigType.DATA, "spawn-location.pitch", location.getPitch());
            Config.save(ConfigType.DATA);
            Config.reload(ConfigType.DATA);
            Text.send("&aLobby successfully placed at &bworld: &r" + location.getWorld().getName() + "&b, x: &r" + location.getX() + "&b, y: &r" + location.getY() + "&b, z: &r" + location.getZ() + "&b, yaw: &r" + location.getYaw() + "&b, pitch: &r" + location.getPitch(), sender);
            return true;
        }

        if (args.length != 4 && args.length != 6) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
            return true;
        }

        try {

            if (Bukkit.getWorld(args[0]) == null) {
                Text.send("The world doesn't exist!", sender);
                return true;
            }

            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);
            float yaw = 0;
            float pitch = 0;
            if (args.length == 6) {
                yaw = Float.parseFloat(args[4]);
                pitch = Float.parseFloat(args[5]);
            }

            Config.set(ConfigType.DATA, "spawn-location.world", args[0]);
            Config.set(ConfigType.DATA, "spawn-location.x", x);
            Config.set(ConfigType.DATA, "spawn-location.y", y);
            Config.set(ConfigType.DATA, "spawn-location.z", z);
            Config.set(ConfigType.DATA, "spawn-location.yaw", yaw);
            Config.set(ConfigType.DATA, "spawn-location.pitch", pitch);

            Config.save(ConfigType.DATA);
            Text.send("&aLobby successfully placed at &bworld: &r" + args[0] + "&b, x: &r" + x + "&b, y: &r" + y + "&b, z: &r" + z + "&b, yaw: &r" + y + "&b, pitch: &r" + pitch, sender);
            return true;
        } catch (Exception e) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
        }



        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            switch (args.length) {
                case 1:
                    return Collections.singletonList("world");
                case 2:
                    return Collections.singletonList("x");
                case 3:
                    return Collections.singletonList("y");
                case 4:
                    return Collections.singletonList("z");
                case 5:
                    return Collections.singletonList("yaw");
                case 6:
                    return Collections.singletonList("pitch");
            }
        }
        return new ArrayList<>();
    }
}
