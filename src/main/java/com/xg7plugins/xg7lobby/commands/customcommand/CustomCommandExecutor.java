package com.xg7plugins.xg7lobby.commands.customcommand;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.utils.text.Text;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class CustomCommandExecutor implements CommandExecutor {

    private final CustomCommandManager customCommandManager;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            Text.formatLang(XG7Plugins.getInstance(), commandSender, "commands.not-a-player").thenAccept(text -> text.send(commandSender));
            return true;
        }

        customCommandManager.getCommand(command.getName()).execute((Player) commandSender);

        return true;
    }
}
