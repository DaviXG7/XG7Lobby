package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DefaultPlayerEvents implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.break-block", "true"}
    )
    public void onBreakBlock(BlockBreakEvent event) {
        event.setCancelled(true);
        Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "player-prohibitions.break-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.place-block", "true"}
    )
    public void onPlaceBlock(BlockPlaceEvent event) {
        event.setCancelled(true);
        Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "player-prohibitions.place-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.interact-with-blocks", "true"}
    )
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(event.getAction().equals(Action.RIGHT_CLICK_BLOCK));
        Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "player-prohibitions.interact-with-blocks").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.drop-item", "true"}
    )
    public void onDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
        Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "player-prohibitions.drop-items").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.pickup-item", "true"}
    )
    public void onPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
        Text.formatLang(XG7Lobby.getInstance(), event.getPlayer(), "player-prohibitions.pickup-items").thenAccept(text -> text.send(event.getPlayer()));
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.take-damage", "true"}
    )
    public void onTakeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "player-defaults.attack", "true"}
    )
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        event.setCancelled(true);
        Text.formatLang(XG7Lobby.getInstance(), event.getDamager(), "player-prohibitions.attack").thenAccept(text -> text.send(event.getDamager()));
    }

}
