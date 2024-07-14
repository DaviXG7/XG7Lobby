package com.xg7plugins.xg7lobby.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface SubCommand {

    String getName();
    default PermissionType getPermission() {
        return PermissionType.DEFAULT;
    }
    default List<SubCommand> getSubCommands() {
        return new ArrayList<>();
    }
    default void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SubCommand subcommand = getSubCommands().stream().filter(subCommand -> subCommand.getName().equals(args[(Arrays.asList(args).indexOf(getName()))])).findFirst().orElse(null);
        if (subcommand != null) {
            subcommand.onCommand(sender,command,label,args);
            return;
        }
    }

}
