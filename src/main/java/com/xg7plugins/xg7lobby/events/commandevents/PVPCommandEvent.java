package com.xg7plugins.xg7lobby.events.commandevents;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PVPCommandEvent implements Event {
    @Override
    public boolean isEnabled() {
        return true;
    }


    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!EventManager.getWorlds().contains(event.getEntity().getWorld().getName())) return;
        if(!(event.getDamager() instanceof Player)) {
            event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "take-damage") && event.getEntity() instanceof Player);
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(!Config.getBoolean(ConfigType.CONFIG, "attack-entities"));
            return;
        }
        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();


        if (!Config.getBoolean(ConfigType.CONFIG, "pvp.enabled")) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, "pvp.not-enabled"), damager);
            return;
        }

        PlayerData damagerData = PlayerManager.getPlayerData(damager.getUniqueId());
        PlayerData victimData = PlayerManager.getPlayerData(victim.getUniqueId());

        if (!damagerData.isPVPEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, !damager.hasPermission(PermissionType.PVP.getPerm()) ? "pvp.no-permission" : "pvp.disabled"), damager);
            return;
        }

        if (!victimData.isPVPEnabled()) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, "pvp.disabled-other"), damager);
        }

    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!EventManager.getWorlds().contains(event.getEntity().getWorld().getName())) return;

        PlayerData damagerData = PlayerManager.getPlayerData(event.getEntity().getKiller().getUniqueId());
        PlayerData victimData = PlayerManager.getPlayerData(event.getEntity().getUniqueId());

        if (victimData.isPVPEnabled() && damagerData.isPVPEnabled()) {
            Config.getList(ConfigType.CONFIG, "pvp.events-on-kill").forEach(action -> Action.execute(action.replace("[KILLER]", event.getEntity().getKiller().getName()), event.getEntity()));
        }

        if (!Config.getBoolean(ConfigType.CONFIG, "pvp.drop-items")) event.getDrops().clear();


        if (Config.getBoolean(ConfigType.CONFIG, "pvp.auto-respawn")) event.getEntity().spigot().respawn();
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;


            if (PlayerManager.getPlayerData(event.getPlayer().getUniqueId()).isPVPEnabled()) {
                Config.getList(ConfigType.CONFIG, "pvp.events-on-respawn").forEach(action -> Action.execute(action.replace("[PLAYER]", event.getPlayer().getName()), event.getPlayer()));
            }
        },5L);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (CacheManager.getPvpCache().asMap().containsKey(event.getPlayer().getUniqueId())) {
            if (
                    event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                            event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                            event.getFrom().getBlockZ() != event.getTo().getBlockZ()
            )
            {
                Player player = event.getPlayer();
                PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());
                Text.send(Config.getString(ConfigType.MESSAGES, data.isPVPEnabled() ? "pvp.pvp-disable-cancelled" : "pvp.pvp-enable-cancelled"), player);
                TaskManager.cancelTask("cooldown:pvp=" + player.getUniqueId());
                CacheManager.remove(player.getUniqueId(), CacheType.PVP_COOLDOWN);
            }
        }
    }
}
