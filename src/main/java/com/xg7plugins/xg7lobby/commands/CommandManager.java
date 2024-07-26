package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.xg7lobby.commands.implcommands.*;
import com.xg7plugins.xg7lobby.commands.implcommands.chatcommands.ClearChat;
import com.xg7plugins.xg7lobby.commands.implcommands.chatcommands.LockChatCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.moderationcommands.*;
import com.xg7plugins.xg7lobby.commands.implcommands.tocreatorcommands.BugsCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.tocreatorcommands.SuggestCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.togglecommands.FlyCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.togglecommands.PVPCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.togglecommands.VanishCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.lobby.BuildCommand;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.commands.implcommands.lobby.LobbyCommand;
import com.xg7plugins.xg7lobby.commands.implcommands.lobby.SetLobbyCommand;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.Text;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter, Listener {

    @Getter
    private static final List<com.xg7plugins.xg7lobby.commands.Command> commands = new ArrayList<>();

    @SneakyThrows
    public void init() {
        Log.info("Loading commands...");
        PermissionType.register();

        commands.add(new SetLobbyCommand());
        commands.add(new LobbyCommand());
        commands.add(new ReloadCommand());
        commands.add(new BuildCommand());
        commands.add(new PVPCommand());
        commands.add(new FlyCommand());
        commands.add(new VanishCommand());
        commands.add(new GuiCommand());
        commands.add(new ActionCommand());
        commands.add(new WarnsCommand());
        commands.add(new WarnCommand());
        commands.add(new Mute.MuteCommand());
        commands.add(new Mute.TempmuteCommand());
        commands.add(new Mute.UnmuteCommand());
        commands.add(new Ban.BanCommand());
        commands.add(new Ban.BanipCommand());
        commands.add(new Ban.TempBanCommand());
        commands.add(new Ban.UnbanCommand());
        commands.add(new KickCommand());
        commands.add(new LockChatCommand());
        commands.add(new ClearChat());
        commands.add(new GamemodeCommand());
        commands.add(new HelpCommand());
        commands.add(new SuggestCommand());
        commands.add(new BugsCommand());

        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

        for (com.xg7plugins.xg7lobby.commands.Command command : commands) {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(command.getName(), XG7Lobby.getPlugin());
            pluginCommand.setExecutor(this);
            pluginCommand.setUsage(command.getSyntax());
            pluginCommand.setTabCompleter(this);
            pluginCommand.setDescription(command.getDescription());
            pluginCommand.setAliases(command.getAliasses());
            commandMap.register(command.getName(), pluginCommand);
        }

        XG7Lobby.getPlugin().getServer().getPluginManager().registerEvents(this, XG7Lobby.getPlugin());

        Log.fine("Commands loaded!");
    }

    @SneakyThrows
    public static void initCustomCommands() {
        Log.info("Loading custom commands...");

        for (String cmd : Config.getConfigurationSections(ConfigType.COMMANDS, "custom-commands")) {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());


            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(cmd, XG7Lobby.getPlugin());
            pluginCommand.setExecutor(new CustomCommands());
            pluginCommand.setDescription(Config.getString(ConfigType.COMMANDS, "custom-commands." + cmd + ".description"));
            pluginCommand.setAliases(Config.getList(ConfigType.COMMANDS, "custom-commands." + cmd + ".aliases"));
            commandMap.register(cmd, pluginCommand);

        }
        Log.fine("Custom commands loaded!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        com.xg7plugins.xg7lobby.commands.Command command1 = commands.stream().filter(cmd -> cmd.getName().equals(command.getName())).findFirst().get();

        if (!commandSender.hasPermission(command1.getPermission().getPerm()) && !command1.getPermission().equals(PermissionType.DEFAULT)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), commandSender);
            return true;
        }
        if (!(commandSender instanceof Player)) {
            if (command1.isOnlyPlayer()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), commandSender);
                return true;
            }
        }
        if (commandSender instanceof Player) {
            if (command1.isOnlyInLobbyWorld() && !EventManager.getWorlds().contains(((Player) commandSender).getWorld().getName())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-in-world"), commandSender);
                return true;
            }
        }
        return command1.onCommand(commandSender,command,s,strings);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        com.xg7plugins.xg7lobby.commands.Command command1 = commands.stream().filter(cmd -> cmd.getName().equals(command.getName())).findFirst().get();
        return command1.onTabComplete(commandSender,command,s,strings);
    }
}
