package com.xg7plugins.xg7lobby.commands.toggleCommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.command.CommandSender;

@Command(
        name = "lockchat",
        permission = "xg7lobby.command.lockchat",
        syntax = "/lockchat",
        description = "Lock the chat"
)
public class LockChatCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        XG7Lobby.getInstance().getServerInfo().setChatLocked(!XG7Lobby.getInstance().getServerInfo().isChatLocked());

        Text.fromLang(sender, XG7Lobby.getInstance(), "chat.on-" + (XG7Lobby.getInstance().getServerInfo().isChatLocked() ? "lock" : "unlock")).thenAccept(text -> text.send(sender));
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.OAK_FENCE, this);
    }
}
