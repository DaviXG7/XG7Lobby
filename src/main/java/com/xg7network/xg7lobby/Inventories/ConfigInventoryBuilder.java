package com.xg7network.xg7lobby.Inventories;


import com.xg7network.xg7menus.API.Inventory.Menus.Items.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Items.Others.SkullInventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.StandardMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigInventoryBuilder {

    private String title;
    private int slots;
    private int id;
    private List<InventoryItem> items;



    public ConfigInventoryBuilder(FileConfiguration configuration) {

        this.title = configuration.getString("title");
        this.id = configuration.getInt("id");
        this.slots = configuration.getInt("rows") * 9;

        items = new ArrayList<>();

        for (String path : configuration.getConfigurationSection("items").getKeys(false)) {
            if (path.equals("fill-item")) return;

            String[] materialName = configuration.getString("items." + path + ".material").split(", ");
            MaterialData data;

            String name = configuration.getString("items." + path + ".name");
            List<String> lore = configuration.getStringList("items." + path + ".lore");
            int ammount = configuration.getInt("items." + path + ".amount");
            int slot = configuration.getInt("items." + path + ".slot");
            boolean glow = configuration.getBoolean("items." + path + ".glow");
            List<String> actions = configuration.getStringList("items." + path + ".actions");


            data = new MaterialData(Material.valueOf(materialName[0]));
            if (materialName.length == 2) {
                if (materialName[0].equals("PLAYER_HEAD")) {
                    String skin = materialName[1];

                    if (skin.startsWith("OWNER=")) {

                        items.add(
                                glow ?
                                        new SkullInventoryItem(
                                            name, lore, ammount, slot, Bukkit.getOfflinePlayer(skin.substring(6)).getUniqueId()

                                        ).addEnchant(Enchantment.DURABILITY, 1)
                                        :
                                        new SkullInventoryItem(
                                                name, lore, ammount, slot, Bukkit.getOfflinePlayer(skin.substring(6)).getUniqueId()

                                                )
                        );

                    }
                    if (skin.startsWith("VALUE=")) {
                        items.add(
                                glow ?
                                        new SkullInventoryItem(
                                                name, lore, ammount, slot, skin.substring(6)

                                        ).addEnchant(Enchantment.DURABILITY, 1)
                                        :
                                        new SkullInventoryItem(
                                                name, lore, ammount, slot, skin.substring(6)
                                        )
                        );
                    }

                    throw new IllegalArgumentException("[XG7LOBBY] MENU WITH ID " + id + ", HAVE A SYNTAX ERROR ON AN SKULL ITEM! " + " The skull doesn't have an owner or a skin value");

                }

                data = new MaterialData(Material.getMaterial(materialName[0]), Byte.parseByte(materialName[1]));
            }



            items.add(
                    new InventoryItem(
                            data,name,lore,ammount,slot
                    )
            )


        }



    }


    public StandardMenu build(Player player) {

    }

}
