package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.commands.SubCommand;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.jumpevents.DoubleJumpEvent;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GamemodeCommand implements Command {

    @Override
    public String getName() {
        return "xg7lobbygamemode";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.GRASS_BLOCK.parseItem(), "§6Gamemode command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Changes the gamemode";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbygamemode [s,c,sp,a] (Player)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.GAMEMODE;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return Arrays.asList(new GamemodeAdventure(), new GamemodeSpectator(), new GamemodeSurvival(), new GamemodeCreative());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Arrays.asList("c", "s", "a", "sp");
    }

    static class GamemodeSurvival implements SubCommand {

        @Override
        public String getName() {
            return "s";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.GAMEMODE_SURVIVAL;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length == 2) {
                if (!sender.hasPermission(PermissionType.GAMEMODE_OTHERS.getPerm())) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                    return;
                }
                if (Config.getBoolean(ConfigType.CONFIG, "double-jump.enabled")) {
                    target.setAllowFlight(target.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
                    DoubleJumpEvent.isJumping.add(target.getUniqueId());
                }
                target.setGameMode(GameMode.SURVIVAL);

                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", target.getGameMode().name()), target);
                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change-other").replace("[GAMEMODE]", target.getGameMode().name()), sender);
                return;
            }

            if (!(sender instanceof Player)) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
                return;
            }

            Player player = (Player) sender;

            player.setGameMode(GameMode.SURVIVAL);

            if (Config.getBoolean(ConfigType.CONFIG, "double-jump.enabled")) {
                player.setAllowFlight(player.hasPermission(PermissionType.DOUBLE_JUMP.getPerm()));
                DoubleJumpEvent.isJumping.add(player.getUniqueId());
            }
            Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", player.getGameMode().name()), player);

        }
    }
    static class GamemodeCreative implements SubCommand {

        @Override
        public String getName() {
            return "c";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.GAMEMODE_SURVIVAL;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length == 2) {
                if (!sender.hasPermission(PermissionType.GAMEMODE_OTHERS.getPerm())) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                    return;
                }
                target.setGameMode(GameMode.CREATIVE);

                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", target.getGameMode().name()), target);
                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change-other").replace("[GAMEMODE]", target.getGameMode().name()), sender);
                return;
            }

            if (!(sender instanceof Player)) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
                return;
            }

            Player player = (Player) sender;

            player.setGameMode(GameMode.CREATIVE);
            Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", player.getGameMode().name()), player);

        }
    }
    static class GamemodeSpectator implements SubCommand {

        @Override
        public String getName() {
            return "sp";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.GAMEMODE_SURVIVAL;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length == 2) {
                if (!sender.hasPermission(PermissionType.GAMEMODE_OTHERS.getPerm())) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                    return;
                }
                target.setGameMode(GameMode.SPECTATOR);

                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", target.getGameMode().name()), target);
                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change-other").replace("[GAMEMODE]", target.getGameMode().name()), sender);
                return;
            }

            if (!(sender instanceof Player)) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
                return;
            }

            Player player = (Player) sender;

            player.setGameMode(GameMode.SPECTATOR);
            Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", player.getGameMode().name()), player);

        }
    }
    static class GamemodeAdventure implements SubCommand {

        @Override
        public String getName() {
            return "a";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.GAMEMODE_SURVIVAL;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length == 2) {
                if (!sender.hasPermission(PermissionType.GAMEMODE_OTHERS.getPerm())) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                    return;
                }
                target.setGameMode(GameMode.ADVENTURE);

                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", target.getGameMode().name()), target);
                Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change-other").replace("[GAMEMODE]", target.getGameMode().name()), sender);
                return;
            }

            if (!(sender instanceof Player)) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
                return;
            }

            Player player = (Player) sender;

            player.setGameMode(GameMode.ADVENTURE);
            Text.send(Config.getString(ConfigType.MESSAGES, "gamemode.on-change").replace("[GAMEMODE]", player.getGameMode().name()), player);

        }
    }
}
