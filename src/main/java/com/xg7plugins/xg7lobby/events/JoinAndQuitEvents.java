package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;

import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.model.LobbyLocation;
import com.xg7plugins.xg7lobby.model.PlayerData;
import com.xg7plugins.xg7lobby.utils.LobbyEvent;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class JoinAndQuitEvents extends LobbyEvent {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    @SneakyThrows
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");
        PlayerData data = null;
        XG7Lobby.getInstance().getPlayerDAO().get(player.getUniqueId()).thenAccept(playerData -> {
            System.out.println("Depois do processo ou antes????????");
        });
        boolean isFirstJoin = false;

        System.out.println(data);

        if (data == null) {

            data = new PlayerData(player.getUniqueId());
            XG7Lobby.getInstance().getPlayerDAO().add(data);

            isFirstJoin = config.get("on-first-join.enabled");
            if (isFirstJoin) {

                boolean isOnlyOnLobby = config.get("on-join.send-join-message-only-on-lobby");

                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (isOnlyOnLobby && !XG7Lobby.getInstance().getEnabledWorlds().contains(p.getWorld().getName())) return;
                    Text.format("lang:[messages.on-first-join]", XG7Lobby.getInstance())
                            .replace("[PLAYER]", event.getPlayer().getName())
                            .send(p);
                });

                XG7Lobby.getInstance().getActionsProcessor().process("on-first-join", player);

            }

        }

        if (!isFirstJoin) {

            if (config.get("on-join.send-join-message")) {

                boolean isOnlyOnLobby = config.get("on-join.send-join-message-only-on-lobby");

                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (isOnlyOnLobby && !XG7Lobby.getInstance().getEnabledWorlds().contains(p.getWorld().getName())) return;
                    Text.format("lang:[messages.on-join]", XG7Lobby.getInstance())
                            .replace("[PLAYER]", p.getName())
                            .send(player);
                });

            }

            XG7Lobby.getInstance().getActionsProcessor().process("on-join", player);

        }

        if (config.get("on-join.tp-to-lobby")) {
            LobbyLocation location = XG7Plugins.getInstance().getJsonManager().load(XG7Lobby.getInstance(), "lobby.json", LobbyLocation.class);
            if (location.getLocation() == null) Text
                    .format("lang:[lobby.on-teleport." + (player.hasPermission("xg7lobby.command.setlobby") ? "on-error-doesnt-exist-adm" : "on-error-doesnt-exist") + "]", XG7Lobby.getInstance())
                    .send(player);
            else player.teleport(location.getLocation().getBukkitLocation());
        }
        if (config.get("on-join.heal")) player.setHealth(player.getMaxHealth());
        if (config.get("on-join.clear-inventory")) player.getInventory().clear();
    }



}
