package com.xg7network.xg7lobby.Utils.PluginInventories;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Item {

    private Action action;
    private ItemStack itemStack;
    private int slot;

    public Item(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, Action action) {

        this.action = action;

        this.slot = slot;

        String[] materialByte = material.split(", ");

        ItemMeta meta;

        if (material.contains(", ")) {

            if (materialByte[0].equals("PLAYER_HEAD") && materialByte[1].startsWith("OWNER=")) {

                String playername = materialByte[1].replace("OWNER=", "");

                boolean skull = Arrays.stream(Material.values())
                        .map(Material::name)
                        .collect(Collectors.toList())
                        .contains("PLAYER_HEAD");

                Material cabecatype = Material.matchMaterial(skull ? "PLAYER_HEAD" : "SKULL_ITEM");
                this.itemStack = new ItemStack(skull ? cabecatype : cabecatype, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();

                OfflinePlayer player1 = playername.equals("THIS_PLAYER") ? Bukkit.getOfflinePlayer(player.getUniqueId()) : Bukkit.getOfflinePlayer(playername);

                skullMeta.setOwningPlayer(player1);

                meta = skullMeta;

                this.itemStack.setItemMeta(meta);


            } else {
                MaterialData data = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));
                this.itemStack = data.toItemStack(ammount);
            }
        } else {
            this.itemStack = new ItemStack(Material.getMaterial(material), ammount);
        }

        meta = this.itemStack.getItemMeta();

        meta.setDisplayName(TextUtil.get(name, player));

        meta.setLore(Arrays.stream(TextUtil.get(lore, player).split(" /// ")).toList());
        if (glow) meta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemStack.setItemMeta(meta);

        this.itemStack.setItemMeta(meta);


    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void execute() {

        if (action == null) return;

        action.execute();


    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }
}
