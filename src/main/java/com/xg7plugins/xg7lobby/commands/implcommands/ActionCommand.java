package com.xg7plugins.xg7lobby.commands.implcommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbyaction";
    }
    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.REDSTONE_TORCH.parseItem(), "§6Actions command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }
    @Override
    public String getDescription() {
        return "Executes an action";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyaction [Action]";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.ACTION;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Action.execute(String.join(" ", args), (Player) sender);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.singletonList("action");
    }
}
