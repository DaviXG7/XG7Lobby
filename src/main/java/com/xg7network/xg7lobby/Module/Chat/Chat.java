package com.xg7network.xg7lobby.Module.Chat;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Chat implements Listener {

    private static final FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);



    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (config.getBoolean("anti-swearing.enabled")) {
            for (String s : config.getStringList("anti-swearing.blocked-words")) {
                if (event.getMessage().contains(" " + s + " ") || event.getMessage().equalsIgnoreCase(s)) {

                    event.setCancelled(!player.hasPermission(PermissionType.CHAT_PALAVRAS.getPerm()));
                    if (event.isCancelled()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.badword"), player);

                }
            }
        }

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean("block-commands.enabled")) {

            for (String s : config.getStringList("block-commands.commands-blocked")) {
                if (event.getMessage().startsWith(s)) {

                    event.setCancelled(!player.hasPermission(PermissionType.CHAT_COMANDOS.getPerm()));
                    if (event.isCancelled()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("events.command-block"), player);

                }
            }

        }
    }


}
