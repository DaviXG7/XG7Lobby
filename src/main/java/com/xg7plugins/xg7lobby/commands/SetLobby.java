package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.XSeries.XMaterial;
import com.xg7plugins.libs.xg7menus.builders.item.ItemBuilder;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby implements ICommand {

    @Override
    public void onCommand(Command command, CommandSender sender, String label) {

        if (!(sender instanceof Player)) {
            ICommand.super.onCommand(command,sender,label);
            return;
        }

        Player player = (Player) sender;


    }

    @Override
    public ItemBuilder getIcon() {
        return ItemBuilder.commandIcon(XMaterial.BLAZE_ROD, this, XG7Lobby.getInstance());
    }
}
