package br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.SeletoresPersonalizados;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class SPManager {

    ConfigurationSection Seletores = seletor.getSelector().getConfigurationSection("Seletores.Seletores");
    ConfigurationSection Inventários = seletor.getSelector().getConfigurationSection("Seletores.Inventários");
    ConfigurationSection itensSeletores;
    ItemStack item;
    ItemStack itemPadrão;
    ItemStack itemGUI;
    ItemStack itemInventory;

    public List<ItemStack> getSeletorItens() {
        List<ItemStack> itens = new ArrayList<>();
        if (Seletores != null && !(Seletores.getKeys(false).isEmpty())) {
            for (String Seletor : Seletores.getKeys(false)) {
                item = new ItemStack(Material.valueOf(seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".item")));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".nome").replace("&", "§"));
                List<String> lores = seletor.getSelector().getStringList("Seletores.Seletores." + Seletor + ".lore");
                for (int i = 0; i < lores.size(); i++) {
                    lores.set(i, lores.get(i).replaceAll("&", "§"));
                }
                meta.setLore(lores);
                if (seletor.getSelector().getBoolean("Seletores.Seletores." + Seletor + ".glow")) {
                    meta.addEnchant(null, 0, false);
                }
                item.setItemMeta(meta);

                itens.add(item);

            }
        }
        return itens;
    }

    public List<Inventory> getSelectorInventories(Player player) {
        Inventory inv = null;
        ItemMeta meta;
        List<Inventory> inventories = new ArrayList<>();
        if (Seletores != null && !(Seletores.getKeys(false).isEmpty())) {
            for (String Seletor : Seletores.getKeys(false)) {
                if (seletor.getSelector().getConfigurationSection("Seletores.Seletores." + Seletor + ".GUI") != null) {
                    itensSeletores = seletor.getSelector().getConfigurationSection("Seletores.Seletores." + Seletor + ".GUI.Itens");
                    for (String ItemDeInventario : itensSeletores.getKeys(false)) {
                        inv = Bukkit.createInventory(player, seletor.getSelector().getInt("Seletores.Seletores." + Seletor + ".GUI.tamanho"), seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".GUI.nome").replace("&", "§"));
                            itemGUI = new ItemStack(Material.valueOf(seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".item")));
                            meta = item.getItemMeta();
                            meta.setDisplayName(seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".nome").replace("&", "§"));
                            List<String> lores = seletor.getSelector().getStringList("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".lore");
                            for (int i = 0; i < lores.size(); i++) {
                                lores.set(i, lores.get(i).replaceAll("&", "§"));
                            }
                            meta.setLore(lores);
                            if (seletor.getSelector().getBoolean("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".glow")) {
                                meta.addEnchant(null, 0, false);
                            }
                            itemGUI.setItemMeta(meta);
                            int slot = seletor.getSelector().getInt("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".slot") + 1;
                            inv.setItem(1, itemGUI);

                        if (seletor.getSelector().getConfigurationSection("Seletores.Seletores." + Seletor + ".GUI.Itens.Padrão") != null) {
                            for (int i = 0; i > inv.getSize(); i++) {
                                itemPadrão = new ItemStack(Material.valueOf(seletor.getSelector().getString("Seletores.Seletores." + Seletor + ".GUI.Itens.Padrão.item")));
                                meta = item.getItemMeta();
                                meta.setDisplayName("");
                                item.setItemMeta(meta);
                                inv.setItem(i, itemPadrão);
                            }
                        }
                    }
                    inventories.add(inv);

                } else {
                    inventories.add(null);
                }
            }
        }
        return inventories;
    }

    public Inventory getSeletor(Player player) {
        Inventory inv = null;
        if (this.getSeletorItens().contains(player.getItemInHand())) {
            int posição = this.getSeletorItens().indexOf(player.getItemInHand());
            for (int i = 0; i < this.getSelectorInventories(player).size(); i++) {
                inv = this.getSelectorInventories(player).get(posição);
            }
        }
        return inv;

    }

    public void giveItens(Player player) {
        if (Seletores != null && !(Seletores.getKeys(false).isEmpty())) {
            for (String Seletor : Seletores.getKeys(false)) {
                for (ItemStack item : this.getSeletorItens()) {
                    int slot = seletor.getSelector().getInt("Seletores.Seletores." + Seletor + ".slot") + 1;
                    if (player.hasPermission("xg7lobby.admin")) {
                        player.getPlayer().getInventory().addItem(item);

                    } else {
                        player.getPlayer().getInventory().setItem(slot, item);
                    }
                }
            }
        }
    }
    public void executeAction(Player player) {
        if (Seletores != null && !(Seletores.getKeys(false).isEmpty())) {
            for (String Seletor : Seletores.getKeys(false)) {
                if (seletor.getSelector().getConfigurationSection("Seletores.Seletores." + Seletor + ".GUI") != null) {
                    itensSeletores = seletor.getSelector().getConfigurationSection("Seletores.Seletores." + Seletor + ".GUI.Itens");
                    for (String ItemDeInventario : itensSeletores.getKeys(false)) {
                        for (String ação : seletor.getSelector().getStringList("Seletores.Seletores." + Seletor + ".GUI.Itens." + ItemDeInventario + ".ações")) {
                            if (ação != null) {
                                String algo;
                                String algo1;
                                if (ação.startsWith("[MENSAGEM] ")) {
                                    algo = ação.replace("[MENSAGEM] ", "");
                                    algo = ChatColor.translateAlternateColorCodes('&', ação);
                                    player.sendMessage(algo);

                                } else if (ação.startsWith("[COMANDO] ")) {
                                    algo = ação.replace("[COMANDO] ", "");
                                    player.performCommand(algo);
                                } else if (ação.startsWith("[CONSOLE] ")) {
                                    algo = ação.replace("[CONSOLE] ", "");
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender().getServer().getConsoleSender(), algo);
                                } else if (ação.startsWith("[BUNGEE]")) {
                                    algo = ação.replace("[BUNGEE]", "");
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender().getServer().getConsoleSender(), "bungee:tp " + player + algo);
                                } else if (ação.startsWith("[SOM] ")) {
                                    float volume;
                                    algo = ação.replace("[SOM] ", "");
                                    algo1 = ação.replaceAll("[\\D]", "");
                                    volume = Float.parseFloat(algo1);
                                    player.playSound(player.getLocation(), algo, volume, volume);
                                }
                            }
                        }
                    }
                } else {
                    for (String ação : seletor.getSelector().getStringList("Seletores.Seletores." + Seletor + ".ações")) {
                        if (ação != null) {
                            String algo;
                            String algo1;
                            if (ação.startsWith("[MENSAGEM] ")) {
                                algo = ação.replace("[MENSAGEM] ", "");
                                algo = ChatColor.translateAlternateColorCodes('&', ação);
                                player.sendMessage(algo);

                            } else if (ação.startsWith("[COMANDO] ")) {
                                algo = ação.replace("[COMANDO] ", "");
                                player.performCommand(algo);
                            } else if (ação.startsWith("[CONSOLE] ")) {
                                algo = ação.replace("[CONSOLE] ", "");
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender().getServer().getConsoleSender(), algo);
                            } else if (ação.startsWith("[BUNGEE]")) {
                                algo = ação.replace("[BUNGEE]", "");
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender().getServer().getConsoleSender(), "bungee:tp " + player + algo);
                            } else if (ação.startsWith("[ABRIR] ")) {
                                algo = ação.replace("[ABRIR] ", "");
                                for (Inventory inv : this.getInventories(player)) {
                                    if (inv.getType().getDefaultTitle().equals(algo)) {
                                        player.openInventory(inv);
                                    }
                                }
                            } else if (ação.startsWith("[SOM] ")) {
                                float volume;
                                algo = ação.replace("[SOM] ", "");
                                algo1 = ação.replaceAll("[\\D]", "");
                                volume = Float.parseFloat(algo1);
                                player.playSound(player.getLocation(), algo, volume, volume);

                            } else if (ação.startsWith("[CLOSE] ")) {
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }

    }

    public List<Inventory> getInventories(Player player) {
        int id;
        Inventory inv = null;
        ItemMeta meta;
        int slotIgnorado;
        List<Inventory> inventories = new ArrayList<>();
        if (Inventários != null && !(Inventários.getKeys(false).isEmpty())) {
            for (String Inventarios : Inventários.getKeys(false)) {
                inv = Bukkit.createInventory(player, seletor.getSelector().getInt("Seletores.Inventários." + Inventarios + "tamanho"), seletor.getSelector().getString("Seletores." + Inventarios + "nome_inv").replace("&", "§"));
                for (String itens : seletor.getSelector().getConfigurationSection("Seletores.Inventários." + Inventarios + ".Itens").getKeys(false)) {
                    itemInventory = new ItemStack(Material.valueOf(seletor.getSelector().getString("Inventários." + Inventarios + ".Itens." + itens + ".item")));
                    meta = itemInventory.getItemMeta();
                    meta.setDisplayName(seletor.getSelector().getString("Seletores.Inventários." + Inventarios + ".Itens." + itens + ".nome").replace("&", "§"));
                    List<String> lores = seletor.getSelector().getStringList("Seletores.Inventários." + Inventarios + ".Itens." + itens + ".lore");
                    for (int i = 0; i < lores.size(); i++) {
                        lores.set(i, lores.get(i).replaceAll("&", "§"));
                    }
                    meta.setLore(lores);
                    if (seletor.getSelector().getBoolean("Seletores.Inventários." + Inventarios + ".Itens." + itens + ".glow")) {
                        meta.addEnchant(null, 0, false);
                    }

                    slotIgnorado = seletor.getSelector().getInt("Seletores.Inventários." + Inventarios + ".Itens." + itens + ".slot");


                    item.setItemMeta(meta);
                    inv.setItem(slotIgnorado, itemInventory);
                    if (seletor.getSelector().getConfigurationSection("Seletores.Inventários." + Inventarios + ".Itens.Padrão") != null) {
                        for (int i = 0; i > seletor.getSelector().getInt("Seletores.Inventários." + Inventarios + "tamanho"); i++) {
                            if (i != slotIgnorado) {
                                itemPadrão = new ItemStack(Material.valueOf(seletor.getSelector().getString("Seletores.Inventários." + Inventarios + ".Itens.Padrão.item")));
                                meta = item.getItemMeta();
                                meta.setDisplayName("");
                                item.setItemMeta(meta);
                                inv.setItem(i, itemPadrão);
                            }
                        }

                    }
                    inventories.add(inv);

                }
                return inventories;
            }
        }
        return null;

    }

}
