package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class LockChatCommand implements CommandExecutor, Listener {



    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!PluginUtil.hasPermission(commandSender, PermissionType.LOCK_CHAT, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        configManager.getConfig(ConfigType.DATA).set("chat-locked", !configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked"));

        configManager.saveConfig(ConfigType.DATA);

        commandSender.sendMessage(configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked") ?
                prefix + ChatColor.GREEN + "The chat has been locked!" : prefix + ChatColor.GREEN + "The chat has been unlocked!"
        );


        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        if (configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked")) {
            event.setCancelled(!event.getPlayer().hasPermission(PermissionType.LOCK_CHAT.getPerm()));
            new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-lock-chat")).send(event.getPlayer());
        }
    }


}
