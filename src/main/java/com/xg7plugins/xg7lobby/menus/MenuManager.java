package com.xg7plugins.xg7lobby.menus;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MenuManager {

    private static final List<Menu> menus = new ArrayList<>();
    @Getter
    private static final HashMap<String, HashMap<String, InventoryItem>> storedItems = new HashMap<>();

    public static void load() {

        for (FileConfiguration configuration : Config.getMenus()) {

            Menu menu = new Menu(configuration.getString("id"), configuration.getString("title").startsWith("[CENTER] ") ? Text.translateColorCodes(Text.getCentralizedText(Text.PixelsSize.INV.getPixels(), configuration.getString("title"))) : Text.translateColorCodes(configuration.getString("title")), configuration.getInt("rows") * 9);

            if (!configuration.getString("fill-item").equals("AIR") && configuration.get("fill-item") != null) menu.setFillItem(new InventoryItem(XMaterial.valueOf(configuration.getString("fill-item")).parseItem().getData(), " ", new ArrayList<>(), 1, -1));

            for (String s : configuration.getConfigurationSection("items").getKeys(false)) {

                String path = "items." + s + ".";
                int slot = configuration.getInt(path + "slot");

                if (configuration.getString(path + "item") != null) {
                    path = "stored-items." + configuration.getString("item") + ".";
                }

                menu.addItems(getItemByConfig(configuration, path, slot));

            }

            if (configuration.getConfigurationSection("stored-items") != null) storedItems.put(menu.getId(), configuration.getConfigurationSection("stored-items").getKeys(false).stream().collect(Collectors.toMap(s -> s, s -> getItemByConfig(configuration, "stored-items." + s + ".", -1), (a, b) -> b, HashMap::new)));

            menus.add(menu);
        }
        System.out.println(menus.size());
    }

    public static InventoryItem getItemByConfig(FileConfiguration configuration, String path, int slot) {
        if (configuration.getString(path + "material").startsWith("PLAYER_HEAD")) {

            String value = configuration.getString(path + "material").split(", ")[1];
            InventoryItem.SkullInventoryItem skullInventoryItem = new InventoryItem.SkullInventoryItem(
                    Text.translateColorCodes(configuration.getString(path + "name")), configuration.getStringList(path + "lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()), configuration.getInt(path + "amount"), slot - 1
            );

            if (value.startsWith("OWNER=")) {
                skullInventoryItem.setOwner(value.substring(7), Bukkit.getServer().getOnlineMode());
            }
            if (value.startsWith("VALUE=")) {
                skullInventoryItem.setValue(value.substring(7));
            }

            if (configuration.getBoolean(path + "glow")) {
                skullInventoryItem.addEnchant(Enchantment.DURABILITY, 1);
            }

            return skullInventoryItem;
        }

        InventoryItem item = new InventoryItem(
                XMaterial.valueOf(configuration.getString(path + "material")).parseItem().getData() , Text.translateColorCodes(configuration.getString(path + "name")), configuration.getStringList(path + "lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()), configuration.getInt(path + "amount"), slot - 1
        );
        if (configuration.getBoolean(path + "glow")) {
            item.addEnchant(Enchantment.DURABILITY, 1);
        }
        return item;
    }

    public static Menu getById(String id) {
        return menus.stream().filter(menu -> menu.getId().equals(id)).findFirst().orElse(null);
    }


}
