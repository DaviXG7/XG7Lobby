package com.xg7plugins.xg7lobby.events.worldevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.EventManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WorldEvents implements Event {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "spawn-mobs") && EventManager.getWorlds().contains(event.getLocation().getWorld().getName()));
    }
    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "cancel-explosions") && EventManager.getWorlds().contains(event.getBlock().getWorld().getName()));
    }
    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "cancel-explosions") && EventManager.getWorlds().contains(event.getLocation().getWorld().getName()));
    }
    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "block-spread") && EventManager.getWorlds().contains(event.getBlock().getWorld().getName()));
    }
    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "burn-blocks") && EventManager.getWorlds().contains(event.getBlock().getWorld().getName()));
    }
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "leaves-decay") && EventManager.getWorlds().contains(event.getBlock().getWorld().getName()));
    }
}
