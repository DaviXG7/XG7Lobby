package com.xg7plugins.xg7lobby.events.defaults;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class DefaultWorldEvents implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "spawn-mobs", "true"}
    )
    public void onMobSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "leaves-decay", "true"}
    )
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "burn-blocks", "true"}
    )
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "block-spread", "true"}
    )
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "cancel-explosions", "false"}
    )
    public void onExplosion(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

}
