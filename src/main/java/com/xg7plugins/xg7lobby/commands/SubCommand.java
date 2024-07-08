package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.xg7lobby.Enums.PermissionType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface SubCommand {

    String getName();
    PermissionType getPermission();
    default List<SubCommand> getSubCommands() {
        return new ArrayList<>();
    }
    void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

}
