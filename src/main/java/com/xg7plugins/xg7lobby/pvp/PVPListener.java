package com.xg7plugins.xg7lobby.pvp;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.cache.ObjectCache;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.events.LobbyEvent;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PVPListener implements Listener {

    private final GlobalPVPManager pvpManager;

    private final ObjectCache<UUID, UUID> combatLog = new ObjectCache<>(
            XG7Lobby.getInstance(),
            Config.mainConfigOf(XG7Lobby.getInstance()).getTime("global-pvp.combat-log-remove").orElse(1000L * 30),
            true,
            "combat-log",
            true,
            UUID.class,
            UUID.class
    );

    public PVPListener(GlobalPVPManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("global-pvp.enabled", Boolean.class).orElse(false);
    }


    @EventHandler(priority = EventPriority.HIGHEST, isOnlyInWorld = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (pvpManager.isPlayerInPVP(((Player) event.getEntity()))) event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, isOnlyInWorld = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!pvpManager.isPlayerInPVP(damager)) return;

        if (!pvpManager.isPlayerInPVP(victim)) {
            event.setCancelled(true);
            Text.fromLang(damager, XG7Lobby.getInstance(), "pvp.on-attack").join().send(damager);
            return;
        }

        event.setCancelled(false);

        if (victim.getUniqueId().equals(damager.getUniqueId())) return;

        combatLog.remove(victim.getUniqueId());
        combatLog.put(victim.getUniqueId(), damager.getUniqueId());

    }

    @EventHandler(isOnlyInWorld = true)
    public void onDeath(PlayerDeathEvent event) {
        if (!pvpManager.isPlayerInPVP(event.getEntity())) return;

        event.setDeathMessage(null);

        XG7Plugins.taskManager().runAsyncTask(XG7Lobby.getInstance(), "database", () -> {
            LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getEntity().getUniqueId(), false).join();

            lobbyPlayer.setGlobalPVPDeaths(lobbyPlayer.getGlobalPVPDeaths() + 1);
            lobbyPlayer.update();

            LobbyPlayer killer = event.getEntity().getKiller() != null ? LobbyPlayer.cast(event.getEntity().getKiller().getUniqueId(), false).join() : null;

            if (killer != null) {
                if (killer.getPlayerUUID().equals(event.getEntity().getUniqueId())) {
                    killer = null;
                } else {
                    killer.setGlobalPVPKills(killer.getGlobalPVPKills() + 1);
                    killer.update();
                }
            }
            if (killer == null) {
                UUID killerUUID = combatLog.get(event.getEntity().getUniqueId()).join();
                if (killerUUID != null && !killerUUID.equals(event.getEntity().getUniqueId())) {
                    killer = LobbyPlayer.cast(killerUUID, false).join();
                    killer.setGlobalPVPKills(killer.getGlobalPVPKills() + 1);
                    killer.update();
                }
            }

            XG7Lobby.getInstance().getActionsProcessor().process("on-pvp-death", event.getEntity());

            LobbyPlayer finalKiller = killer;
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!pvpManager.isPlayerInPVP(player) && !Config.mainConfigOf(XG7Lobby.getInstance()).get("global-pvp.send-kill-message-only-in-pvp",Boolean.class).orElse(false)) return;
                if (finalKiller != null) {
                    Text.fromLang(player,XG7Lobby.getInstance(), "pvp.on-death-with-killer").join()
                            .replace("victim", event.getEntity().getName())
                            .replace("killer", finalKiller.getOfflinePlayer().getName())
                            .replace("cause", event.getEntity().getLastDamageCause().getCause().name().toLowerCase())
                            .send(player);
                    return;
                }

                Text.fromLang(player,XG7Lobby.getInstance(), "pvp.on-death-without-killer").join()
                        .replace("victim", event.getEntity().getName())
                        .replace("cause", event.getEntity().getLastDamageCause().getCause().name().toLowerCase())
                        .send(player);
            });

            combatLog.remove(event.getEntity().getUniqueId());

        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID killerUUID = combatLog.get(event.getPlayer().getUniqueId()).join();
        if (killerUUID != null && !killerUUID.equals(event.getPlayer().getUniqueId())) {
            LobbyPlayer player = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();
            player.setGlobalPVPDeaths(player.getGlobalPVPDeaths() + 1);
            player.update();
            LobbyPlayer killer = LobbyPlayer.cast(killerUUID, false).join();
            killer.setGlobalPVPKills(killer.getGlobalPVPKills() + 1);
            killer.update();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (!pvpManager.isPlayerInPVP(p) && !Config.mainConfigOf(XG7Lobby.getInstance()).get("global-pvp.send-kill-message-only-in-pvp", Boolean.class).orElse(false))
                    return;

                Text.fromLang(p, XG7Lobby.getInstance(), "pvp.on-death-with-killer").join()
                        .replace("victim", event.getPlayer().getName())
                        .replace("killer", killer.getOfflinePlayer().getName())
                        .replace("cause", event.getPlayer().getLastDamageCause().getCause().name().toLowerCase())
                        .send(p);
            });
        }
        combatLog.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!pvpManager.isPlayerInPVP(event.getPlayer())) return;
        Player player = event.getPlayer();

        if (!XG7Lobby.getInstance().isInWorldEnabled(player)) {
            pvpManager.removePlayerFromPVP(player);
            return;
        }

        player.getInventory().clear();

        pvpManager.addPlayerToPVP(player);

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        if (!pvpManager.isPlayerInPVP(event.getPlayer())) return;

        if (event.getPlayer().hasPermission("xg7lobby.command.*")) return;

        if (XG7Lobby.getInstance().getConfig("config").getList("global-pvp.commands-blocked", String.class).orElse(new ArrayList<>()).contains(event.getMessage().split(" ")[0])) {
            event.setCancelled(true);
            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "pvp.pvp-command").thenAccept(text -> text.send(event.getPlayer()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, isOnlyInWorld = true)
    public void onInteract(PlayerInteractEvent event) {

        if (!pvpManager.isPlayerInPVP(event.getPlayer())) return;

        event.setCancelled(false);

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("pvp-disable", event.getPlayer())) {
            if (
                    event.getFrom().getBlockX() != event.getTo().getBlockX()
                            || event.getFrom().getBlockY() != event.getTo().getBlockY()
                            || event.getFrom().getBlockZ() != event.getTo().getBlockZ()
            ) {
                XG7Plugins.getInstance().getCooldownManager().removePlayer("pvp-disable", event.getPlayer().getUniqueId());
            }
        }

    }

}
