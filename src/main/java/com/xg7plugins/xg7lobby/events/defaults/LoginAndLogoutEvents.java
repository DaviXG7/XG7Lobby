package com.xg7plugins.xg7lobby.events.defaults;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;

import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.events.LobbyEvent;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class LoginAndLogoutEvents implements LobbyEvent {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    @SneakyThrows
    public void onJoin(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");


        LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).thenAccept(lobbyPlayer -> {

            Player player = lobbyPlayer.getPlayer();

            boolean messageOnlyInLobby = config.get("on-join.send-join-message-only-on-lobby", Boolean.class).orElse(false);

            boolean firstJoinEnabled = config.get("on-first-join.enabled", Boolean.class).orElse(false);
            boolean sendJoinMessage = config.get("on-join.send-join-message", Boolean.class).orElse(true);

            if (sendJoinMessage) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (
                            !XG7Lobby.getInstance().getEnabledWorlds().contains(p.getWorld().getName())
                                    && messageOnlyInLobby
                    ) return;
                    Text.formatLang(XG7Lobby.getInstance(), p, firstJoinEnabled && lobbyPlayer.isFirstJoin() ? "messages.on-first-join" : "messages.on-join").join()
                            .replace("[PLAYER]", player.getName())
                            .send(p);
                });
            }

            if (!XG7Lobby.getInstance().isInWorldEnabled(player)) return;

            onWorldJoin(player, player.getWorld());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).thenAccept(lobbyPlayer -> {

            Player player = lobbyPlayer.getPlayer();

            Text.formatLang(XG7Lobby.getInstance(), player, "messages.on-quit")
                    .join().send(player.getPlayer());
        });
    }

    @Override
    public void onWorldJoin(Player player, World newWorld) {
        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.fly();

        if (player.getWorld() == newWorld || config.get("on-join.run-events-when-return-to-the-world", Boolean.class).orElse(false)) XG7Lobby.getInstance().getActionsProcessor().process(config.get("on-first-join.enabled", Boolean.class).orElse(false) && lobbyPlayer.isFirstJoin() ? "on-first-join" : "on-join", player);

        if (config.get("on-join.tp-to-lobby", Boolean.class).orElse(true)) {

            XG7Lobby.getInstance().getLobbyManager().getALobbyByPlayer(player.getPlayer()).thenAccept(lobby -> {
                if (lobby.getLocation() == null) {
                    Text.formatLang(XG7Lobby.getInstance(), player, "lobby.on-teleport." + (player.hasPermission("xg7lobby.command.setlobby") ? "on-error-doesnt-exist-adm" : "on-error-doesnt-exist"))
                            .join().send(player.getPlayer());
                    return;
                }
                lobby.teleport(player.getPlayer());
            });

        }

        LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m instanceof LobbySelector).map(m -> (LobbySelector) m).findFirst().orElse(null);

        if (menu != null) menu.open(player);

        player.setMaxHealth(config.get("hearts", Double.class).orElse(10D) * 2);
        player.setFoodLevel(config.get("hunger", Integer.class).orElse(10) * 2);
        if (config.get("on-join.heal", Boolean.class).orElse(true)) player.setHealth(player.getMaxHealth());
        if (config.get("on-join.clear-inventory", Boolean.class).orElse(true)) player.getInventory().clear();
        lobbyPlayer.setFirstJoin(false);
        XG7Lobby.getInstance().getPlayerDAO().update(lobbyPlayer);
    }

    @Override
    public void onWorldLeave(Player player, World newWorld) {
        player.closeInventory();
        if (XG7Plugins.getInstance().getMenuManager().hasPlayerMenu(player.getUniqueId())) {
            player.getInventory().clear();
            XG7Plugins.getInstance().getMenuManager().removePlayerMenu(player.getUniqueId());
        }

        player.setMaxHealth(20);
        player.setHealth(20);
        player.getInventory().clear();
        player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }


}
