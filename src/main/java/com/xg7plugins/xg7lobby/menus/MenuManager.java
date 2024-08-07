package com.xg7plugins.xg7lobby.menus;

import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MenuManager {

    private static final List<Menu> menus = new ArrayList<>();
    @Getter
    private static final HashMap<String, HashMap<String, InventoryItem>> storedItems = new HashMap<>();
    @Getter
    private static final HashMap<String, List<InventoryItem.SkullInventoryItem>> skulls = new HashMap<>();

    public static void load() {

        menus.clear();

        for (FileConfiguration configuration : Config.getMenus()) {

            Menu menu = new Menu(configuration.getString("id"), configuration.getString("title"), configuration.getInt("rows") * 9);

            if (configuration.get("fill-item") != null && !configuration.getString("fill-item").equals("AIR")) menu.setFillItem(new InventoryItem(XMaterial.matchXMaterial(configuration.getString("fill-item").toUpperCase()).get().parseItem(), " ", new ArrayList<>(), 1, -1));

            for (String s : configuration.getConfigurationSection("items").getKeys(false)) {

                String path = "items." + s + ".";
                int slot = configuration.getInt(path + "slot");

                if (configuration.getString(path + "item") != null) {
                    path = "stored-items." + configuration.getString("item") + ".";
                }

                if (getItemByConfig(configuration.getString("id"), configuration, path, slot) != null) menu.addItems(getItemByConfig(configuration.getString("id"), configuration, path, slot));

            }

            if (configuration.getConfigurationSection("stored-items") != null) storedItems.put(menu.getId(), configuration.getConfigurationSection("stored-items").getKeys(false).stream().collect(Collectors.toMap(s -> s, s -> getItemByConfig(menu.getId(), configuration, "stored-items." + s + ".", -1), (a, b) -> b, HashMap::new)));

            menus.add(menu);
        }
    }

    public static InventoryItem getItemByConfig(String id, FileConfiguration configuration, String path, int slot) {

        if (configuration.getString(path + "material").startsWith("PLAYER_HEAD") && configuration.getString(path + "material").split(", ").length == 2) {

            String value = configuration.getString(path + "material").split(", ")[1];
            InventoryItem.SkullInventoryItem skullInventoryItem = new InventoryItem.SkullInventoryItem(
                    Text.translateColorCodes(configuration.getString(path + "name")), configuration.getStringList(path + "lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()), configuration.getInt(path + "amount"), slot - 1
            );

            if (value.equals("THIS_PLAYER")) {
                skulls.putIfAbsent(id, new ArrayList<>());
                skulls.get(id).add(skullInventoryItem);
                return skullInventoryItem;
            }

            if (value.startsWith("OWNER=")) {
                skullInventoryItem.setOwner(value.substring(7), Bukkit.getServer().getOnlineMode());
            }
            if (value.startsWith("VALUE=")) {
                skullInventoryItem.setValue(value.substring(7));
            }

            if (configuration.get(path + "item-flags") != null) configuration.getStringList(path + "item-flags").stream().map(ItemFlag::valueOf).forEach(skullInventoryItem::addFlags);

            if (configuration.getBoolean(path + "glow")) {
                skullInventoryItem.addEnchant(Enchantment.DURABILITY, 1);
            }

            return skullInventoryItem;
        }


        InventoryItem item = new InventoryItem(
                XMaterial.matchXMaterial(configuration.getString(path + "material")).get().parseItem(), Text.translateColorCodes(configuration.getString(path + "name")), configuration.getStringList(path + "lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()), configuration.getInt(path + "amount"), slot - 1
        );
        if (configuration.get(path + "custom-model-data") != null) item.setCustomModelData(configuration.getInt(path + "custom-model-data"));

        if (configuration.get(path + "item-flags") != null) configuration.getStringList(path + "item-flags").stream().map(ItemFlag::valueOf).forEach(item::addFlags);

        if (configuration.getBoolean(path + "glow")) item.addEnchant(Enchantment.DURABILITY, 1);

        return item;
    }

    public static Menu getById(String id) {
        return menus.stream().filter(menu -> menu.getId().equals(id)).findFirst().orElse(null);
    }

    public static void openById(Player player, String id) {
        Menu menu = getById(id);
        String tempTitle = menu.getTitle();
        menu.setTitle(menu.getTitle().startsWith("[CENTER] ") ? Text.translateColorCodes(Text.getCentralizedText(Text.PixelsSize.INV.getPixels(), Text.setPlaceholders(menu.getTitle().substring(9), player))) : Text.translateColorCodes(menu.getTitle()));
        menu.open(player);
        menu.setTitle(tempTitle);
        if (skulls.containsKey(id)) skulls.get(id).forEach(item -> {
            if (item.getSlot() == -1) return;
            menu.updateInventory(player, item.setOwner(player.getName(), Bukkit.getOnlineMode()));
        });
    }



}
