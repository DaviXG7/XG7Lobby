package com.xg7plugins.xg7lobby.commands.implcommands.lobby;

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
import com.xg7plugins.xg7lobby.menus.SelectorManager;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuildCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbybuild";
    }
    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.CRAFTING_TABLE.parseMaterial(), "&6Reload command", Arrays.asList("&9Description: " + getDescription(), "&9Usage: &7&o" + getSyntax(), "&9Permission: &b" + getPermission().getPerm()), 1, -1);
    }
    @Override
    public String getDescription() {
        return "Enables the build on the lobby";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbybuild (Player)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return true;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.BUILD;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 1) {

            if (!sender.hasPermission(PermissionType.BUILD_OTHER.getPerm())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-doesnt-exist"), sender);
                return true;
            }

            PlayerData data = PlayerManager.createPlayerData(target.getUniqueId());
            data.setBuildEnabled(!data.isBuildEnabled());
            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
            SQLHandler.update("UPDATE players SET isbuildenabled = ? WHERE id = ?", data.isBuildEnabled(), data.getId());

            if (Config.getBoolean(ConfigType.SELECTOR, "enabled")) {
                if (data.isBuildEnabled()) SelectorManager.getMenu().close(target);
                else SelectorManager.getMenu().open(target);
            }


            Text.send(data.isBuildEnabled() ? Config.getString(ConfigType.MESSAGES, "build.on-enable") : Config.getString(ConfigType.MESSAGES, "build.on-disable"), target);
            Text.send(data.isBuildEnabled() ? Config.getString(ConfigType.MESSAGES, "build.on-enable-other").replace("[PLAYER]", target.getName()) : Config.getString(ConfigType.MESSAGES, "build.on-disable-other").replace("[PLAYER]", target.getName()), sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
            return true;
        }

        Player player = (Player) sender;

        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());
        data.setBuildEnabled(!data.isBuildEnabled());
        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
        SQLHandler.update("UPDATE players SET isbuildenabled = ? WHERE id = ?", data.isBuildEnabled(), data.getId());

        if (Config.getBoolean(ConfigType.SELECTOR, "enabled")) {
            if (data.isBuildEnabled()) SelectorManager.getMenu().close(player);
            else SelectorManager.getMenu().open(player);
        }

        Text.send(data.isBuildEnabled() ? Config.getString(ConfigType.MESSAGES, "build.on-enable") : Config.getString(ConfigType.MESSAGES, "build.on-disable"), player);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
