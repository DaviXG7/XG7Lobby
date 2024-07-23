package com.xg7plugins.xg7lobby.commands.implcommands.moderationcommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.commands.SubCommand;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.data.player.model.Warn;
import com.xg7plugins.xg7lobby.events.commandevents.WarnsCommandEvent;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WarnCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbywarn";
    }
    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.NAME_TAG.parseMaterial(), "§6Warn command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }
    @Override
    public String getDescription() {
        return "Warn a player";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbywarn [Player] [Warn level] [Reason] or /xg7lobbywarn remove [Warn id]";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.WARN;
    }
    @Override
    public List<SubCommand> getSubCommands() {
        return Collections.singletonList(new RemoveWarn());
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length >= 1 && args[0].equals("remove")) {
            getSubCommands().get(0).onCommand(sender,command,label,args);
            return true;
        }

        if (args.length < 3) {
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

        int level = Integer.parseInt(args[1]);

        if (level < 1 || level > 3) {
            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-invalid-level").replace("[LEVEL]", args[1]), sender);
            return true;
        }

        Warn warn = new Warn(level, String.join(" ", Arrays.copyOfRange(args, 2, args.length)), System.currentTimeMillis(), UUID.randomUUID());

        data.getInfractions().add(warn);

        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

        SQLHandler.update("INSERT INTO warns (playerid, level, warnid, warn, whenw) VALUES (?,?,?,?,?)", data.getId(), warn.getLevel(), warn.getId().toString(), warn.getWarn(), warn.getDate());

        Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-warn").replace("[LEVEL]", warn.getLevel() + "").replace("[PLAYER]", target.getName()).replace("[WARN]", warn.getWarn()), sender);

        WarnsCommandEvent.verify(data);
        if (target.isOnline()) Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-warn-other").replace("[WARN]", warn.getWarn()), target.getPlayer());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Stream.concat(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName), Stream.of("remove")).collect(Collectors.toList()) : args.length == 2 && Objects.equals(args[0], "remove") ? Collections.singletonList("warn id") : args.length == 2 ? Collections.singletonList("warn level") : args.length >= 3 && !Objects.equals(args[0], "remove") ? Collections.singletonList("reason") : new ArrayList<>();
    }

    static class RemoveWarn implements SubCommand {

        @Override
        public String getName() {
            return "remove";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.WARN_REMOVE;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

            if (args.length != 2) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.syntax-error").replace("[SYNTAX]", "/xg7lobbywarn remove [Warn id]"), sender);
                return;
            }

            List<Map<String, Object>> select = SQLHandler.select("SELECT * FROM warns WHERE warnid = ?", args[1]);

            if (select.isEmpty()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "moderation.warn-not-found").replace("[ID]", args[1]), sender);
                return;
            }

            PlayerData data = PlayerManager.getPlayerData(UUID.fromString((String) select.get(0).get("playerid")));

            data.getInfractions().remove(data.getWarnById(UUID.fromString((String) select.get(0).get("warnid"))));

            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

            SQLHandler.update("DELETE FROM warns WHERE warnid = ?", args[1]);

            Text.send(Config.getString(ConfigType.MESSAGES, "moderation.on-remove").replace("[ID]", args[1]), sender);

        }
    }

}
