package com.xg7network.xg7lobby.Utils.CustomInventories.Config;

import com.xg7network.xg7lobby.Module.Inventories.Actions.Action;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Items.SkullInventoryItem;
import com.xg7network.xg7menus.API.Inventory.SuperClasses.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Objects;

public class ConfigInventoryItem extends InventoryItem {
    private List<String> actions;
    public ConfigInventoryItem(MaterialData materialData, String name, List<String> lore, int amount, int slot, List<String> actions, Player player) {
        super(materialData, name, lore, amount, slot, null);
        this.actions = actions;
        this.setPlayer(player);
    }
    public ConfigInventoryItem(Material material, String name, List<String> lore, int amount, int slot, List<String> actions, Player player) {
        super(material, name, lore, amount, slot, null);
        this.actions = actions;
        this.setPlayer(player);
    }

    public ConfigInventoryItem(ItemStack item, int slot, List<String> actions, Player player) {
        super(item, slot, null);
        this.actions = actions;
        this.setPlayer(player);
    }

    @Override
    public void execute() {
        for (String action : actions) new Action(player, action).execute();
    }

    public static ConfigInventoryItem fromConfig(FileConfiguration configuration, String path, Player player) {

        if (configuration.getString("items." + path + ".material").split(", ").length == 2) {

            String[] materialdata = configuration.getString("items." + path + ".material").split(", ");

            if (Objects.equals(materialdata[0], "PLAYER_HEAD")) {

                String ownerOrValue = materialdata[1].substring(6);

                if (materialdata[1].startsWith("VALUE=")) {

                    SkullInventoryItem item = new SkullInventoryItem(
                            configuration.getString("items." + path + ".name"),
                            configuration.getStringList("items." + path + ".lore"),
                            configuration.getInt("items." + path + ".amount"),
                            configuration.getInt("items." + path + ".slot") - 1,
                            null,
                            ownerOrValue
                    );
                    if (configuration.getBoolean("items." + path + ".glow")) item.addEnchant(Enchantment.DURABILITY, 1);
                    return new ConfigInventoryItem(item.getItemStack(), item.getSlot(), configuration.getStringList("items." + path + ".actions"), player);

                } else if (materialdata[1].startsWith("OWNER=")) {

                    SkullInventoryItem item = new SkullInventoryItem(
                            configuration.getString("items." + path + ".name"),
                            configuration.getStringList("items." + path + ".lore"),
                            configuration.getInt("items." + path + ".amount"),
                            configuration.getInt("items." + path + ".slot") - 1,
                            null,
                            ownerOrValue.equals("THIS_PLAYER") ? player : Bukkit.getOfflinePlayer(ownerOrValue).getPlayer()
                    );
                    if (configuration.getBoolean("items." + path + ".glow")) item.addEnchant(Enchantment.DURABILITY, 1);
                    return new ConfigInventoryItem(item.getItemStack(), item.getSlot(), configuration.getStringList("items." + path + ".actions"), player);

                }

                return null;

            } else {

                ConfigInventoryItem configInventoryItem =
                        new ConfigInventoryItem(
                                new MaterialData(
                                        Material.getMaterial(materialdata[0]),
                                        Byte.parseByte(materialdata[1])
                                ),
                                configuration.getString("items." + path + ".name"),
                                configuration.getStringList("items." + path + ".lore"),
                                configuration.getInt("items." + path + ".amount"),
                                configuration.getInt("items." + path + ".slot") - 1,
                                configuration.getStringList("items." + path + ".actions"),
                                player
                        );

                if (configuration.getBoolean("items." + path + ".glow"))
                    configInventoryItem.addEnchant(Enchantment.DURABILITY, 1);

                return configInventoryItem;


            }
        }

        ConfigInventoryItem configInventoryItem =
                new ConfigInventoryItem(
                        Material.getMaterial(configuration.getString("items." + path + ".material")),
                        configuration.getString("items." + path + ".name"),
                        configuration.getStringList("items." + path + ".lore"),
                        configuration.getInt("items." + path + ".amount"),
                        configuration.getInt("items." + path + ".slot") -1,
                        configuration.getStringList("items." + path + ".actions"),
                        player
                );

        if (configuration.getBoolean("items." + path + ".glow")) configInventoryItem.addEnchant(Enchantment.DURABILITY, 1);

        return configInventoryItem;
    }
}
