package com.xg7plugins.xg7lobby.commands.toggleCommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        name = "vanish",
        permission = "xg7lobby.command.vanish",
        description = "Vanish command",
        syntax = "/vanish",
        isPlayerOnly = true,
        isAsync = true
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

        Text.formatLang(XG7Lobby.getInstance(), sender, lobbyPlayer.isPlayerHiding() ? "hide-players.hide" : "hide-players.show").thenAccept(text -> text.send(sender));
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.ENDER_EYE, this);
    }
}
