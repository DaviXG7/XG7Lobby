package com.xg7network.xg7lobby.Module.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Utils.PrivateInforations.VerfVersion;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class WarnVersion extends Module {

    public WarnVersion(XG7Lobby plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (VerfVersion.verifyUpdate()) {
                        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("warning-version")
                        && player.hasPermission("xg7lobby.admin")) {
                            player.sendMessage(prefix + ChatColor.GREEN + "A new version of XG7Lobby is available! Please update to new version to avoid bugs and maybe get more resources!");

                        }
                    }
                }

            },600, 4800);
    }

    @Override
    public void onDisable() {

    }
}
