package com.xg7network.xg7lobby.commands;

import com.xg7network.xg7lobby.config.PermissionType;
import com.xg7network.xg7lobby.utils.Text.TextUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class Command extends BukkitCommand {
    boolean isEnabled;

    protected Command(boolean enabled, PermissionType permission, String noPermMessage, String name, String description, String usageMessage, List<String> aliases) {
        super(name);

        this.setAliases(aliases);
        this.isEnabled = enabled;
        this.setDescription(description);
        this.setPermission(permission.getPerm());
        this.setPermissionMessage(TextUtil.)

    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
    public abstract void execute(CommandSender sender, String[] args);
}
