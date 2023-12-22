package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others;

import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Action.Action;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
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

        TextUtil message = new TextUtil(configManager.getConfig(ConfigType.CONFIG).getString("join-message"));
        event.setJoinMessage(message.get(player));

        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("tp-when-join")) {
            Location location = new LobbyLocation().getLocation();
            if (location != null) {
                player.teleport(location);
            } else {
                if (PluginUtil.hasPermission(player, PermissionType.SETLOBBY_COMMAND, configManager.getConfig(ConfigType.MESSAGES).getString("commands.lobby-warn"))) {
                    TextUtil message1 = new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.adm-lobby-warn"));
                    message1.send(player);
                }
            }

            Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {

                PlayerData data = PlayersManager.getData(player.getUniqueId().toString());
                new Action(player, data.isPlayershide() ? "HIDE" : "SHOW").execute();

            }, 10l);
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
        TextUtil message = new TextUtil(configManager.getConfig(ConfigType.CONFIG).getString("leave-message"));
        event.setQuitMessage(message.get(player));
    }




}
