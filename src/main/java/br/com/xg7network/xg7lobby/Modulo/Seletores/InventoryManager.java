package br.com.xg7network.xg7lobby.Modulo.Seletores;

import br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers;
import br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.ServerInformations;
import br.com.xg7network.xg7lobby.XG7Lobby;
import br.com.xg7network.xg7lobby.Modulo.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers.*;
import static br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.ServerInformations.SIitem;
import static br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.ServerInformations.SIslot;
import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class InventoryManager extends Module implements Listener {
    public InventoryManager(XG7Lobby plugin) {
        super(plugin);
    }
    public static int itens;

    @Override
    public void onEnable() {
        if (seletor.getSelector().getBoolean("GanharItens.QuandoNaoEstaNoInventario")) {
            itens = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    addMeta(1);
                    addMeta(2);
                    if (!p.getInventory().contains(HPitemAtivado) && !p.getInventory().contains(HPitemDesativado)) {
                        if (p.getInventory().contains(HPitemAtivado) && !p.getInventory().contains(HPitemDesativado)) {
                            if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("PADMREIESSCSIENJ")) {
                                vanished.remove(p.getUniqueId());
                                p.getInventory().setItem(HPslot, HPitemAtivado);
                            } else {
                                vanished.remove(p.getUniqueId());
                                p.getInventory().addItem(HPitemAtivado);
                            }
                        }
                    }
                    if (!p.getInventory().contains(SIitem)) {
                        if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("PADMREIESSCSIENJ")) {
                            p.getInventory().setItem(SIslot, SIitem);
                        } else {
                            p.getInventory().addItem(SIitem);
                        }
                    }
                }

            }, 0, seletor.getSelector().getInt("GanharItens.Cooldown")).getTaskId();


        }
    }

    @Override
    public void onDisable() {

        Bukkit.getScheduler().cancelTask(itens);

    }
    @EventHandler
    public void giveItem(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (getPlugin().getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                addMeta(1);

                if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar")) {
                    p.getInventory().setItem(HPslot, HPitemAtivado);
                    p.getInventory().setItem(SIslot, SIitem);
                } else if (p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar") && seletor.getSelector().getBoolean("PADMREIESSCSIENJ")) {
                    p.getInventory().addItem(HPitemAtivado);
                    p.getInventory().addItem(SIitem);
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

    public static void addMeta(int selector) {
        /*
        seletor 1 = HidePlayers
        seletor 2 = ServerInformations
         */

        //Hide Players
        if (selector == 1) {
            HidePlayers.HPmetaAtivado.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_ativado").replace("&", "§"));
            for (int i = 0; i < HidePlayers.HPlore.size(); i++) {
                HidePlayers.HPlore.set(i, HidePlayers.HPlore.get(i).replaceAll("&", "§"));
            }
            HidePlayers.HPmetaAtivado.setLore(HidePlayers.HPlore);
            HPitemAtivado.setItemMeta(HidePlayers.HPmetaAtivado);


            HidePlayers.HPmetaDesativado.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_desativado").replace("&", "§"));
            for (int i = 0; i < HidePlayers.HPlore.size(); i++) {
                HidePlayers.HPlore.set(i, HidePlayers.HPlore.get(i).replaceAll("&", "§"));
            }
            HidePlayers.HPmetaDesativado.setLore(HidePlayers.HPlore);
            HPitemDesativado.setItemMeta(HidePlayers.HPmetaDesativado);
        }
        if (selector == 2) {


            ServerInformations.SImeta.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_ativado").replace("&", "§"));
            for (int i = 0; i < ServerInformations.SIlore.size(); i++) {
                ServerInformations.SIlore.set(i, ServerInformations.SIlore.get(i).replaceAll("&", "§"));
            }
            ServerInformations.SImeta.setLore(HidePlayers.HPlore);
            SIitem.setItemMeta(ServerInformations.SImeta);
        }
    }

}
