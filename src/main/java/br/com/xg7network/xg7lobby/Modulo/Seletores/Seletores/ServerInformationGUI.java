package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class ServerInformationGUI {

    private XG7Lobby pl;

    int slotIgnorado;

    public ServerInformationGUI(XG7Lobby pl) {
        this.pl = pl;
    }

    private Inventory inventory;

    public Inventory siCreateInventory() {

        ConfigurationSection itensSection = seletor.getSelector().getConfigurationSection("InfServidor.GUI.InfGUI.Itens");

        if (itensSection != null && !(itensSection.getKeys(false).isEmpty())) {
            for (String subSection : itensSection.getKeys(false)) {
                if (!subSection.equals("Padrão")) {
                    ItemStack items = new ItemStack(Material.valueOf(seletor.getSelector().getString("InfServidor.GUI.InfGUI.Itens." + subSection + ".item")));
                    ItemMeta metas = items.getItemMeta();
                    List<String> lores = seletor.getSelector().getStringList("InfServidor.GUI.InfGUI.Itens." + subSection + ".lore");
                    for (int i = 0; i < lores.size(); i++) {
                        lores.set(i, lores.get(i).replaceAll("&", "§"));
                    }
                    metas.setDisplayName(seletor.getSelector().getString("InfServidor.GUI.InfGUI.Itens." + subSection + ".nome").replace("&", "§"));
                    metas.setLore(lores);
                    items.setItemMeta(metas);

                    slotIgnorado = seletor.getSelector().getInt("InfServidor.GUI.InfGUI.Itens." + subSection + ".slot");

                    inventory.setItem(seletor.getSelector().getInt("InfServidor.GUI.InfGUI.Itens." + subSection + ".slot"), items);
                } else {
                    ItemStack item = new ItemStack(Material.valueOf(seletor.getSelector().getString("InfServidor.GUI.InfGUI.Itens." + subSection + ".item")));
                    for (int i = 0; i < inventory.getSize(); i++) {
                        if (i != slotIgnorado) {
                            inventory.setItem(i, item);
                        }
                    }
                }
            }
        }
        return inventory;
    }
}
