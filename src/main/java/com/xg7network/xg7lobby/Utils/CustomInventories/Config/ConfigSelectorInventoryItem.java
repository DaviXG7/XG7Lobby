package com.xg7network.xg7lobby.Utils.CustomInventories.Config;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Inventories.Actions.Action;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Items.ActionInventoryItem;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Items.SkullInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Objects;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class ConfigSelectorInventoryItem extends ActionInventoryItem {

    private List<String> actions;

    String path;

    public ConfigSelectorInventoryItem(MaterialData materialData, String name, List<String> lore, int amount, int slot, List<String> actions, Player player, String path) {
        super(materialData, name, lore, amount, slot, null, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK);
        setSecundaryAction(org.bukkit.event.block.Action.RIGHT_CLICK_AIR);
        this.actions = actions;
        this.setPlayer(player);
        this.path = path;
        setCooldown(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"));
        setCooldownMessage(TextUtil.get(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown")));
    }
    public ConfigSelectorInventoryItem(Material material, String name, List<String> lore, int amount, int slot, List<String> actions, Player player, String path) {
        super(material, name, lore, amount, slot, null, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK);
        setSecundaryAction(org.bukkit.event.block.Action.RIGHT_CLICK_AIR);
        this.actions = actions;
        this.setPlayer(player);
        this.path = path;
        setCooldown(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"));
        setCooldownMessage(TextUtil.get(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown")));
    }

    public ConfigSelectorInventoryItem(ItemStack item, int slot, List<String> actions, Player player, String path) {
        super(item, slot, null, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK);
        setSecundaryAction(org.bukkit.event.block.Action.RIGHT_CLICK_AIR);
        this.actions = actions;
        this.setPlayer(player);
        this.path = path;
        setCooldown(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"));
        setCooldownMessage(TextUtil.get(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown")));
    }

    @Override
    public void execute(Location location) {
        for (String action : actions) new Action(player, action).execute();
    }

    public static ConfigSelectorInventoryItem fromConfig(String path, Player player) {

        if (configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".material").split(", ").length == 2) {

            String[] materialdata = configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".material").split(", ");

            if (Objects.equals(materialdata[0], "PLAYER_HEAD")) {

                String ownerOrValue = materialdata[1].substring(6);

                if (materialdata[1].startsWith("VALUE=")) {

                    SkullInventoryItem item = new SkullInventoryItem(
                            configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".name"),
                            configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".lore"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".amount"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".slot") -1,
                            null,
                            ownerOrValue
                    );
                    if (configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.items." + path + ".glow")) item.addEnchant(Enchantment.DURABILITY, 1);

                    return new ConfigSelectorInventoryItem(item.getItemStack(), item.getSlot(), configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".actions"), player, path);

                } else if (materialdata[1].startsWith("OWNER=")) {

                    SkullInventoryItem item = new SkullInventoryItem(
                            configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".name"),
                            configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".lore"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".amount"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".slot") -1,
                            null,
                            ownerOrValue.equals("THIS_PLAYER") ? player : Bukkit.getOfflinePlayer(ownerOrValue).getPlayer()
                    );
                    if (configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.items." + path + ".glow")) item.addEnchant(Enchantment.DURABILITY, 1);

                    return new ConfigSelectorInventoryItem(item.getItemStack(), item.getSlot(), configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".actions"), player, path);

                }

                return null;

            }

            ConfigSelectorInventoryItem configInventoryItem =
                    new ConfigSelectorInventoryItem(
                            new MaterialData(
                                    Material.getMaterial(materialdata[0]),
                                    Byte.parseByte(materialdata[1])
                            ),
                            configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".name"),
                            configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".lore"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".amount"),
                            configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".slot") -1,
                            configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".actions"),
                            player, path
                    );

            if (configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.items." + path + ".glow")) configInventoryItem.addEnchant(Enchantment.DURABILITY, 1);

            return configInventoryItem;


        }

        ConfigSelectorInventoryItem configInventoryItem =
                new ConfigSelectorInventoryItem(
                        Material.getMaterial(configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".material")),
                        configManager.getConfig(ConfigType.SELECTORS).getString("selectors.items." + path + ".name"),
                        configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".lore"),
                        configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".amount"),
                        configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.items." + path + ".slot") -1,
                        configManager.getConfig(ConfigType.SELECTORS).getStringList("selectors.items." + path + ".actions"),
                        player, path
                );

        if (configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.items." + path + ".glow")) configInventoryItem.addEnchant(Enchantment.DURABILITY, 1);

        return configInventoryItem;
    }

    public String getPath() {
        return path;
    }
}
