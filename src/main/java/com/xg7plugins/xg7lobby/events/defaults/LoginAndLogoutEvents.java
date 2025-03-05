package com.xg7plugins.xg7lobby.events.defaults;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;

import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.modules.xg7menus.XG7Menus;
import com.xg7plugins.modules.xg7menus.menus.holders.PlayerMenuHolder;
import com.xg7plugins.modules.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.modules.xg7scores.XG7Scores;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


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
                    Text.fromLang(p,XG7Lobby.getInstance(), firstJoinEnabled && lobbyPlayer.isFirstJoin() ? "messages.on-first-join" : "messages.on-join").join()
                            .replace("target", player.getName())
                            .send(p);
                });
            }

            if (!XG7Lobby.getInstance().isInWorldEnabled(player)) return;

            XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> {
                XG7Scores.getInstance().addPlayer(player);
                onWorldJoin(player, player.getWorld());
            });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) XG7Lobby.getInstance().getGlobalPVPManager().removePlayerFromPVP(event.getPlayer());
        LobbyPlayer.cast(event.getPlayer().getUniqueId(), true).thenAccept(lobbyPlayer -> {

            Player player = lobbyPlayer.getPlayer();

            if (XG7Menus.getInstance().hasPlayerMenuHolder(player.getUniqueId())) {
                player.getInventory().clear();
                XG7Menus.getInstance().removePlayerMenuHolder(player.getUniqueId());
            }

            boolean messageOnlyInLobby = XG7Lobby.getInstance().getConfig("config").get("on-join.send-join-message-only-on-lobby", Boolean.class).orElse(false);

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (
                        !XG7Lobby.getInstance().getEnabledWorlds().contains(p.getWorld().getName())
                                && messageOnlyInLobby
                ) return;
                Text.fromLang(p, XG7Lobby.getInstance(), "messages.on-quit").join()
                        .replace("target", player.getName())
                        .send(p);
            });
        });
    }

    @Override
    public void onWorldJoin(Player player, World newWorld) {

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.fly();

        lobbyPlayer.setPlayerHiding(lobbyPlayer.isPlayerHiding());

        Bukkit.getOnlinePlayers().stream().filter(p -> XG7Lobby.getInstance().isInWorldEnabled(p) && XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(p) && Config.mainConfigOf(XG7Lobby.getInstance()).get("global-pvp.hide-players-not-in-pvp", Boolean.class).orElse(false)).forEach(p -> p.hidePlayer(player));

        if (player.getWorld() == newWorld || config.get("on-join.run-events-when-return-to-the-world", Boolean.class).orElse(false)) XG7Lobby.getInstance().getActionsProcessor().process(config.get("on-first-join.enabled", Boolean.class).orElse(false) && lobbyPlayer.isFirstJoin() ? "on-first-join" : "on-join", player);

        if (config.get("on-join.tp-to-lobby", Boolean.class).orElse(true)) {

            XG7Lobby.getInstance().getLobbyManager().getRandomLobby().thenAccept(lobby -> {
                if (lobby.getLocation() == null) {
                    Text.fromLang(player,XG7Lobby.getInstance(), "lobby.on-teleport.on-error-doesnt-exist" + (player.hasPermission("xg7lobby.command.lobby.set") ? "-adm" : "")).thenAccept(text -> text.send(player));
                    return;
                }
                XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> lobby.teleport(player));
            });

        }

        LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m.getId().equals(config.get("main-selector-id", String.class).orElse(null))).map(m -> (LobbySelector) m).findFirst().orElse(null);

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

        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(player)) XG7Lobby.getInstance().getGlobalPVPManager().removePlayerFromPVP(player);
        player.closeInventory();
        PlayerMenuHolder menu = XG7Menus.getInstance().getPlayerMenuHolder(player.getUniqueId());

        if (menu != null) ((PlayerMenu)menu.getMenu()).close(player);

        Bukkit.getOnlinePlayers().forEach(player::showPlayer);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!XG7Lobby.getInstance().isInWorldEnabled(event.getPlayer())) return;

        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) return;

        Player player = event.getPlayer();

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.fly();

        lobbyPlayer.setPlayerHiding(lobbyPlayer.isPlayerHiding());

        LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m.getId().equals(config.get("main-selector-id", String.class).orElse(null))).map(m -> (LobbySelector) m).findFirst().orElse(null);

        if (menu != null) menu.open(player);

        player.setMaxHealth(config.get("hearts", Double.class).orElse(10D) * 2);
        player.setFoodLevel(config.get("hunger", Integer.class).orElse(10) * 2);
        if (config.get("global-pvp.on-leave-pvp.heal", Boolean.class).orElse(true)) player.setHealth(player.getMaxHealth());
        if (config.get("global-pvp.on-leave-pvp.clear-inventory", Boolean.class).orElse(true)) player.getInventory().clear();

    }


}
