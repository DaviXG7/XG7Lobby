package com.xg7plugins.xg7lobby.commands.implcommands.togglecommands;

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
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VanishCommand implements Command {

    @Override
    public String getName() {
        return "xg7lobbyvanish";
    }
    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.ENDER_EYE.parseItem().getData(), "&6Vanish command", Arrays.asList("&9Description: " + getDescription(), "&9Usage: &7&o" + getSyntax(), "&9Permission: &b" + getPermission().getPerm()), 1, -1);
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

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }

            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());
            data.setPlayerHiding(!data.isPlayerHiding());
            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
            SQLHandler.update("UPDATE players SET isplayershide = ? WHERE id = ?", data.isPlayerHiding(), data.getId());

            if (target.isOnline()) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (data.isPlayerHiding()) target.getPlayer().hidePlayer(player);
                    else target.getPlayer().showPlayer(player);
                });

                Text.send(data.isPlayerHiding() ? Config.getString(ConfigType.MESSAGES, "vanish.on-enable") : Config.getString(ConfigType.MESSAGES, "build.on-disable"), target.getPlayer());
            }
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
        Bukkit.getOnlinePlayers().forEach(player1 -> {
            if (data.isPlayerHiding()) player.hidePlayer(player1);
            else player.showPlayer(player1);
        });

        Text.send(data.isPlayerHiding() ? Config.getString(ConfigType.MESSAGES, "vanish.on-enable") : Config.getString(ConfigType.MESSAGES, "vanish.on-disable"), player);

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
