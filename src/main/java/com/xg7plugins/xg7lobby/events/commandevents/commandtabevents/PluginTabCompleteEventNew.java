package com.xg7plugins.xg7lobby.events.commandevents.commandtabevents;

import com.xg7plugins.xg7lobby.commands.CommandManager;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.NMSUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PluginTabCompleteEventNew implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.COMMANDS, "anti-tab") || Config.getBoolean(ConfigType.CONFIG, "block-commands.anti-tab");
    }

    @EventHandler
    public void onTabComplete(PlayerCommandSendEvent event) {

        List<String> commands = new ArrayList<>();
        List<String> commandsToAdd = new ArrayList<>();

        if (event.getPlayer().hasPermission(PermissionType.ANTITAB_PLUGIN_BYPASS.getPerm())) return;
        if (Config.getBoolean(ConfigType.CONFIG, "block-commands.anti-tab") && !event.getPlayer().hasPermission(PermissionType.ANTITAB_BYPASS.getPerm()))
            commands.addAll(Config.getList(ConfigType.CONFIG, "block-commands.commands-blocked").stream().map(cmd -> cmd.replaceFirst("/", "")).collect(Collectors.toList()));
        if (Config.getBoolean(ConfigType.COMMANDS, "anti-tab") && !event.getPlayer().hasPermission(PermissionType.ANTITAB_PLUGIN_BYPASS.getPerm())) {
            commands.addAll(CommandManager.getCommands().stream().flatMap(cmd -> {
                        List<String> combined = new ArrayList<>();
                        if (cmd.getPermission().equals(PermissionType.DEFAULT)) {
                            commandsToAdd.add(cmd.getName());
                            commandsToAdd.addAll(cmd.getAliasses());
                        }
                        combined.add(cmd.getName());
                        combined.addAll(cmd.getAliasses());

                        return combined.stream();
                    })
                    .collect(Collectors.toList()));
        }
        List<String> eventCommands = new ArrayList<>(event.getCommands());
        event.getCommands().clear();
        commandsToAdd.forEach(event.getCommands()::add);
        for (String command : eventCommands) {

            if (commands.stream().noneMatch(command::startsWith)) {
                event.getCommands().add(command);
                break;
            }
        }
    }
}
