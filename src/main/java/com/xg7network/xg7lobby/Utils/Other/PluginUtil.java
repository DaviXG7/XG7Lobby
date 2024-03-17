package com.xg7network.xg7lobby.Utils.Other;

import com.xg7network.xg7lobby.Config.ConfigManager;
import com.xg7network.xg7lobby.Config.ConfigType;
import com.xg7network.xg7lobby.Config.PermissionType;
import com.xg7network.xg7lobby.Data.PlayerData;
import com.xg7network.xg7lobby.Data.PlayersManager;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.SimpleDateFormat;

public class PluginUtil {

    public static boolean placeholderapi() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static boolean isInWorld(World world) {
        return ConfigManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds").contains(world.getName());
    }
    public static boolean isInWorld(Player player) {
        return ConfigManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds").contains(player.getWorld().getName());
    }

    public static String setPlaceHolders(String s, Player p) {
        return placeholderapi() ? PlaceholderAPI.setPlaceholders(p, s) : setPluginPlaceholders(s, p);
    }

    public static boolean hasPermission(Player player, PermissionType permissionType) {
        if (player.hasPermission(permissionType.getPerm())) return true;
        TextUtil.send(ConfigManager.getConfig(ConfigType.MESSAGES).getString("error-messages.no-permission"), player);
        return false;

    }

    public static boolean hasPermission(CommandSender sender, PermissionType permissionType) {
        if (sender instanceof Player) {
            if (sender.hasPermission(permissionType.getPerm())) return true;

            TextUtil.send(ConfigManager.getConfig(ConfigType.MESSAGES).getString("error-messages.no-permission"), (Player) sender);
            return false;
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

    private static String setPluginPlaceholders(String s, Player player) {
        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());
        return s.replace("%xg7lobby_warns%", data.getInfractions().size() + "")
                .replace("%xg7lobby_chat_locked%", data.isMuted() + "")
                .replace("%xg7lobby_muted%", data.isMuted() + "")
                .replace("%xg7lobby_time_for_unmute%", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data.getLastDayToUnmute()))
                .replace("%xg7lobby_first_join%", data.getFirstJoin())
                .replace("%xg7lobby_lobby_location%", data.isMuted() + "")
                .replace("%xg7lobby_players_hide%", data.isPlayershide() + "");
    }
}
