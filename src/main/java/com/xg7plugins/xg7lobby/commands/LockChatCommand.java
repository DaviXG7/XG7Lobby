package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(
        name = "lockchat",
        permission = "xg7lobby.command.lockchat",
        syntax = "/lockchat",
        description = "Lock the chat"
)
public class LockChatCommand implements ICommand {
    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        XG7Lobby.getInstance().getServerInfo().setChatLocked(!XG7Lobby.getInstance().getServerInfo().isChatLocked());

        Text.formatLang(XG7Lobby.getInstance(), sender, "chat.on-" + (XG7Lobby.getInstance().getServerInfo().isChatLocked() ? "lock" : "unlock")).thenAccept(text -> text.send(sender));
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.OAK_FENCE, this);
    }
}
