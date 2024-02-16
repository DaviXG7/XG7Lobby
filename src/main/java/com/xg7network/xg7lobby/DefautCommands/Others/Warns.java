package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Items.SkullInventoryItem;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Menus.Page.PagesMenu;
import com.xg7network.xg7menus.API.Inventory.SuperClasses.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Warns implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }

        Player player = (Player) commandSender;

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

        List<ItemStack> warnsitems = new ArrayList<>();

        for (int i = 0; i < data.getInfractions().size(); i++) {
            warnsitems.add(new com.xg7network.xg7menus.API.Inventory.SuperClasses.InventoryItem(Material.PAPER, "§f" + data.getInfractions().get(i).getWarn(), Collections.singletonList(data.getInfractions().get(i).getWhen()), 1, i, null).getItemStack());
        }
        PagesMenu pagesMenu = new PagesMenu(warnsitems, configManager.getConfig(ConfigType.SELECTORS).getString("warn-inventory.title"));

        pagesMenu.addItem(getWarnItem("warn-inventory.go-back-item", player,  () -> pagesMenu.goBack(player, player.getOpenInventory().getTopInventory())));
        pagesMenu.addItem(getWarnItem("warn-inventory.go-next-item", player,  () -> pagesMenu.goNext(player, player.getOpenInventory().getTopInventory())));


        pagesMenu.getPages().get(0).open(player);



        return true;

    }

    private InventoryItem getWarnItem(String path, Player player, Runnable runnable) {



        MaterialData materialData;

        if (configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").contains(", ")) {
            String[] materialByte = configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").split(", ");
            if (materialByte[0].equals("PLAYER_HEAD") && (materialByte[1].startsWith("OWNER=") || materialByte[1].startsWith("VALUE="))) {
                if (materialByte[1].startsWith("OWNER=")) {
                    String playername = materialByte[1].replace("OWNER=", "");
                    OfflinePlayer player1 = playername.equals("THIS_PLAYER") ? Bukkit.getOfflinePlayer(player.getUniqueId()) : Bukkit.getOfflinePlayer(playername);
                    SkullInventoryItem skullInventoryItem = new SkullInventoryItem(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name"), configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore"), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") -1, runnable, player1.getPlayer());
                    if (configManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow")) skullInventoryItem.addEnchant(Enchantment.DURABILITY, 1);
                    return skullInventoryItem;
                } else {
                    String value = materialByte[1].replace("VALUE=", "");
                    SkullInventoryItem skullInventoryItem = new SkullInventoryItem(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name"), configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore"), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") -1, runnable, value);
                    if (configManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow")) skullInventoryItem.addEnchant(Enchantment.DURABILITY, 1);
                    return skullInventoryItem;
                }
            }
            materialData = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));
            InventoryItem inventoryItem = new InventoryItem(materialData,
                    configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name"),
                    configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore"),
                    configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"),
                    configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") - 1,
                    runnable
            );
            if (configManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow")) inventoryItem.addEnchant(Enchantment.DURABILITY, 1);
            return inventoryItem;
        } else {
            materialData = new MaterialData(Material.valueOf(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material")));
            InventoryItem inventoryItem = new InventoryItem(materialData,
                    configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name"),
                    configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore"),
                    configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"),
                    configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") - 1,
                    runnable
            );
            if (configManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow")) inventoryItem.addEnchant(Enchantment.DURABILITY, 1);
            return inventoryItem;
        }

    }


}
