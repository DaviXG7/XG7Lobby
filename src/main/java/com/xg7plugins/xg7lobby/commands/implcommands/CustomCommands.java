package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!Config.getConfigurationSections(ConfigType.COMMANDS, "custom-commands").contains(command.getName())) return true;

        if (!Config.getString(ConfigType.COMMANDS, "custom-commands." + command.getName() + ".permission").isEmpty()) {
            if (!commandSender.hasPermission(Config.getString(ConfigType.COMMANDS, "custom-commands." + command.getName() + ".permission"))) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), commandSender);
                return true;
            }
        }
        Config.getList(ConfigType.COMMANDS, "custom-commands." + command.getName() + ".actions").forEach(ac -> Action.execute(ac, (Player) commandSender));
        return true;
    }
}
