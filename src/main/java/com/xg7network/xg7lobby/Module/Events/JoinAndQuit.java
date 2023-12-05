package com.xg7network.xg7lobby.Module.Events;

import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Action.Action;
import com.xg7network.xg7lobby.Utils.Text.Message;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class JoinAndQuit implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Message message = new Message(configManager.getConfig(ConfigType.CONFIG).getString("join-message"), player);
        event.setJoinMessage(message.getMessage(player));

        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("tp-when-join")) {
            Location location = new LobbyLocation().getLocation();
            if (location != null) {
                player.teleport(location);
            } else {
                if (PluginUtil.hasPermission(player, PermissionType.SETLOBBY_COMMAND, configManager.getConfig(ConfigType.MESSAGES).getString("commands.lobby-warn"))) {
                    Message message1 = new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.adm-lobby-warn"), player);
                    message1.sendMessage();
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (PluginUtil.isInWorld(player)) {
                for (String s : configManager.getConfig(ConfigType.CONFIG).getStringList("join-events.actions"))
                    new Action(player, s).execute();
            }
        }, 10l);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Message message = new Message(configManager.getConfig(ConfigType.CONFIG).getString("leave-message"), player);
        event.setQuitMessage(message.getMessage(player));
    }




}
