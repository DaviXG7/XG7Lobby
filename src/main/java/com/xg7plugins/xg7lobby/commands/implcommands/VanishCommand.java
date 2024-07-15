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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VanishCommand implements Command {

    @Override
    public String getName() {
        return "xg7lobbyvanish";
    }

    @Override
    public String getDescription() {
        return "Hide or show players";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyvanish (Player)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return true;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.VANISH;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1) {

            if (!sender.hasPermission(PermissionType.VANISH_OTHER.getPerm())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-doesnt-exist"), sender);
                return true;
            }

            PlayerData data = PlayerManager.getPlayerData(target.getUniqueId());
            data.setPlayerHiding(!data.isPlayerHiding());
            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
            SQLHandler.update("UPDATE players SET isplayershide = ? WHERE id = ?", data.isPlayerHiding(), data.getId());

            Text.send(data.isPlayerHiding() ? Config.getString(ConfigType.MESSAGES, "vanish.on-enable") : Config.getString(ConfigType.MESSAGES, "build.on-disable"), target);
            Text.send(data.isPlayerHiding() ? Config.getString(ConfigType.MESSAGES, "vanish.on-enable-other").replace("[PLAYER]", target.getName()) : Config.getString(ConfigType.MESSAGES, "vanish.on-disable-other").replace("[PLAYER]", target.getName()), sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
            return true;
        }

        Player player = (Player) sender;

        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());
        data.setPlayerHiding(!data.isPlayerHiding());
        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
        SQLHandler.update("UPDATE players SET isplayershide = ? WHERE id = ?", data.isPlayerHiding(), data.getId());

        Text.send(data.isPlayerHiding() ? Config.getString(ConfigType.MESSAGES, "vanish.on-enable") : Config.getString(ConfigType.MESSAGES, "vanish.on-disable"), player);

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
