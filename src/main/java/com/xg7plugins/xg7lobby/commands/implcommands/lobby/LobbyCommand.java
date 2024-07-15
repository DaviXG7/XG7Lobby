package com.xg7plugins.xg7lobby.commands.implcommands.lobby;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.SubCommand;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.tasks.CooldownTask;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public String getSyntax() {
        return "/xg7lobbylobby";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
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

        if (!CacheManager.getLobbyCache().asMap().containsKey(player.getUniqueId())) {
            CacheManager.put(player.getUniqueId(), CacheType.LOBBY_COOLDOWN, null);
            Text.send(Config.getString(ConfigType.MESSAGES, "lobby.on-cmd"), player);
            TaskManager.addTask(new CooldownTask("cooldown:lobby=" + player.getUniqueId()) {
                @Override
                    public void run() {
                    if (!CacheManager.getLobbyCache().asMap().containsKey(player.getUniqueId())) {
                        World world = Bukkit.getWorld(Config.getString(ConfigType.DATA, "spawn-location.world"));
                        double x = Config.getDouble(ConfigType.DATA, "spawn-location.x");
                        double y = Config.getDouble(ConfigType.DATA, "spawn-location.y");
                        double z = Config.getDouble(ConfigType.DATA, "spawn-location.z");
                        float yaw = (float) Config.getDouble(ConfigType.DATA, "spawn-location.yaw");
                        float pitch = (float) Config.getDouble(ConfigType.DATA, "spawn-location.pitch");

                        player.teleport(new Location(world,x,y,z,yaw,pitch));

                        TaskManager.cancelTask(this.getName());
                        return;
                    }
                    Text.send(Config.getString(ConfigType.MESSAGES, "lobby.on-tp").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getLobbyCache().asMap().get(player.getUniqueId()) - System.currentTimeMillis()) + 1) + ""), player);
                }
            });
            return true;
        }

        Text.send(Config.getString(ConfigType.MESSAGES, "lobby.on-tp-cancel"), player);
        TaskManager.cancelTask("cooldown:lobby=" + player.getUniqueId());
        CacheManager.remove(player.getUniqueId(), CacheType.LOBBY_COOLDOWN);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
