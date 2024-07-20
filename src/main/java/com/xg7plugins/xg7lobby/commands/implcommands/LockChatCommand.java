package com.xg7plugins.xg7lobby.commands.implcommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LockChatCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbylockchat";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.BARRIER.parseMaterial(), "&6Unban command", Arrays.asList("&9Description: " + getDescription(), "&9Usage: &7&o" + getSyntax(), "&9Permission: &b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Locks the chat";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbylockchat";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.LOCKCHAT;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Config.set(ConfigType.DATA, "chat-locked", !Config.getBoolean(ConfigType.DATA, "chat-locked"));
        Config.save(ConfigType.DATA);
        Config.reload(ConfigType.DATA);

        Text.send(Config.getString(ConfigType.MESSAGES, Config.getBoolean(ConfigType.DATA, "chat-locked") ? "chat.on-lock" : "chat.on-unlock"), sender);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
