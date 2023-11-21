package com.xg7network.xg7lobby.Utils.Inventory;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.XG7ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class SelectorItem {

    private int slot = -1;
    private String path;
    private ItemStack itemStack;
    private List<String> actions;

    private Player player;

    public SelectorItem(String path, Player player) {
        if (configManager.getConfig(ConfigType.SELECTORS).get(path + ".slot") != null) this.slot = configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") -1;

        this.path = path;
        this.player = player;
        this.actions = configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".actions");

        this.itemStack = getItemAndMaterial();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(XG7ChatUtil.getTexts(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name").replace("&", "§")));
        List<String> lore2 = new ArrayList<>();
        for (String l : configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore")) {
            l = PluginUtil.setPlaceHolders(l, player);
            l = XG7ChatUtil.getTexts(l.replace("&", "§"));
            lore2.add(l);
        }
        meta.setLore(lore2);
        if (configManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow")) meta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemStack.setItemMeta(meta);

    }

    /*
     * Isso é para caso a versão do servidor for antiga, ele pegar
     * o item pelo byte dele, por exemplo:
     * Lã azul: "WOOL, 3"
     */
    private ItemStack getItemAndMaterial() {
        if (configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").contains(", ")) {
            String[] materialByte = configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").split(", ");
            if (materialByte[0].equals("PLAYER_HEAD") && materialByte[1].startsWith("OWNER=")) {

                String playername = materialByte[1].replace("OWNER=", "");

                if (playername.equals("THIS_PLAYER")) {

                    ItemStack cabeca = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
                    cabeca.setItemMeta(skullMeta);

                    return cabeca;
                } else {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playername);

                    ItemStack cabeca = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

                    skullMeta.setOwningPlayer(offlinePlayer);
                    cabeca.setItemMeta(skullMeta);

                    return cabeca;
                }


            } else {
                MaterialData data = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));
                return data.toItemStack(configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"));
            }

        } else {
            return new ItemStack(Material.valueOf(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").toUpperCase()), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"));
        }
    }

    public String getPath() {
        return path;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }



    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getActions() {
        return actions;
    }


}
