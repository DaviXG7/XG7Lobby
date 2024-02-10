package com.xg7network.xg7lobby.Utils.CustomInventories;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;

import de.tr7zw.changeme.nbtapi.NBTItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class InventoryItem {


    public Inventory inv;
    private int slot;
    private String path;
    private ItemStack itemStack;
    private List<String> actions;
    private Player player;
    private String id;

    public InventoryItem(String path, Inventory inv, Player player) {
        this.slot = configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") - 1;
        this.inv = inv;
        this.path = path;
        this.player = player;
        this.actions = configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".actions");

        this.id = UUID.randomUUID().toString();

        this.itemStack = getItemAndMaterial();

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("xg7linvid", this.id);
        this.itemStack = nbtItem.getItem();

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(TextUtil.get(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".name"), player));
        List<String> lore2 = new ArrayList<>();
        for (String l : configManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore")) {
            l = TextUtil.get(l, player);
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
            if (materialByte[0].equals("PLAYER_HEAD") && (materialByte[1].startsWith("OWNER=") || materialByte[1].startsWith("VALUE="))) {
                if (materialByte[1].startsWith("OWNER=")) {
                    String playername = materialByte[1].replace("OWNER=", "");

                    boolean skull = Arrays.asList(Material.values())
                            .stream()
                            .map(Material::name)
                            .collect(Collectors.toList())
                            .contains("PLAYER_HEAD");

                    OfflinePlayer player1 = playername.equals("THIS_PLAYER") ? Bukkit.getOfflinePlayer(player.getUniqueId()) : Bukkit.getOfflinePlayer(playername);


                    ItemStack cabeca = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

                    SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

                    skullMeta.setOwner(player1.getName());

                    cabeca.setItemMeta(skullMeta);


                    return cabeca;

                } else {

                    String texture = materialByte[1].replace("VALUE=", "");

                    if (texture.equals("THIS_PLAYER")) {

                        boolean skull = Arrays.asList(Material.values())
                                .stream()
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD");

                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(player.getUniqueId());


                        ItemStack cabeca = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

                        SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

                        try {

                            System.out.println(player1.getUniqueId());

                            HttpClient client = HttpClients.createDefault();
                            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + player1.getUniqueId();
                            HttpGet request = new HttpGet(url);
                            HttpResponse response = client.execute(request);

                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            StringBuilder result = new StringBuilder();
                            String line;

                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }

                            JsonObject profileData = new JsonParser().parse(result.toString()).getAsJsonObject();
                            JsonObject properties = profileData.getAsJsonArray("properties").get(0).getAsJsonObject();


                            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                            gameProfile.getProperties().put("textures", new Property("textures", properties.get("value").getAsString()));


                            try {
                                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                                profileField.setAccessible(true);
                                profileField.set(skullMeta, gameProfile);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            cabeca.setItemMeta(skullMeta);

                            skullMeta.setOwner(player1.getName());

                            cabeca.setItemMeta(skullMeta);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return cabeca;
                    } else {

                        boolean skull = Arrays.asList(Material.values())
                                .stream()
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD");

                        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                        gameProfile.getProperties().put("textures", new Property("textures", texture));

                        ItemStack cabeca = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

                        SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

                        try {
                            Field profileField = skullMeta.getClass().getDeclaredField("profile");
                            profileField.setAccessible(true);
                            profileField.set(skullMeta, gameProfile);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        cabeca.setItemMeta(skullMeta);

                        return cabeca;

                    }
                }

            } else {
                MaterialData data = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));
                return data.toItemStack(configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"));
            }

        } else {
            return new ItemStack(Material.valueOf(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").toUpperCase()), configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount"));
        }
    }

    public org.bukkit.inventory.Inventory getInv() {
        return inv.getInv();
    }
    public int getSlot() {
        return slot;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getActions() {
        return actions;
    }

    public String getId() {
        return id;
    }

}