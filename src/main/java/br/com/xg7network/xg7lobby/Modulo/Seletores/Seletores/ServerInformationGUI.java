package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores;

import br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers;
import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.ServerInformations.inv;

public class ServerInformationGUI {

    private XG7Lobby pl;

    public ServerInformationGUI(XG7Lobby pl) {
        this.pl = pl;
    }

    public void siGui() {

        ConfigurationSection itensSection = pl.getConfig().getConfigurationSection("InfServidor.GUI.Itens");

        if (itensSection != null && !itensSection.getKeys(false).isEmpty()) {
            for (String subSection : itensSection.getKeys(false)) {
                ItemStack items = new ItemStack(Material.valueOf(pl.getConfig().getString("InfServidor.GUI.InfGUI.Itens." + subSection + ".item")));
                ItemMeta metas = items.getItemMeta();
                List<String> lores = pl.getConfig().getStringList("InfServidor.GUI.InfGUI.Itens." + subSection + ".lore");
                for (int i = 0; i < lores.size(); i++) {
                    lores.set(i, lores.get(i).replaceAll("&", "§"));
                }
                metas.setDisplayName(pl.getConfig().getString("InfServidor.GUI.InfGUI.Itens." + subSection + ".nome").replace("&", "§"));
                metas.setLore(lores);
                items.setItemMeta(metas);

                inv.setItem(pl.getConfig().getInt("InfServidor.GUI.InfGUI.Itens." + subSection + ".slot"), items);
            }
        }


    }
}
