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
import java.util.Arrays;
import java.util.List;

public class LobbyCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbylobby";
    }

    @Override
    public String getDescription() {
        return "Teleports to lobby location";
    }

    @Override
    public List<String> getAliasses() {
        return Arrays.asList("7llobby", "7ll", "l", "lobby");
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbylobby";
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (Config.getString(ConfigType.DATA, "spawn-location.world") == null) {
            if (player.hasPermission(PermissionType.SET_LOBBY.getPerm())) {
                Text.send("&cThe lobby was not placed! Use &a/xg7lobbysetlobby &cto place!", player);
                return true;
            }
            Text.send(Config.getString(ConfigType.MESSAGES, "lobby.no-loation"), player);
            return true;
        }

        World world = Bukkit.getWorld(Config.getString(ConfigType.DATA, "spawn-location.world"));
        double x = Config.getDouble(ConfigType.DATA, "spawn-location.x");
        double y = Config.getDouble(ConfigType.DATA, "spawn-location.y");
        double z = Config.getDouble(ConfigType.DATA, "spawn-location.z");
        float yaw = (float) Config.getDouble(ConfigType.DATA, "spawn-location.yaw");
        float pitch = (float) Config.getDouble(ConfigType.DATA, "spawn-location.pitch");

        Location location = new Location(world,x,y,z,yaw,pitch);

        player.teleport(location);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
