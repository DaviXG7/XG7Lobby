package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.InventoryItem;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SkullInventoryItem extends InventoryItem {

    private String value;
    private String owner;

    public SkullInventoryItem(String name, List<String> lore, int amount, int slot, Runnable runnable, String skinValue) {
        super(Arrays.asList(Material.values()).stream().map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD")
                ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : new MaterialData(Material.getMaterial("SKULL_ITEM"), (byte) 3),
                name, lore, amount, slot, runnable);


        setSkinValue(skinValue);
    }

    public SkullInventoryItem(String name, List<String> lore, int amount, int slot, Runnable runnable, UUID player) {
        super(Arrays.asList(Material.values()).stream().map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD")
                        ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : new MaterialData(Material.getMaterial("SKULL_ITEM"), (byte) 3),
                name, lore, amount, slot, runnable);
        setSkin(player);

    }

    public SkullInventoryItem(String name, List<String> lore, int amount, int slot, Runnable runnable, Player owner) {
        super(Arrays.asList(Material.values()).stream().map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD")
                        ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : new MaterialData(Material.getMaterial("SKULL_ITEM"), (byte) 3),
                name, lore, amount, slot, runnable);
        boolean skull = Arrays.asList(Material.values())
                .stream()
                .map(Material::name)
                .collect(Collectors.toList())
                .contains("PLAYER_HEAD");


        ItemStack cabeca = skull ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);

        SkullMeta skullMeta = (SkullMeta) cabeca.getItemMeta();

        skullMeta.setOwner(owner.getDisplayName());

        cabeca.setItemMeta(skullMeta);

    }

    private void setSkin(UUID player) {
        try {
            HttpClient client = HttpClients.createDefault();
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + player;
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

            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();


            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.itemStack.setItemMeta(skullMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSkinValue(String value) {

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));

        SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.itemStack.setItemMeta(skullMeta);

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        setSkinValue(value);
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner, boolean onlineMode) {
        ((SkullMeta) super.getItemStack().getItemMeta()).setOwner(owner);
        if (onlineMode) setSkin(Bukkit.getOfflinePlayer(owner).getUniqueId());
        this.owner = owner;
    }
}
