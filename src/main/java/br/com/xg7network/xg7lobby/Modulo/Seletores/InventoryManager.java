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
                    addMeta();
                    if (p.getInventory().contains(HidePlayers.HPitemAtivado) && !p.getInventory().contains(HidePlayers.HPitemDesativado)) {
                        if (!p.hasPermission("xg7lobby.admin")) {
                            p.getInventory().setItem(HidePlayers.HPslot, HidePlayers.HPitemAtivado);
                        } else {
                            p.getInventory().addItem(HidePlayers.HPitemAtivado);
                        }
                    } else if (p.getInventory().contains(HidePlayers.HPitemDesativado) && !p.getInventory().contains(HidePlayers.HPitemAtivado)) {
                        if (!p.hasPermission("xg7lobby.admin")) {
                            p.getInventory().setItem(HidePlayers.HPslot, HidePlayers.HPitemDesativado);
                        } else {
                            p.getInventory().addItem(HidePlayers.HPitemDesativado);
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
    public void onInventory(InventoryClickEvent e) {
        if (this.getPlugin().getConfig().getStringList("mundos_ativados").contains(e.getWhoClicked().getWorld().getName())) {
            if (!seletor.getSelector().getBoolean("MexerNoInventário")) {
                e.setCancelled(!e.getWhoClicked().hasPermission("xg7lobby.admin"));
            }
        }

    }

    public static void addMeta() {

        //Hide Players
        HidePlayers.HPmetaAtivado.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_ativado").replace("&", "§"));
        for (int i = 0; i < HidePlayers.HPlore.size(); i++) {
            HidePlayers.HPlore.set(i, HidePlayers.HPlore.get(i).replaceAll("&", "§"));
        }
        HidePlayers.HPmetaAtivado.setLore(HidePlayers.HPlore);
        HidePlayers.HPitemAtivado.setItemMeta(HidePlayers.HPmetaAtivado);


        HidePlayers.HPmetaDesativado.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_desativado").replace("&", "§"));
        for (int i = 0; i < HidePlayers.HPlore.size(); i++) {
            HidePlayers.HPlore.set(i, HidePlayers.HPlore.get(i).replaceAll("&", "§"));
        }
        HidePlayers.HPmetaDesativado.setLore(HidePlayers.HPlore);
        HidePlayers.HPitemDesativado.setItemMeta(HidePlayers.HPmetaDesativado);

        //Server Informations

        ServerInformations.SImeta.setDisplayName(seletor.getSelector().getString("EsconderJogadores.nome_ativado").replace("&", "§"));
        for (int i = 0; i < ServerInformations.SIlore.size(); i++) {
            ServerInformations.SIlore.set(i, ServerInformations.SIlore.get(i).replaceAll("&", "§"));
        }
        ServerInformations.SImeta.setLore(HidePlayers.HPlore);
        ServerInformations.SIitem.setItemMeta(ServerInformations.SImeta);
    }

}
