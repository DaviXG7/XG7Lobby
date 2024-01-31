package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (PluginUtil.hasPermission(commandSender, PermissionType.RELOAD_CONFIG, ErrorMessages.NO_PEMISSION.getMessage())) {
            configManager.reloadConfig(ConfigType.CONFIG);
            configManager.reloadConfig(ConfigType.SELECTORS);
            configManager.reloadConfig(ConfigType.MESSAGES);
            configManager.reloadConfig(ConfigType.DATA);

            commandSender.sendMessage(prefix + ChatColor.GREEN + "All files has been reloaded!");
        }

        return true;
    }
}
