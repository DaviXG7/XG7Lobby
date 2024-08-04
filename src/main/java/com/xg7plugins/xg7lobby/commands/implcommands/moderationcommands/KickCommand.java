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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class KickCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbykick";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.IRON_BOOTS.parseMaterial(), "§6Kick command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }


    @Override
    public String getDescription() {
        return "Kicks a player";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbykick [Player] (Reason)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.KICK;
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

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
            return true;
        }
        if (target.isOp() && !Config.getBoolean(ConfigType.CONFIG, "warn-admin")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-permission"), sender);
            return true;
        }

        String message = args.length >= 2 ? message = Text.translateColorCodes(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) : "";;

        target.kickPlayer(Text.translateColorCodes(message));

        if (!message.isEmpty()) {
            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());

            int level = Config.getInt(ConfigType.CONFIG, "warn-level-kick");

            if (level < 1 || level > 3) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-invalid-level").replace("[LEVEL]", level + ""), sender);
                return true;
            }

            Warn warn = new Warn(level, message, System.currentTimeMillis(), UUID.randomUUID());

            data.getInfractions().add(warn);

            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

            SQLHandler.update("INSERT INTO warns (playerid, level, warnid, warn, whenw) VALUES (?,?,?,?,?)", data.getId(), warn.getLevel(), warn.getId().toString(), warn.getWarn(), warn.getDate());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : Collections.singletonList("Reason");
    }
}
