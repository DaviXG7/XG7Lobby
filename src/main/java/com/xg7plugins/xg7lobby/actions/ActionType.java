package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public enum ActionType {

    MESSAGE((player, args) -> Text.format(String.join(" ", args), XG7Lobby.getInstance()).send(player)),
    COMMAND((player, args) -> player.performCommand(String.join(" ", args))),
    CONSOLE((player, args) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.join(" ", args))),
    TITLE((player, args) -> {
        if (args.length == 1) {
            player.sendTitle(Text.format(args[0], XG7Lobby.getInstance()).getWithPlaceholders(player), "");
            return;
        }
        if (args.length == 2) {
            player.sendTitle(Text.format(args[0], XG7Lobby.getInstance()).getWithPlaceholders(player), Text.format(args[1], XG7Lobby.getInstance()).getWithPlaceholders(player));
        }
    });




    private BiConsumer<Player, String[]> action;




}
