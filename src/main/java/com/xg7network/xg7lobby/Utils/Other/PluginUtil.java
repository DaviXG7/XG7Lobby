package com.xg7network.xg7lobby.Utils.Other;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.placeholderapi;

public class PluginUtil {

    public static boolean isInWorld(World world) {
        return configManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds").contains(world.getName());
    }
    public static boolean isInWorld(Player player) {
        return configManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds").contains(player.getWorld().getName());
    }

    public static String setPlaceHolders(String s, Player p) {
        return placeholderapi ? PlaceholderAPI.setPlaceholders(p, s) : s;
    }

    public static boolean hasPermission(Player player, PermissionType permissionType, String message) {
        if (player.hasPermission(permissionType.getPerm())) return true;
        else {
            TextUtil.send(message, player);
            return false;
        }
    }

    public static boolean hasPermission(CommandSender sender, PermissionType permissionType, String message) {
        if (sender instanceof Player) {
            if (sender.hasPermission(permissionType.getPerm())) return true;
            else {
                TextUtil.send(message, (Player) sender);
                return false;
            }
        }
        return true;
    }

    public static PotionEffect getEffect(String s) {

        String[] effect = s.split(", ");

        switch (effect.length) {
            case 2:
                return new PotionEffect(PotionEffectType.getByName(effect[0]), 20, Integer.valueOf(effect[1]) - 1);
            case 3:
                return new PotionEffect(PotionEffectType.getByName(effect[0]), Integer.valueOf(effect[1]), Integer.valueOf(effect[2]) - 1);
        }
        return null;
    }

    public static void playSound(Player player, String s) {

        String[] s2 = s.split(", ");
        try {
            player.playSound(player.getLocation(), Sound.valueOf(s2[0]), Float.parseFloat(s2[1]), Float.parseFloat(s2[2]));
        } catch (Exception ignored) {}


    }
}
