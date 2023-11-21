package com.xg7network.xg7lobby.DefautCommands;

import com.xg7network.xg7lobby.Utils.Action.Action;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        commandSender.sendMessage("executando...");

        String action = String.join(" ", strings);
        Action Action = new Action((Player) commandSender, action);
        Action.execute();
        return true;
    }
}
