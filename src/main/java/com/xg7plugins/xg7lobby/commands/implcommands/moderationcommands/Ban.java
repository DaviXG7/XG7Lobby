package com.xg7plugins.xg7lobby.commands.implcommands.moderationcommands;


import com.cryptomorin.xseries.XMaterial;
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
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Ban {

    protected static void ban(@NotNull OfflinePlayer player, String message, Date timeUnban, CommandSender sender, boolean banIp) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), Text.translateColorCodes(message), timeUnban, sender.getName());
        if (player.isOnline()) {
            if (banIp) Bukkit.getBanList(BanList.Type.IP).addBan(player.getPlayer().getAddress().getAddress().getHostName(), Text.translateColorCodes(message), timeUnban, sender.getName());
            player.getPlayer().kickPlayer(Text.translateColorCodes(message));
            if (!message.isEmpty()) {
                PlayerData data = PlayerManager.createPlayerData(player.getUniqueId());

                int level = Config.getInt(ConfigType.CONFIG, "warn-level-ban");

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
    }

    public static class BanCommand implements Command {

        @Override
        public String getName() {
            return "xg7lobbyban";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.IRON_AXE.parseMaterial(), "§6Ban command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: &7&o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Bans a player";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbyban [Player] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.BAN;
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

            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());

            String message = args.length >= 2 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) : "";

            Ban.ban(target, message, null, sender, false);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-ban").replace("[PLAYER]", target.getName()), sender);

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : Collections.singletonList("Reason");
        }
    }
    public static class BanipCommand implements Command {

        @Override
        public String getName() {
            return "xg7lobbybanip";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.IRON_AXE.parseMaterial(), "§6Banip command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Bans a player by ip";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbyban [Player] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.BANIP;
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
            if (!target.isOnline()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-online"), sender);
                return true;
            }

            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());

            String message = args.length >= 2 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) : "";

            Ban.ban(target, message, null, sender, true);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-ban").replace("[PLAYER]", target.getName()), sender);

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : Collections.singletonList("Reason");
        }
    }

    public static class TempBanCommand implements Command {

        @Override
        public String getName() {
            return "xg7lobbytempban";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.IRON_AXE.parseMaterial(), "§6TempBan command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Bans a player for a time period!";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbytempban [Player] [Time] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.TEMPBAN;
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

            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());

            String message = args.length >= 3 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 2, args.length))) : "";

            Ban.ban(target, message, new Date(System.currentTimeMillis() + Text.convertToMilliseconds(args[1])), sender, false);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-ban").replace("[PLAYER]", target.getName()), sender);

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : args.length == 2 ? Arrays.asList("1h", "1m", "1s", "1d") : Collections.singletonList("Reason");
        }
    }

    public static class UnbanCommand implements Command {

        @Override
        public String getName() {
            return "xg7lobbyunban";
        }

        @Override
        public InventoryItem getIcon() {
            return new InventoryItem(XMaterial.IRON_AXE.parseMaterial(), "§6Unban command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
        }

        @Override
        public String getDescription() {
            return "Unbans a player";
        }

        @Override
        public String getSyntax() {
            return "/xg7lobbyban [Player] (Reason)";
        }

        @Override
        public boolean isOnlyInLobbyWorld() {
            return false;
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.UNBAN;
        }

        @Override
        public boolean isOnlyPlayer() {
            return false;
        }

        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (args.length != 1) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", getSyntax()), sender);
                return true;
            }
            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }

            Bukkit.getBanList(BanList.Type.NAME).pardon(target.getName());

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-unban").replace("[PLAYER]", target.getName()), sender);

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return args.length == 1 ? Bukkit.getBanList(org.bukkit.BanList.Type.NAME).getBanEntries().stream().map(BanEntry::getTarget).collect(Collectors.toList()) : new ArrayList<>();
        }
    }

}
