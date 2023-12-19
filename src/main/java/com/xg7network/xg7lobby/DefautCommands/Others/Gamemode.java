package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Gamemode implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1) {
            if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_OTHERS, ErrorMessages.NO_PEMISSION.getMessage()))
                return true;
        }

        switch (command.getName()) {
            case "xg7lobbygma":

                if (strings.length == 0) {

                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
                        return true;
                    }

                    Player player = (Player) commandSender;

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_ADVENTURE, ErrorMessages.NO_PEMISSION.getMessage()))
                        return true;

                    player.setGameMode(GameMode.ADVENTURE);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", player.getGameMode().toString())).send(player);

                } else if (strings.length == 1) {

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_ADVENTURE, ErrorMessages.NO_PEMISSION.getMessage() + "With adventure mode!"))
                        return true;

                    Player target = Bukkit.getPlayer(strings[0]);

                    target.setGameMode(GameMode.ADVENTURE);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", target.getGameMode().toString())).send(target);

                    commandSender.sendMessage(new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-target").replace("GAMEMODE", target.getGameMode().toString()).replace("PLAYERT", target.getName())).get());

                } else {
                    commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bgma (Player)");
                }

                return true;

            case"xg7lobbygmc":

                if (strings.length == 0) {

                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
                        return true;
                    }

                    Player player = (Player) commandSender;

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_CREATIVE, ErrorMessages.NO_PEMISSION.getMessage()))
                        return true;

                    player.setGameMode(GameMode.CREATIVE);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", player.getGameMode().toString())).send(player);

                } else if (strings.length == 1) {

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_CREATIVE, ErrorMessages.NO_PEMISSION.getMessage() + "With creative mode!"))
                        return true;

                    Player target = Bukkit.getPlayer(strings[0]);

                    target.setGameMode(GameMode.CREATIVE);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", target.getGameMode().toString())).send(target);

                    commandSender.sendMessage(new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-target").replace("GAMEMODE", target.getGameMode().toString()).replace("PLAYERT", target.getName())).get());

                } else {
                    commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bgmc (Player)");
                }

                return true;

            case"xg7lobbygms":

                if (strings.length == 0) {

                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
                        return true;
                    }

                    Player player = (Player) commandSender;

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_SURVIVAL, ErrorMessages.NO_PEMISSION.getMessage()))
                        return true;

                    player.setGameMode(GameMode.SURVIVAL);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", player.getGameMode().toString())).send(player);

                } else if (strings.length == 1) {

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_SURVIVAL, ErrorMessages.NO_PEMISSION.getMessage() + "With survival mode!"))
                        return true;

                    Player target = Bukkit.getPlayer(strings[0]);

                    target.setGameMode(GameMode.SURVIVAL);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", target.getGameMode().toString())).send(target);

                    commandSender.sendMessage(new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-target").replace("GAMEMODE", target.getGameMode().toString()).replace("PLAYERT", target.getName())).get());

                } else {
                    commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bgmc (Player)");
                }

                return true;

            case "xg7lobbygmsp":

                if (strings.length == 0) {

                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
                        return true;
                    }

                    Player player = (Player) commandSender;

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_SPECTATOR, ErrorMessages.NO_PEMISSION.getMessage()))
                        return true;

                    player.setGameMode(GameMode.SPECTATOR);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", player.getGameMode().toString())).send(player);

                } else if (strings.length == 1) {

                    if (!PluginUtil.hasPermission(commandSender, PermissionType.GAMEMODE_SPECTATOR, ErrorMessages.NO_PEMISSION.getMessage() + "With spectator mode!"))
                        return true;

                    Player target = Bukkit.getPlayer(strings[0]);

                    target.setGameMode(GameMode.SPECTATOR);

                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-player").replace("GAMEMODE", target.getGameMode().toString())).send(target);

                    commandSender.sendMessage(new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.gamemode-target").replace("GAMEMODE", target.getGameMode().toString()).replace("PLAYERT", target.getName())).get());

                } else {
                    commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bgmc (Player)");
                }

                return true;
        }

        return true;
    }
}
