package com.xg7network.xg7lobby.inventories.inventory;

import com.xg7network.xg7lobby.inventories.Action;
import com.xg7network.xg7lobby.utils.Other.PluginUtil;
import com.xg7network.xg7menus.API.Inventory.Menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigInventoryBuilder {

    private String title;
    private int slots;
    private String id;
    private List<InventoryItem> items;
    private HashMap<Integer, List<Action>> actions;
    private InventoryItem fillerItem;
    private List<Integer> fillerItemSlots;
    private HashMap<String, InventoryItem> storedItems;



    public ConfigInventoryBuilder(FileConfiguration configuration) throws Exception {

        this.title = configuration.getString("title");
        this.id = configuration.getInt("id") + "";
        this.slots = configuration.getInt("rows") * 9;

        this.items = new ArrayList<>();
        this.actions = new HashMap<>();
        this.storedItems = new HashMap<>();

        for (String path : configuration.getConfigurationSection("stored-items").getKeys(false)) {
            path = "stored-items." + path;

            String[] materialName = configuration.getString(path + ".material").split(", ");
            MaterialData data;

            String name = configuration.getString(path + ".name");
            List<String> lore = configuration.getStringList(path + ".lore");
            int ammount = configuration.getInt(path + ".amount");
            boolean glow = configuration.getBoolean(path + ".glow");
            List<String> actions = configuration.getStringList(path + ".actions");
            storedItems.put(
                    path.split("\\.")[0],
                    getItem(materialName,name,lore,ammount,-1,glow)
            );

        }


        for (String path : configuration.getConfigurationSection("items").getKeys(false)) {
            path = "items." + path;
            if (path.equals("filler-item")) {
                String[] materialName = configuration.getString("items.filler-item.material").split(", ");
                if (materialName[0] == null && materialName[0].equals("AIR")) continue;
                fillerItem = getItem(materialName,"",null,1,1,false);
                fillerItemSlots = !configuration.get("items.filler-item.slots").equals(-1) ? configuration.getIntegerList("items.filler-item.slots") : Collections.singletonList(-1);
                continue;
            }

            int slot = configuration.getInt(path + ".slot") -1;

            if (configuration.getString(path + ".stored-item") != null) path = "stored-items." + configuration.getString(path + ".stored-item");


            String[] materialName = configuration.getString(path + ".material").split(", ");
            MaterialData data;

            String name = configuration.getString(path + ".name");
            List<String> lore = configuration.getStringList(path + ".lore");
            int ammount = configuration.getInt(path + ".amount");
            boolean glow = configuration.getBoolean(path + ".glow");
            List<String> actions = configuration.getStringList(path + ".actions");

            this.actions.put(
                    slot,
                    actions.stream().map(Action::new).collect(Collectors.toList())
            );

            items.add(getItem(materialName,name,lore,ammount,slot,glow));

        }



    }

    public static InventoryItem getItem(String[] material, String name, List<String> lore, int ammount,  int slot, boolean glow) throws Exception {
        MaterialData data;
        if (material.length == 2) {
            if (material[0].equals("PLAYER_HEAD")) {
                String skin = material[1];

                if (skin.startsWith("OWNER=")) {

                    return
                            glow ?
                                    new InventoryItem.SkullInventoryItem(
                                            name, lore, ammount, slot, Bukkit.getOfflinePlayer(skin.substring(6)).getUniqueId()

                                    ).addEnchant(Enchantment.DURABILITY, 1)
                                    :
                                    new InventoryItem.SkullInventoryItem(
                                            name, lore, ammount, slot, Bukkit.getOfflinePlayer(skin.substring(6)).getUniqueId()

                                    )
                            ;

                }
                if (skin.startsWith("VALUE=")) {
                    return
                            glow ?
                                    new InventoryItem.SkullInventoryItem(
                                            name, lore, ammount, slot, skin.substring(6)

                                    ).addEnchant(Enchantment.DURABILITY, 1)
                                    :
                                    new InventoryItem.SkullInventoryItem(
                                            name, lore, ammount, slot, skin.substring(6)
                                    )
                            ;
                }

                throw new Exception("[XG7LOBBY] A MENU HAVE A SYNTAX ERROR ON A SKULL ITEM! The skull doesn't have an owner or a skin value");
            }

            data = new MaterialData(Material.getMaterial(material[0]), Byte.parseByte(material[1]));
        }
        else data = new MaterialData(Material.valueOf(material[0]));

        return new InventoryItem(data,name,lore,ammount,slot);

    }
    public String getId() {
        return id;
    }
    public ConfigInventory build(Player player) {

        ConfigInventory menu = new ConfigInventory(title,slots,id);
        items.forEach(item -> {
            if (PluginUtil.placeholderapi()) item.setPlaceholders(player);
            menu.addItems(item);
        });
        menu.setStoredItems(storedItems);
        menu.setActions(actions);
        if (fillerItem != null && !fillerItem.getItemStack().getType().equals(Material.AIR)) {
            if (fillerItemSlots.get(0).equals(-1)) menu.setFillItem(fillerItem);
            else fillerItemSlots.forEach(slots -> menu.addNoListedItem(fillerItem.getItemStack(), slots));
        }

        return menu;
    }

}
