package com.xg7network.xg7lobby.Utils.PluginInventories;

/*

    This class was made by DaviXG7 to facilitate
    the creation of plugin items and inventories.

    The class is free to use if this text is copied
    into your plugin and your plugin is free.

    Thanks for reading/using my code <3

 */

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class Item {

    private Action action;
    private ItemStack itemStack;
    private int slot;
    private String id;

    public Item(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, Action action) {

        this.action = action;

        this.slot = slot;
        this.id = UUID.randomUUID().toString();

        String[] materialByte = material.split(", ");

        ItemMeta meta;

        if (material.contains(", ")) {

            if (materialByte[0].equals("PLAYER_HEAD") && (materialByte[1].startsWith("OWNER=") || materialByte[1].startsWith("VALUE="))) {

                if (materialByte[1].startsWith("OWNER=")) {

                    String playername = materialByte[1].replace("OWNER=", "");

                    boolean skull = Arrays.asList(Material.values())
                            .stream()
                            .map(Material::name)
                            .collect(Collectors.toList())
                            .contains("PLAYER_HEAD");

                    OfflinePlayer player1 = playername.equals("THIS_PLAYER") ? Bukkit.getOfflinePlayer(player.getUniqueId()) : Bukkit.getOfflinePlayer(playername);


                    this.itemStack = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

                    SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();

                    skullMeta.setOwner(player1.getName());

                    this.itemStack.setItemMeta(skullMeta);

                } else {

                    String texture = materialByte[1].replace("VALUE=", "");

                    boolean skull = Arrays.asList(Material.values())
                            .stream()
                            .map(Material::name)
                            .collect(Collectors.toList())
                            .contains("PLAYER_HEAD");

                    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                    gameProfile.getProperties().put("textures", new Property("textures", texture));

                    this.itemStack = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

                    SkullMeta skullMeta = (SkullMeta) this.itemStack .getItemMeta();

                    try {
                        Field profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(skullMeta, gameProfile);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    this.itemStack.setItemMeta(skullMeta);

                }


            } else {
                MaterialData data = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));
                this.itemStack = data.toItemStack(ammount);
            }
        } else {
            this.itemStack = new ItemStack(Material.getMaterial(material), ammount);
        }

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("xg7lhelpid", this.id);
        this.itemStack = nbtItem.getItem();

        meta = this.itemStack.getItemMeta();

        meta.setDisplayName(TextUtil.get(name, player));

        meta.setLore(Arrays.asList(TextUtil.get(lore, player).split(" /// ")));
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

    public String getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }
}