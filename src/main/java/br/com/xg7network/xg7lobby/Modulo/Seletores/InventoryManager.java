package br.com.xg7network.xg7lobby.Modulo.Seletores;

import br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers;
import br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.SPManager;
import br.com.xg7network.xg7lobby.XG7Lobby;
import br.com.xg7network.xg7lobby.Modulo.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers.*;
import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class InventoryManager extends Module implements Listener {
    public static int itens;

    private SPManager SPM;
    public static List<ItemStack> items = new ArrayList();

    public InventoryManager(XG7Lobby plugin) {
        super(plugin);
        this.SPM = new SPManager(plugin);
    }

    public void onEnable() {
        itens = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                addMeta();
                if (this.getPlugin().getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                    for (ItemStack item : items) {
                        if (!p.getInventory().contains(item)) {
                            giveItem(p);
                            SPM.giveItens(p);
                        }
                    }
                } else {

                    for (ItemStack item : items) {
                        p.getInventory().removeItem(item);
                    }
                }
            }
        }, 0L, 5L).getTaskId();
    }


    @Override
    public void onDisable() {

        Bukkit.getScheduler().cancelTask(itens);

    }
    private void giveItem(Player p) {
        if (getPlugin().getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                if (!p.getInventory().contains(HPitemAtivado) || !p.getInventory().contains(HPitemDesativado)) {
                    addMeta();

                    if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar")) {
                        p.getInventory().setItem(HPslot, HPitemAtivado);
                    } else if (p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar") && seletor.getSelector().getBoolean("PADMREIESSCSIENJ")) {
                        p.getInventory().addItem(HPitemAtivado);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (getPlugin().getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                if (!p.hasPermission("xg7lobby.admin")) {
                    p.getInventory().clear(HPslot);
                } else {
                    if (p.getInventory().contains(HPitemAtivado)) {
                        p.getInventory().removeItem(HPitemAtivado);
                    } else if (p.getInventory().contains(HPitemDesativado)) {
                        p.getInventory().removeItem(HPitemDesativado);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if (this.getPlugin().getConfig().getStringList("mundos_ativados").contains(e.getWhoClicked().getWorld().getName())) {
            if (!seletor.getSelector().getBoolean("MexerNoInventário")) {
                e.setCancelled(!e.getWhoClicked().hasPermission("xg7lobby.admin"));
            }
        }

    }

    public void addMeta() {
        HidePlayers.HPmetaAtivado.setDisplayName(XG7Lobby.seletor.getSelector().getString("EsconderJogadores.nome_ativado").replace("&", "§"));

        int i;
        for(i = 0; i < HidePlayers.HPlore.size(); ++i) {
            HidePlayers.HPlore.set(i, ((String)HidePlayers.HPlore.get(i)).replaceAll("&", "§"));
        }

        HidePlayers.HPmetaAtivado.setLore(HidePlayers.HPlore);
        HidePlayers.HPitemAtivado.setItemMeta(HidePlayers.HPmetaAtivado);
        HidePlayers.HPmetaDesativado.setDisplayName(XG7Lobby.seletor.getSelector().getString("EsconderJogadores.nome_desativado").replace("&", "§"));

        for(i = 0; i < HidePlayers.HPlore.size(); ++i) {
            HidePlayers.HPlore.set(i, ((String)HidePlayers.HPlore.get(i)).replaceAll("&", "§"));
        }

        HidePlayers.HPmetaDesativado.setLore(HidePlayers.HPlore);
        HidePlayers.HPitemDesativado.setItemMeta(HidePlayers.HPmetaDesativado);
        items.add(HidePlayers.HPitemDesativado);
        items.add(HidePlayers.HPitemAtivado);
    }

}
