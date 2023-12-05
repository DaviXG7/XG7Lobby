package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.ConfigType;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Infractions {

    private static FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);

    public static void verify(Player player, int infractions) {

        if (config.getInt("infractions-to-ban") > 0) {
            if (infractions == config.getInt("infractions-to-ban")) {
                player.kickPlayer("");
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ChatColor.RED + "You have been banned for breaking the rules multiple times, check with an admin if you think it is incorrect and avoid breaking the rules!", null, null);
            }
        }

        if (config.getInt("min-infractions-to-kick") > 0) {
            if (infractions >= config.getInt("min-infractions-to-kick")) {
                player.kickPlayer(ChatColor.RED + "You've been kicked out for violating the rules too many times!");
            }
        }

    }

}
