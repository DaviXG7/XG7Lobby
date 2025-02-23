package com.xg7plugins.xg7lobby.events.defaults;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.modules.xg7menus.XG7Menus;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class DefaultPlayerEvents implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "break-block", "true"},
            priority = EventPriority.HIGH
    )
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.command.build")) {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            if (lobbyPlayer.isBuildEnabled())  {
                event.setCancelled(false);
                return;
            }
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "build-not-enabled").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "player-prohibitions.break-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "place-block", "true"},
            priority = EventPriority.HIGH
    )
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.command.build")) {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            if (lobbyPlayer.isBuildEnabled())  {
                event.setCancelled(false);
                return;
            }
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "build-not-enabled").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "player-prohibitions.place-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "interact-with-blocks", "true"},
            priority = EventPriority.HIGH
    )
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) return;
        if (event.getPlayer().hasPermission("xg7lobby.command.build")) {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            if (lobbyPlayer.isBuildEnabled()) {
                event.setCancelled(false);
                return;
            }
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "build-not-enabled").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "player-prohibitions.interact-with-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "drop-item", "true"},
            priority = EventPriority.HIGH
    )
    public void onDropItem(PlayerDropItemEvent event) {
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) {
            event.setCancelled(false);
            return;
        }
        if (event.getPlayer().hasPermission("xg7lobby.command.build")) {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            if (lobbyPlayer.isBuildEnabled()) {
                event.setCancelled(false);
                return;
            }
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "build-not-enabled").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "player-prohibitions.drop-items").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "pickup-item", "true"},
            priority = EventPriority.HIGH
    )
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) {
            event.setCancelled(false);
            return;
        }
        if (event.getPlayer().hasPermission("xg7lobby.command.build")) {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            if (lobbyPlayer.isBuildEnabled()) return;
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "build-not-enabled").thenAccept(text -> text.send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "player-prohibitions.pickup-items").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "take-damage", "true"}
    )
    public void onTakeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP((Player) event.getEntity())) return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "attack", "true"}
    )
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP((Player) event.getEntity()) && XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP((Player) event.getDamager())) return;
        if (event.getDamager().hasPermission("xg7lobby.attack")) return;
        event.setCancelled(true);
        Text.fromLang(event.getDamager(), XG7Lobby.getInstance(), "player-prohibitions.attack").thenAccept(text -> text.send(event.getDamager()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "food-change", "true"}
    )
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "cancel-death-by-void", "false"}
    )
    public void voidCheck(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY() < (XG7Plugins.getMinecraftVersion() > 17 ? -70 : -6)) {
            XG7Lobby.getInstance().getLobbyManager().getRandomLobby().thenAccept(lobby -> {
                if (lobby.getLocation() == null) {
                    XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation()));
                    return;
                }

                XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> lobby.teleport(event.getPlayer()));
            });
        }
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "cancel-portal-teleport", "false"},
            priority = EventPriority.LOW
    )
    public void onPortal(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        event.setCancelled(!event.getFrom().getWorld().getEnvironment().equals(event.getTo().getWorld().getEnvironment()));
    }

    @EventHandler(isOnlyInWorld = true)
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    @EventHandler(isOnlyInWorld = true)
    public void onRespawn(PlayerRespawnEvent event) {

        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer())) return;

        Player player = event.getPlayer();

        if (XG7Menus.getInstance().hasPlayerMenuHolder(player.getUniqueId())) {
            player.getInventory().clear();
            XG7Menus.getInstance().removePlayerMenuHolder(player.getUniqueId());
        }

        LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m instanceof LobbySelector).map(m -> (LobbySelector) m).findFirst().orElse(null);

        if (menu != null) menu.open(player);
    }

}
