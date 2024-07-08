package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.xg7lobby.Enums.ConfigType;
import com.xg7plugins.xg7lobby.Enums.PermissionType;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Log;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter, Listener {

    private static final List<com.xg7plugins.xg7lobby.commands.Command> commands = new ArrayList<>();

    @SneakyThrows
    public void init() {
        Log.info("Loading commands...");
        PermissionType.register();

        commands.

        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

        for (com.xg7plugins.xg7lobby.commands.Command command : commands) {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, JavaPlugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(command.getName(), XG7Lobby.getPlugin());
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
            commandMap.register(command.getName(), pluginCommand);
        }

        Log.fine("Commands loaded!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        com.xg7plugins.xg7lobby.commands.Command command1 = commands.stream().filter(cmd -> cmd.getName().equals(command.getName())).findFirst().get();

        if (!commandSender.hasPermission(command1.getPermission().getPerm())) {
            commandSender.sendMessage(Config.getString(ConfigType.MESSAGES, "commands.no-permission"));
            return true;
        }
        return command1.onCommand(commandSender,command,s,strings);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        com.xg7plugins.xg7lobby.commands.Command command1 = commands.stream().filter(cmd -> cmd.getName().equals(command.getName())).findFirst().get();
        return command1.onTabComplete(commandSender,command,s,strings);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {

        if (!Config.getBoolean(ConfigType.COMMANDS, "anti-tab")) return;

        Iterator<String> it = event.getCompletions().iterator();
        while (it.hasNext()) {
            String completion = it.next();

            com.xg7plugins.xg7lobby.commands.Command command1 = commands.stream().filter(cmd -> ("/" + cmd.getName()).equals(completion)).findFirst().get();

            if (!event.getSender().hasPermission(command1.getPermission().getPerm())) {
                it.remove();
            }
            break;
        }
    }
}
