package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class ServerInformations implements Listener {

    private XG7Lobby pl;

    public ServerInformations(XG7Lobby pl) {
        this.pl = pl;
    }

    int primeiraVez = 0;

    Inventory inv;

    public static List<String> SIlore = seletor.getSelector().getStringList("InfServidor.lore");
    public static int SIslot = seletor.getSelector().getInt("Infservidor.slot");
    public static ItemStack SIitem = new ItemStack(Material.valueOf(seletor.getSelector().getString("InfServidor.item")));
    public static ItemMeta SImeta = SIitem.getItemMeta();
    private ServerInformationGUI siGUI;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("InfServidor.ativado")) {
                if (e.getItem() != null && SIitem != null && e.getItem().equals(SIitem)) {
                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        if (p.getItemInHand().equals(SIitem)) {
                            if (seletor.getSelector().getBoolean("InfServidor.GUI.ativado")) {
                                this.siGUI = new ServerInformationGUI(pl);
                                inv = Bukkit.createInventory(p, seletor.getSelector().getInt("InfServidor.GUI.InfGUI.tamanho"),  seletor.getSelector().getString("InfServidor.GUI.InfGUI.nome").replace("&", "§"));
                                inv = siGUI.siCreateInventory();
                                p.openInventory(inv);


                            } else {
                                for (String mensagens : seletor.getSelector().getStringList("InfServidor.GUI.mensagem")) {
                                    p.sendMessage(mensagens);
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onServerInformations(InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

}
