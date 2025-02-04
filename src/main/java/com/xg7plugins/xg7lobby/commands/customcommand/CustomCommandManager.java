package com.xg7plugins.xg7lobby.commands.customcommand;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.utils.reflection.ReflectionClass;
import com.xg7plugins.utils.reflection.ReflectionObject;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CustomCommandManager {

    private final HashMap<String, CustomCommand> commands = new HashMap<>();

    @Getter
    private final CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor(this);

    public CustomCommandManager(Config config) {

        CommandMap commandMap = ReflectionObject.of(Bukkit.getServer()).getField("commandMap");

        for (String path : config.get("custom-commands", ConfigurationSection.class).orElse(null).getKeys(false)) {
            CustomCommand customCommand = new CustomCommand(config, path);
            PluginCommand pluginCommand = (PluginCommand) ReflectionClass.of(PluginCommand.class)
                    .getConstructor(String.class, org.bukkit.plugin.Plugin.class)
                    .newInstance(customCommand.getName(), XG7Lobby.getInstance())
                    .getObject();
            pluginCommand.setAliases(customCommand.getAliases());
            pluginCommand.setDescription(customCommand.getDescription());
            pluginCommand.setPermission(customCommand.getPermission());
            pluginCommand.setUsage(customCommand.getSyntax());
            pluginCommand.setExecutor(customCommandExecutor);
            commandMap.register(customCommand.getName(), pluginCommand);
            commands.put(customCommand.getName(), customCommand);
        }
    }

    public CustomCommand getCommand(String name) {
        return commands.get(name);
    }
}
