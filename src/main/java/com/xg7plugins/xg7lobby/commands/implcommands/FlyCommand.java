package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.jumpevents.DoubleJumpEvent;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlyCommand implements Command {

    @Override
    public String getName() {
        return "xg7lobbyfly";
    }

    @Override
    public String getDescription() {
        return "Enables flight on lobby";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyfly (Player)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return true;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.FLY;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 1) {

            if (!sender.hasPermission(PermissionType.FLY_OTHER.getPerm())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-doesnt-exist"), sender);
                return true;
            }

            if (target.getGameMode().equals(GameMode.CREATIVE) || target.getGameMode().equals(GameMode.SPECTATOR)) {
                Text.send(Config.getString(ConfigType.MESSAGES, "fly.gamemode-other"), sender);
                return true;
            }

            PlayerData data = PlayerManager.getPlayerData(target.getUniqueId());

            if (data.isPVPEnabled() && !data.isFlying() && Config.getBoolean(ConfigType.CONFIG, "pvp.disable-fly")) {
                Text.send(Config.getString(ConfigType.MESSAGES, "fly.on-pvp"), sender);
                return true;
            }

            target.setAllowFlight(!data.isFlying());
            data.setFlying(!data.isFlying());
            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

            if (data.isFlying()) DoubleJumpEvent.isJumping.remove(target.getUniqueId());
            else DoubleJumpEvent.isJumping.add(target.getUniqueId());

            SQLHandler.update("UPDATE players SET isflying = ? WHERE id = ?", data.isFlying(), data.getId());

            Text.send(data.isFlying() ? Config.getString(ConfigType.MESSAGES, "fly.on-enable") : Config.getString(ConfigType.MESSAGES, "fly.on-disable"), target);
            Text.send(data.isFlying() ? Config.getString(ConfigType.MESSAGES, "fly.on-enable-other").replace("[PLAYER]", target.getName()) : Config.getString(ConfigType.MESSAGES, "fly.on-disable-other").replace("[PLAYER]", target.getName()), sender);
            return true;

        }

        if (!(sender instanceof Player)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
            return true;
        }

        Player player = (Player) sender;

        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "fly.gamemode"), sender);
            return true;
        }

        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());

        if (data.isPVPEnabled() && !data.isFlying() && Config.getBoolean(ConfigType.CONFIG, "pvp.disable-fly")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "fly.on-pvp"), sender);
            return true;
        }

        player.setAllowFlight(!data.isFlying());

        data.setFlying(!data.isFlying());

        if (data.isFlying()) DoubleJumpEvent.isJumping.remove(player.getUniqueId());
        else DoubleJumpEvent.isJumping.add(player.getUniqueId());

        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

        SQLHandler.update("UPDATE players SET isflying = ? WHERE id = ?", data.isFlying(), data.getId());

        Text.send(data.isFlying() ? Config.getString(ConfigType.MESSAGES, "fly.on-enable") : Config.getString(ConfigType.MESSAGES, "fly.on-disable"), player);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
