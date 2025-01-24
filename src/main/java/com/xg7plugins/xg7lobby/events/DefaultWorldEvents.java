package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class DefaultWorldEvents implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "spawn-mobs", "false"}
    )
    public void onMobSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "leaves-decay", "false"}
    )
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "burn-blocks", "false"}
    )
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "block-spread", "false"}
    )
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            isOnlyInWorld = true,
            enabledPath = {"config", "cancel-explosions", "true"}
    )
    public void onExplosion(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

}
