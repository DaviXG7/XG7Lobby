package com.xg7plugins.xg7lobby.commands.customcommand;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomCommand {

    private final String name;
    private final String syntax;
    private final List<String> aliases;
    private final String description;
    private final String permission;
    private final List<String> actions;

    public CustomCommand(Config config, String path) {
        this.name = config.get("custom-commands." + path + ".name", String.class).orElse("No name");
        this.syntax = config.get("custom-commands." + path + ".syntax", String.class).orElse("No syntax");
        this.description = config.get("custom-commands." + path + ".description", String.class).orElse("No description");
        this.permission = config.get("custom-commands." + path + ".permission", String.class).orElse("");
        this.aliases = config.getList("custom-commands." + path + ".aliases", String.class).orElse(new ArrayList<>());
        this.actions = config.getList("custom-commands." + path + ".actions", String.class).orElse(new ArrayList<>());
    }

    public void execute(Player player) {
        XG7Lobby.getInstance().getActionsProcessor().process(actions, player);
    }
}
