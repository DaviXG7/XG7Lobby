package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SPListener implements Listener {

    private XG7Lobby pl;
    private SPManager SPM;

    public SPListener(XG7Lobby pl) {
        this.pl = pl;
        this.SPM = new SPManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        SPM.giveItens(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (SPM.getSeletorItens().contains(e.getItem())) {
                if (SPM.inventory(p)) {
                    SPM.open(p);
                } else {
                    SPM.executeActionSeletor(p);
                }
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (SPM.getSelectorInventories(p).contains(e.getInventory()) || SPM.getInventories(p).contains(e.getInventory())) {
            SPM.executeActionInventário(p, e.getCurrentItem(), e.getInventory());
            e.setCancelled(true);
        }
    }

}
