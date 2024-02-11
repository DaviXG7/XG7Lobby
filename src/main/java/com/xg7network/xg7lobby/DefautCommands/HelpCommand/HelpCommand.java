package com.xg7network.xg7lobby.DefautCommands.HelpCommand;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!PluginUtil.hasPermission(commandSender, PermissionType.HELP_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }

        Player player = (Player) commandSender;

        HelpGUI.openInventory(player);



        return true;

    }


}
