package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others;

import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Module.Inventories.Actions.Action;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
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

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString()) != null ? PlayersManager.getData(player.getUniqueId().toString()) : PlayersManager.createData(player);
        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("first-join-message.enabled")) {

            if (data.getFirstJoinLong() == 0) {
                data.setFirstJoin(System.currentTimeMillis());

                event.setJoinMessage(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("first-join-message.join-message"), player));

                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("first-join-message.for-everyone")) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        PluginUtil.playSound(players, configManager.getConfig(ConfigType.CONFIG).getString("first-join-message.especial-sound"));
                    }
                } else {
                    PluginUtil.playSound(player, configManager.getConfig(ConfigType.CONFIG).getString("first-join-message.especial-sound"));
                }

                PlayersManager.update(data.getId(), data);
            } else {
                event.setJoinMessage(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("join-message"), player));
            }

        } else {

            event.setJoinMessage(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("join-message"), player));

            if (data.getFirstJoinLong() == 0) {
                data.setFirstJoin(System.currentTimeMillis());
                PlayersManager.update(data.getId(), data);
            }

        }

        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("tp-when-join")) {
            Location location = new LobbyLocation().getLocation();
            if (location != null) {
                player.teleport(location);
            } else {
                if (PluginUtil.hasPermission(player, PermissionType.SETLOBBY_COMMAND, configManager.getConfig(ConfigType.MESSAGES).getString("commands.lobby-warn"))) {
                    TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.adm-lobby-warn"), player);
                }
            }

        }

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (PluginUtil.isInWorld(player)) {
                for (String s : configManager.getConfig(ConfigType.CONFIG).getStringList("join-events.actions"))
                    new Action(player, s).execute();

            }
        }, 10);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("leave-message"), player));
    }




}
