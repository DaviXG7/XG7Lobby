package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    String getName();
    default List<String> getAliasses() {
        return Config.getList(ConfigType.COMMANDS, "commands." + getName() + ".args");
    }
    String getDescription();
    String getSyntax();
    default PermissionType getPermission() {
        return PermissionType.DEFAULT;
    }
    default boolean isEnabled() {
        return Config.getBoolean(ConfigType.COMMANDS, "commands." + getName() + ".enabled");
    }
    boolean isOnlyPlayer();
    List<SubCommand> getSubCommands();
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
            return true;
        }
        SubCommand subcommand = getSubCommands().stream().filter(subCommand -> subCommand.getName().equals(args[0])).findFirst().orElse(null);
        if (subcommand != null) {
            subcommand.onCommand(sender,command,label,args);
            return true;
        }
        return true;
    }
    List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

}
