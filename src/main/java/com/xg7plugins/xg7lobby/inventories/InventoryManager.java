package com.xg7plugins.xg7lobby.inventories;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.libs.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.Pair;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.menu.LobbyItem;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class InventoryManager {
    @Getter
    private final HashMap<String, BaseMenu> inventoriesMap = new HashMap<>();


    public InventoryManager(XG7Lobby lobby, String... defaultInventories) {

        File folderFile = new File(lobby.getDataFolder(), "menus");

        boolean existsBefore = folderFile.exists();

        if (!existsBefore) folderFile.mkdirs();

        List<File> files = new ArrayList<>(Arrays.asList(folderFile.listFiles()));
        if (files == null) return;

        for (String inventory : defaultInventories) {
            File file = new File(folderFile, inventory + ".yml");
            if (!file.exists() && !existsBefore) {
                lobby.saveResource("menus/" + inventory + ".yml", false);
                files.add(file);
            }
        }

        for (File file : files) {
            Config config = new Config(lobby, YamlConfiguration.loadConfiguration(file));
            String id = config.get("id", String.class).orElse("No id");

            LobbyInventoryBuilder builder = new LobbyInventoryBuilder(id, config);

            BaseMenu menu = builder.loadMenu();

            if (menu instanceof PlayerMenu && (inventoriesMap.values().stream().anyMatch(m -> m instanceof LobbySelector))) throw new IllegalArgumentException("Only one player menu can be registered");

            inventoriesMap.put(id, menu);
        }
    }

    public static LobbyItem fromConfig(Config config, String path) {

        Item item = Item.from(config.get("items." + path + ".material", String.class).orElse("AIR"))
                .amount(config.get("items." + path + ".amount", Integer.class).orElse(1))
                .name(config.get("items." + path + ".name", String.class).orElse("No name"))
                .lore(config.getList("items." + path + ".lore", String.class).orElse(Collections.emptyList()))
                .setNBTTag("actions", config.getList("items." + path + ".actions", String.class).orElse(Collections.emptyList()));

        if (config.get("items." + path + ".glow", Boolean.class).orElse(false)) {
            item.enchant(Enchantment.DURABILITY, 1);

        }

        Pair<Condition, String> condition = config.contains("items." + path + ".conditional") ? Condition.extractCondition(config.get("items." + path + ".conditional", String.class).orElse("[IF: true]") + " ") : new Pair<>(Condition.IF, "true");

        String otherItemPath = config.contains("items." + path + ".if-false") ? config.get("items." + path + ".if-false", String.class).orElse(null) : null;

        return new LobbyItem(item, path, condition, otherItemPath);

    }

    public BaseMenu getInventory(String id) {
        return inventoriesMap.get(id);
    }
    public void openMenu(String id, Player player) {
        inventoriesMap.get(id).open(player);
    }

    public List<BaseMenu> getInventories() {
        return new ArrayList<>(inventoriesMap.values());
    }

}
