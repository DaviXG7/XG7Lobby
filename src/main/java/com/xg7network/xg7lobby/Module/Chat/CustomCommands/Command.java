package com.xg7network.xg7lobby.Module.Chat.CustomCommands;

import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.CustomInventories.Action.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public class Command extends BukkitCommand {

    private List<String> actions;
    protected Command(@NotNull String name, @NotNull String description, @NotNull List<String> aliases, String permission, List<String> actions) {
        super(name);

        this.actions = actions;

        this.setAliases(aliases);
        this.setDescription(description);
        if (permission != null && !permission.isEmpty()) this.setPermission(permission);
        this.setPermissionMessage(ErrorMessages.NO_PEMISSION.getMessage());

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(name, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }

        for (String action : actions) new Action((Player) commandSender, action).execute();


        return true;
    }
}
