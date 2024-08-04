package com.xg7plugins.xg7lobby.commands.implcommands.chatcommands;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClearChat implements Command {
    @Override
    public String getName() {
        return "xg7lobbyclearchat";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.BOOK.parseMaterial(), "§6ClearChat command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Clears the chat";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyclearchat";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.CLEAR_CHAT;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Bukkit.broadcastMessage(StringUtils.repeat(" \n", 100));
        Text.send(Config.getString(ConfigType.MESSAGES, "chat.cleared"), sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
