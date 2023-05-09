package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("InfServidor.ativado")) {
                addMeta(2);
                primeiraVez++;

                if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar")) {
                    p.getInventory().setItem(SIslot, SIitem);
                } else if (p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar") && seletor.getSelector().getBoolean("PADMREIESSCSI")) {
                    p.getInventory().addItem(SIitem);
                }
            }
        }

    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("InfServidor.ativado")) {
                if (e.getItem() != null && SIitem != null && e.getItem().equals(SIitem)) {
                    if (seletor.getSelector().getBoolean("InfServidor.GUI.ativado")) {
                        inv = Bukkit.createInventory(e.getPlayer(), (seletor.getSelector().getInt("InfServidor.GUI.InfGUI.tamanho") - 1));
                        this.siGUI = new ServerInformationGUI(pl);
                        siGUI.siGui();
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

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                if (!p.hasPermission("xg7lobby.admin")) {
                    p.getInventory().clear(SIslot);
                } else {
                    p.getInventory().removeItem(SIitem);
                }
            }
        }
    }
}
