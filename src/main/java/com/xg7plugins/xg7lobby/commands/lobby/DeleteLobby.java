package com.xg7plugins.xg7lobby.commands.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import org.bukkit.command.CommandSender;

@Command(
        name = "deletelobby",
        permission = "xg7lobby.command.lobby.delete",
        syntax = "xg7lobby deletelobby <id>",
        description = "Sets the lobby location",
        isAsync = true
)
public class DeleteLobby implements ICommand {


    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() == 0) {
            syntaxError(sender, "xg7lobby deletelobby <id>");
            return;
        }

        LobbyLocation location = XG7Lobby.getInstance().getLobbyManager().getLobby(args.get(0, String.class)).join();

        if (location == null) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "lobby.delete.id-not-found").thenAccept(text -> text.replace("id", args.get(0,String.class)).send(sender));
            return;
        }

        XG7Lobby.getInstance().getLobbyManager().getDao().deleteLobby(location)
                .exceptionally(e -> {
                    Text.fromLang(sender, XG7Lobby.getInstance(), "lobby.delete.on-error").thenAccept(text -> text.replace("error", e.getMessage()).send(sender));
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }).thenRun(() -> Text.fromLang(sender, XG7Lobby.getInstance(), "lobby.delete.on-success").thenAccept(text -> text.replace("id", location.getID()).send(sender)));

    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.BLAZE_ROD, this);
    }
}
