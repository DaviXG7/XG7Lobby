package br.com.xg7network.xg7lobby.Eventos.PlayerConfigs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent implements Listener {

    private XG7Lobby pl;

    public PlayerDamageEvent(XG7Lobby pl) {
        this.pl = pl;
    }


    @EventHandler
    public void onPlayerDrown(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorAfogamento")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFire(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorFogo")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerExplosion(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockAttack(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorBlocoCaindo")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                    if (!pl.getConfig().getBoolean("LevarDanoPorBlocos")) {
                        e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFly(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (!pl.getConfig().getBoolean("EvitarADMs")) {
                            if (e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
                                e.setCancelled(true);
                            }
                        } else {
                            if (e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
                                e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMagma(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLava(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDragon(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!pl.getConfig().getBoolean("LevarDanos")) {
                    if (!pl.getConfig().getBoolean("LevarDanoPorExplosão")) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)) {
                            e.setCancelled(!p.hasPermission("xg7lobby.admin"));
                        }
                    }
                }
            }
        }
    }
}
