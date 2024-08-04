package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.menus.MenuManager;
import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbygui";
    }
    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.GLASS_PANE.parseItem(), "§6GUI command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Opens a gui";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbygui [MENUID]";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.GUI;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (MenuManager.getById(args[0]) == null) {
            Log.severe("The menu with gui " + args[0] + " doesn't exist!");
            Text.send("&cThe menu with gui " + args[0] + " doesn't exist!", sender);
            return true;
        }
        MenuManager.openById((Player) sender, args[0]);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Collections.singletonList("id") : new ArrayList<>();
    }
}
