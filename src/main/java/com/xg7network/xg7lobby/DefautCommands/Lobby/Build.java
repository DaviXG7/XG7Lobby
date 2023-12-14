package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Build implements CommandExecutor, Listener {

    public static HashMap<UUID, Boolean> canBuild = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (PluginUtil.isInWorld(player)) {
                if (!PluginUtil.hasPermission(commandSender, PermissionType.BUILD, ErrorMessages.NO_PEMISSION.getMessage())) return true;

                if (!canBuild.containsKey(player.getUniqueId())) {
                    canBuild.put(player.getUniqueId(), true);
                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.build-enabled")).send(player);
                } else {
                    canBuild.remove(player.getUniqueId());
                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.build-disabled")).send(player);
                }

            } else {
                commandSender.sendMessage(ErrorMessages.NOT_IN_WORLD.getMessage());
            }

        } else {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
        }

        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {

        Player player = event.getPlayer();

        if (!PluginUtil.isInWorld(event.getTo().getWorld())) canBuild.remove(player.getUniqueId());


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        canBuild.remove(event.getPlayer().getUniqueId());
    }


    public static boolean canBuild(Player player) {
        return canBuild.containsKey(player.getUniqueId());
    }

}
