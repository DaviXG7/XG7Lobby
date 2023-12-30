package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class DamageEvent implements Listener {
    private static FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();

            if (PluginUtil.isInWorld(player)) {
                if (event.getDamager() instanceof Player) {
                    event.setCancelled(!event.getDamager().hasPermission(PermissionType.ATACAR.getPerm()));
                    return;
                }
                event.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PluginUtil.isInWorld(player)) {

                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

                event.setCancelled(!config.getBoolean("take-damage"));
            }
        }
    }

}
