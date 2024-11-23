package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.Parser;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
public enum Conditions {

    IF((player, condition) -> Parser.BOOLEAN.convert(Text.format(condition, XG7Lobby.getInstance()).getWithPlaceholders(player))),
    IF_NOT((player, condition) -> !((boolean)Parser.BOOLEAN.convert(Text.format(condition, XG7Lobby.getInstance()).getWithPlaceholders(player)))),
    PERMISSION(Permissible::hasPermission),
    NO_PERMISSION((player, perm) -> !player.hasPermission(perm));

    private BiFunction<Player, String, Boolean> condition;

}
