package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerEvents implements Event {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onPortal(PlayerTeleportEvent event) {

        if (!Config.getBoolean(ConfigType.CONFIG, "cancel-portal")) return;
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        if (!EventManager.getWorlds().contains(event.getFrom().getWorld().getName())) return;
        event.setCancelled(event.getTo().getWorld().getEnvironment().equals(World.Environment.NETHER) || event.getTo().getWorld().getEnvironment().equals(World.Environment.THE_END));

    }

    @EventHandler
    public void onCancelVoid(PlayerMoveEvent event) {
        if (!Config.getBoolean(ConfigType.CONFIG, "cancel-death-by-void")) return;
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        int level = Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1]) >= 18 ? -79 : -6;


        Player player = event.getPlayer();

        if (!EventManager.getWorlds().contains(player.getWorld().getName())) return;
        if (event.getTo().getBlockY() <= level) {

            World world = Bukkit.getWorld(Config.getString(ConfigType.DATA, "spawn-location.world"));
            double x = Config.getDouble(ConfigType.DATA, "spawn-location.x");
            double y = Config.getDouble(ConfigType.DATA, "spawn-location.y");
            double z = Config.getDouble(ConfigType.DATA, "spawn-location.z");
            float yaw = (float) Config.getDouble(ConfigType.DATA, "spawn-location.yaw");
            float pitch = (float) Config.getDouble(ConfigType.DATA, "spawn-location.pitch");

            player.teleport(new Location(world, x, y, z, yaw, pitch));

        }


    }
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (Config.getBoolean(ConfigType.CONFIG, "take-damage")) return;
        if (event.getEntity() instanceof Player) {
            if (PlayerManager.getPlayerData(event.getEntity().getUniqueId()).isPVPEnabled()) {
                event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "pvp.take-damage") && !event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK));
                return;
            }

            event.setCancelled(EventManager.getWorlds().contains(event.getEntity().getWorld().getName()));

        }
    }

}
