package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Build;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class OnBuild implements Listener {

    boolean defaultCondition(boolean def, PermissionType permissionType, String message, Player player) {

        if (PluginUtil.isInWorld(player)) {

            if (player.hasPermission(PermissionType.BUILD.getPerm())) {

                if (Build.canBuild(player)) return false;
                else {

                    TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.build-warn"), player);
                    return true;
                }

            } else if (!def) {

                if (PluginUtil.hasPermission(player, permissionType, message)) {
                    TextUtil.send(prefix + "&cYou need to have permission xg7lobby.build to build or interact!", player);
                }
                return true;
            }
        }

        return false;

    }


    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks"), PermissionType.BLOCOS_QUEBRAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-break"), player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks"), PermissionType.BLOCOS_COLOCAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-place"), player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (clickedBlock != null) {
                Material blockType = clickedBlock.getType();
                World world = clickedBlock.getWorld();

                if (PluginUtil.isInWorld(world)) {

                    if (configManager.getConfig(ConfigType.CONFIG).getStringList("blocks-with-canceled-interaction").contains(blockType.name())) {
                        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks"), PermissionType.BLOCOS_INTERAGIR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-interact"), player));
                    }
                }
            }
        }
    }





}
