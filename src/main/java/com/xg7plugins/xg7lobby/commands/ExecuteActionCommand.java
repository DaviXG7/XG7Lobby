package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.actions.ActionType;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "execute",
        permission = "xg7lobby.command.execute",
        description = "Execute an action",
        syntax = "/execute \"[ACTIONNAME] [args...]\"",
        isPlayerOnly = true
)

public class ExecuteActionCommand implements ICommand {
    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() == 0) {
            syntaxError(sender, "/execute \"[ACTIONNAME] [args...]\"");
            return;
        }

        String actionArgs = Strings.join(Arrays.asList(args.getArgs()), ' ');

        XG7Lobby.getInstance().getActionsProcessor().process(Collections.singletonList(actionArgs), (Player) sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return args.len() == 1 ? Arrays.stream(ActionType.values()).map(type -> "[" + type.name() + "] ").collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.matchXMaterial("COMMAND_BLOCK").orElse(XMaterial.CRAFTING_TABLE), this);
    }
}
