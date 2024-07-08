package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.xg7lobby.Enums.PermissionType;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    String getName();
    List<String> getAliasses();
    String getDescription();
    String getSyntax();
    PermissionType getPermission();
    boolean isEnabled();
    boolean isOnlyPlayer();
    List<SubCommand> getSubCommands();
    boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);
    List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

}
