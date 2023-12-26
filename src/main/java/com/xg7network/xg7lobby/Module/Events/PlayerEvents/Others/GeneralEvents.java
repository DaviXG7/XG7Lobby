package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class GeneralEvents implements Listener {


    @EventHandler
    public void onPortal(PlayerTeleportEvent event) {

        if (PluginUtil.isInWorld(event.getPlayer()) && configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal")) {

            event.setCancelled(event.getTo().getWorld().getEnvironment().equals(World.Environment.NETHER) || event.getTo().getWorld().getEnvironment().equals(World.Environment.THE_END));

        }

    }


}
