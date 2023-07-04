package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class SPListener implements Listener {
    private XG7Lobby pl;
    private SPManager SPM;
    private Cache<UUID, Long> cooldown;

    public SPListener(XG7Lobby pl) {
        this.cooldown = CacheBuilder.newBuilder().expireAfterWrite((long) seletor.getSelector().getInt("Cooldown"), TimeUnit.SECONDS).build();
        this.pl = pl;
        this.SPM = new SPManager(pl);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            long segundos = 0L;
            if (!cooldown.asMap().containsKey(p.getUniqueId()) && cooldown.asMap().get(p.getUniqueId()) > System.currentTimeMillis()) {
                segundos = seletor.getSelector().getInt("EsconderJogadores.Cooldown");
                this.cooldown.put(p.getUniqueId(), System.currentTimeMillis() + segundos * 1000L);
                if (this.SPM.getSeletorItens().contains(e.getItem())) {
                    if (this.SPM.inventory(p)) {
                        this.SPM.open(p);
                    } else {
                        this.executar(p);
                    }
                }
            } else {
                long distancia = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
                action.mandarAction(p, seletor.getSelector().getString("Mensagem_EmCooldown").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))));
            }
        }

    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (this.SPM.getSelectorInventories(p).contains(e.getInventory()) || this.SPM.getInventories(p).contains(e.getInventory())) {
            this.SPM.executeActionInventário(p, e.getCurrentItem(), e.getInventory());
            e.setCancelled(true);
        }

    }

    void executar(Player player) {
        this.SPM.executeActionSeletor(player);
    }
}
