package com.xg7plugins.xg7lobby.commands.toggleCommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.XG7Menus;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.modules.xg7menus.menus.gui.Menu;
import com.xg7plugins.modules.xg7menus.menus.holders.PlayerMenuHolder;
import com.xg7plugins.modules.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        name = "vanish",
        permission = "xg7lobby.command.vanish",
        description = "Vanish command",
        syntax = "/vanish",
        isPlayerOnly = true,
        isAsync = false
)
public class VanishCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(((Player)sender).getUniqueId(), false).join();

        lobbyPlayer.setPlayerHiding(!lobbyPlayer.isPlayerHiding());

        PlayerMenuHolder playerMenu = XG7Menus.getInstance().getPlayerMenuHolder(lobbyPlayer.getPlayerUUID());
        if (playerMenu != null) Menu.refresh(playerMenu);

        Text.fromLang(sender, XG7Lobby.getInstance(), lobbyPlayer.isPlayerHiding() ? "hide-players.hide" : "hide-players.show").thenAccept(text -> text.send(sender));
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.ENDER_EYE, this);
    }
}
