package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction;

import com.comphenix.protocol.PacketType;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class DropPickup implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (PluginUtil.isInWorld(player)) {

            event.setCancelled(!PluginUtil.hasPermission(player, PermissionType.ITENS_JOGAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-drop")));

        }


    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (PluginUtil.isInWorld(player)) {

            event.setCancelled(!PluginUtil.hasPermission(player, PermissionType.ITENS_PEGAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-pickup")));

        }


    }



}
