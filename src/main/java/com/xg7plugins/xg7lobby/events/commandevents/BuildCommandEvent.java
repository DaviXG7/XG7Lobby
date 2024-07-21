package com.xg7plugins.xg7lobby.events.commandevents;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.stream.Collectors;

public class BuildCommandEvent implements Event {
    @Override
    public boolean isEnabled() {
        return !Config.getBoolean(ConfigType.CONFIG, "break-blocks") || !Config.getBoolean(ConfigType.CONFIG, "place-blocks") || !Config.getBoolean(ConfigType.CONFIG, "interact-with-blocks") || !Config.getBoolean(ConfigType.CONFIG, "pickup-items") || !Config.getBoolean(ConfigType.CONFIG, "drop-items");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (Config.getBoolean(ConfigType.CONFIG, "place-blocks")) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isBuildEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !event.getPlayer().hasPermission(PermissionType.BLOCK_BUILD.getPerm()) ? "block-item-permission.no-permission-place" : "build.disabled"), event.getPlayer());
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (Config.getBoolean(ConfigType.CONFIG, "break-blocks")) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isBuildEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !event.getPlayer().hasPermission(PermissionType.BLOCK_BREAK.getPerm()) ? "block-item-permission.no-permission-break" : "build.disabled"), event.getPlayer());
        }
    }
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (Config.getBoolean(ConfigType.CONFIG, "pickup-items")) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isBuildEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !event.getPlayer().hasPermission(PermissionType.ITEM_PICKUP.getPerm()) ? "block-item-permission.no-permission-pickup" : "build.disabled"), event.getPlayer());
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (Config.getBoolean(ConfigType.CONFIG, "drop-items")) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isBuildEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !event.getPlayer().hasPermission(PermissionType.ITEM_DROP.getPerm()) ? "block-item-permission.no-permission-drop" : "build.disabled"), event.getPlayer());
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (Config.getBoolean(ConfigType.CONFIG, "interact-with-blocks")) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!Config.getList(ConfigType.CONFIG, "blocks-with-canceled-interaction").stream().map(block -> XMaterial.valueOf(block.toUpperCase()).parseMaterial().name()).collect(Collectors.toList()).contains(event.getClickedBlock().getType().name())) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isBuildEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !event.getPlayer().hasPermission(PermissionType.BLOCK_INTERACT.getPerm()) ? "block-item-permission.no-permission-interact" : "build.disabled"), event.getPlayer());
        }
    }
}
