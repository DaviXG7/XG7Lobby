package com.xg7plugins.xg7lobby.pvp;

import com.xg7plugins.XG7Plugins;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;

public class PVPListener implements LobbyEvent {

    private final GlobalPVPManager pvpManager;

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
            Text.fromLang(damager, XG7Lobby.getInstance(), "pvp.on-attack").join().send(damager);
            return;
        }

        event.setCancelled(false);

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
                killer.setGlobalPVPKills(killer.getGlobalPVPKills() + 1);
                killer.update();
            }

            XG7Lobby.getInstance().getActionsProcessor().process("on-pvp-death", event.getEntity());

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (killer != null) {
                    Text.fromLang(player,XG7Lobby.getInstance(), "pvp.on-death-with-killer").join()
                            .replace("player", event.getEntity().getName())
                            .replace("[KILLER]", event.getEntity().getKiller().getName())
                            .replace("[CAUSE]", event.getEntity().getLastDamageCause().getCause().name().toLowerCase())
                            .send(player);
                    return;
                }

                Text.fromLang(player,XG7Lobby.getInstance(), "pvp.on-death-without-killer").join()
                        .replace("player", event.getEntity().getName())
                        .replace("[CAUSE]", event.getEntity().getLastDamageCause().getCause().name().toLowerCase())
                        .send(player);
            });

        });

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

    @Override
    public void onWorldLeave(Player player, World newWorld) {
        pvpManager.removePlayerFromPVP(player);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
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
