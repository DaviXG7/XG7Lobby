package com.xg7network.xg7lobby.Module.Events.WorldEvents;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Blocks implements Listener {

    private static FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {
        event.setCancelled(PluginUtil.isInWorld(event.getBlock().getWorld()) && config.getBoolean("cancel-explosions"));
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.setCancelled(PluginUtil.isInWorld(event.getEntity().getWorld()) && config.getBoolean("cancel-explosions"));
    }
    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(PluginUtil.isInWorld(event.getBlock().getWorld()) && !config.getBoolean("burn-blocks"));
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        event.setCancelled(PluginUtil.isInWorld(event.getBlock().getWorld()) && !config.getBoolean("block-spread"));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(PluginUtil.isInWorld(event.getBlock().getWorld()) && !config.getBoolean("leaves-decay"));
    }



}
