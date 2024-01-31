package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.getPlugin;

public class Lobby implements CommandExecutor, Listener {

    private static final HashMap<UUID, Integer> cooldown = new HashMap<>();
    private static final HashMap<UUID, BukkitTask> tasks = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {



            Player player = (Player) commandSender;
            LobbyLocation location = new LobbyLocation();

            if (location.getLocation() == null) {
                if (PluginUtil.hasPermission(commandSender, PermissionType.SETLOBBY_COMMAND, configManager.getConfig(ConfigType.MESSAGES).getString("commands.lobby-warn")))
                    TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.adm-lobby-warn"), player);
                return true;
            }

            if (!cooldown.containsKey(player.getUniqueId())) {

                cooldown.put(player.getUniqueId(), configManager.getConfig(ConfigType.CONFIG).getInt("lobby.tp-cooldown"));

                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-lobby-teleport").replace("[SECONDS]", cooldown.get(player.getUniqueId()) + ""), player);

                tasks.put(player.getUniqueId(), new BukkitRunnable() {

                    @Override
                    public void run() {
                        TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.lobby-cooldown-message").replace("[SECONDS]", cooldown.get(player.getUniqueId()) + ""), player);

                        cooldown.put(player.getUniqueId(), cooldown.get(player.getUniqueId()) - 1);

                        if (cooldown.get(player.getUniqueId()) <= 0) {

                            cooldown.remove(player.getUniqueId());
                            player.teleport(location.getLocation());
                            tasks.remove(player.getUniqueId());
                            cancel();

                        }



                    }
                }.runTaskTimer(getPlugin(), 0, 20));

            } else {

                tasks.get(player.getUniqueId()).cancel();
                tasks.remove(player.getUniqueId());
                cooldown.remove(player.getUniqueId());
                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.teleport-cancelled"), player);

            }

        } else {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
        }
        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("lobby.dont-move")) {
            Player player = event.getPlayer();

            if (cooldown.containsKey(player.getUniqueId())) {

                if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                        event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                        event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    tasks.get(player.getUniqueId()).cancel();
                    tasks.remove(player.getUniqueId());
                    cooldown.remove(player.getUniqueId());
                    TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.teleport-cancelled"), player);
                }

            }
        }

    }
}
