package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;
import static br.com.xg7network.xg7lobby.Modulo.Seletores.InventoryManager.addMeta;

public class ServerInformations implements Listener {

    private XG7Lobby pl;

    public ServerInformations(XG7Lobby pl) {
        this.pl = pl;
    }
    public static Inventory inv;

    int primeiraVez = 0;

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
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    if (e.getItem() != null && SIitem != null && e.getItem().equals(SIitem)) {
                        if (p.getItemInHand().equals(SIitem)) {
                            if (seletor.getSelector().getBoolean("InfServidor.GUI.ativado")) {
                                inv = Bukkit.createInventory(p, seletor.getSelector().getInt("InfServidor.GUI.InfGUI.tamanho"));
                                this.siGUI = new ServerInformationGUI(pl);
                                inv = siGUI.siGui(inv);
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

}
