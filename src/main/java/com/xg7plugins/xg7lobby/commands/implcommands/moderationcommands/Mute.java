package com.xg7plugins.xg7lobby.commands.implcommands.moderationcommands;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.data.player.model.Warn;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.*;
import java.util.stream.Collectors;

public class Mute {

    protected static void mute(PlayerData data, long time, String message, CommandSender sender) {
        data.setMuted(true);
        data.setTimeForUnmute(time);
        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
        SQLHandler.update("UPDATE players SET ismuted = ?, timeforunmute = ? WHERE id = ?", data.isMuted(), data.getTimeForUnmute(), data.getId());
        if (!message.isEmpty()) {

            int level = Config.getInt(ConfigType.CONFIG, "warn-level-mute");

            if (level < 1 || level > 3) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-invalid-level").replace("[LEVEL]", level + ""), sender);
                return;
            }

            Warn warn = new Warn(level, Text.translateColorCodes(message), System.currentTimeMillis(), UUID.randomUUID());

            data.getInfractions().add(warn);

            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

            SQLHandler.update("INSERT INTO warns (playerid, level, warnid, warn, whenw) VALUES (?,?,?,?,?)", data.getId(), warn.getLevel(), warn.getId().toString(), warn.getWarn(), warn.getDate());
        }
    }

    public static class MuteCommand implements Command {
        @Override
        public String getName() {
            return "xg7lobbymute";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.OAK_SIGN.parseItem(), "§6Mute command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Mutes a player";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbymute [Player] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.MUTE;
        }

        @Override
        public boolean isOnlyPlayer() {
            return false;
        }

        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

            if (args.length < 1) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }
            if (target.isOp() && !Config.getBoolean(ConfigType.CONFIG, "warn-admin")) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-permission"), sender);
                return true;
            }

            String message = args.length >= 2 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) : "";

            mute(PlayerManager.createPlayerData(target.getUniqueId()), 0, message, sender);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-mute").replace("[PLAYER]", target.getName()), sender);

            if (target.isOnline()) Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-mute-other"), target.getPlayer());

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
        }
    }
    public static class TempmuteCommand implements Command {
        @Override
        public String getName() {
            return "xg7lobbytempmute";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.OAK_SIGN.parseItem(), "§6Tempmute command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Mutes a player for a time";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbytempmute [Player] [Time] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.TEMPMUTE;
        }

        @Override
        public boolean isOnlyPlayer() {
            return false;
        }

        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

            if (args.length < 2) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }
            if (target.isOp() && !Config.getBoolean(ConfigType.CONFIG, "warn-admin")) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-permission"), sender);
                return true;
            }

            String message = args.length >= 3 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 2, args.length))) : "";

            mute(PlayerManager.createPlayerData(target.getUniqueId()), Text.convertToMilliseconds(args[1]) + System.currentTimeMillis(), message, sender);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-mute").replace("[PLAYER]", target.getName()), sender);

            if (target.isOnline()) Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-mute-other"), target.getPlayer());

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : args.length == 2 ? Arrays.asList("1h", "1m", "1s", "1d") : Collections.singletonList("Reason");
        }
    }
    public static class UnmuteCommand implements Command {
        @Override
        public String getName() {
            return "xg7lobbyunmute";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.OAK_SIGN.parseItem(), "§6Tempmute command", Arrays.asList("§9Description: §f" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Mutes a player for a time";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbyunmute [Player]";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.UNMUTE;
        }

        @Override
        public boolean isOnlyPlayer() {
            return false;
        }

        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

            if (args.length < 1) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }
            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());

            data.setMuted(false);
            data.setTimeForUnmute(0);

            SQLHandler.update("UPDATE players SET ismuted = ?, timeforunmute = ? WHERE id = ?", data.isMuted(), data.getTimeForUnmute(), data.getId());

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-unmute").replace("[PLAYER]", target.getName()), sender);

            if (target.isOnline()) Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-unmute-other"), target.getPlayer());

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : args.length == 2 ? Arrays.asList("1h", "1m", "1s", "1d") : Collections.singletonList("Reason");
        }
    }
}
