package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Module.Selectors.Selector;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class Vanish implements CommandExecutor, Listener {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }
        if (PluginUtil.hasPermission(commandSender, PermissionType.VANISH, ErrorMessages.NO_PEMISSION.getMessage())) {

            Player player = (Player) commandSender;

            if (!PluginUtil.isInWorld(player)) {
                TextUtil.send(ErrorMessages.NOT_IN_WORLD.getMessage(), player);
                return true;
            }

            PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

            if (data.isPlayershide()) {

                for (Player target : Bukkit.getOnlinePlayers()) player.hidePlayer(target);
                data.setPlayershide(true);
                PlayersManager.update(player.getUniqueId().toString(), data);

            } else {

                for (Player target : Bukkit.getOnlinePlayers()) player.showPlayer(target);
                data.setPlayershide(false);
                PlayersManager.update(player.getUniqueId().toString(), data);

            }

        }

        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) {
                for (Player target : Bukkit.getOnlinePlayers()) player.showPlayer(target);
            } else {
                PlayerData data = PlayersManager.getData(player.getUniqueId().toString());
                if (data.isPlayershide()) {

                    for (Player target : Bukkit.getOnlinePlayers()) player.hidePlayer(target);

                }
            }


        }, 15);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (Players.getPlayers().containsKey(player.getUniqueId())) {
                PlayerData data = PlayersManager.getData(player.getUniqueId().toString());
                if (data.isPlayershide()) {

                    for (Player target : Bukkit.getOnlinePlayers()) player.hidePlayer(target);

                }
            }
        }, 15);
    }
}
