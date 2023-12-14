package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Build;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class OnBuild implements Listener {

    boolean defaultCondition(boolean def, PermissionType permissionType, String message, Player player) {

        if (PluginUtil.isInWorld(player))
            if (!def)
                if (!PluginUtil.hasPermission(player, permissionType, message)) return true;
                else {

                    if (Build.canBuild(player)) return false;
                    else {
                        new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("commands.build-warn")).send(player);
                        return true;
                    }

                }

        return false;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks"), PermissionType.BLOCOS_QUEBRAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-break"), player));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks"), PermissionType.BLOCOS_QUEBRAR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-place"), player));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (clickedBlock != null) {
                Material blockType = clickedBlock.getType();
                World world = clickedBlock.getWorld();

                if (PluginUtil.isInWorld(world)) {

                    boolean crafiting_table = Arrays.stream(Material.values())
                            .map(Material::name)
                            .collect(Collectors.toList())
                            .contains("CRAFITING_TABLE");

                    if (blockType == Material.ANVIL || blockType == Material.HOPPER
                            || blockType == Material.DISPENSER || blockType == Material.DROPPER || blockType == Material.CHEST
                            || blockType == Material.FURNACE || blockType == Material.matchMaterial(crafiting_table ? "CRAFITING_TABLE" : "WORKBENCH")) {

                        event.setCancelled(defaultCondition(configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks"), PermissionType.BLOCOS_INTERAGIR, configManager.getConfig(ConfigType.MESSAGES).getString("events.permission-interact"), player));
                    }
                }
            }
        }
    }





}
