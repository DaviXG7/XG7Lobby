package com.xg7network.xg7lobby.Module.Events;

import java.io.File;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Ping implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("motd.enabled")) {
            String motd = String.join("\n", configManager.getConfig(ConfigType.CONFIG).getStringList("motd.text"));
            e.setMotd(TextUtil.get(motd));

            try {
                e.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
            } catch (Exception var4) {
                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("warning-image")) Bukkit.getConsoleSender().sendMessage(prefix + "Warning! Your server doesn't have image! If you want an image on your server, put an image file named icon.png on server directory!");
            }
        }

    }
}
