package com.xg7network.xg7lobby.DefautCommands;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.PrivateInforations.SendToCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class ToCreator implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!PluginUtil.hasPermission(commandSender, PermissionType.ADMIN, ErrorMessages.NO_PEMISSION.getMessage())) return true;

        if (strings.length == 0) commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage());

        StringBuilder str = new StringBuilder();

        for (String string : strings) str.append(string).append(" ");

        if (command.getName().equalsIgnoreCase("xg7lobbysuggest")) {
            SendToCreator.Suggest(str.toString().trim(), commandSender);
        } else {
            SendToCreator.bugReport(str.toString().trim(), commandSender);
        }


        return true;
    }
}
