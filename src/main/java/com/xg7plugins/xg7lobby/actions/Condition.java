package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.Pair;
import com.xg7plugins.utils.Parser;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
public enum Condition {

    IF((player, condition) -> Parser.BOOLEAN.convert(Text.format(condition, XG7Lobby.getInstance()).getWithPlaceholders(player))),
    IF_NOT((player, condition) -> !((boolean)Parser.BOOLEAN.convert(Text.format(condition, XG7Lobby.getInstance()).getWithPlaceholders(player)))),
    PERMISSION(Permissible::hasPermission),
    NO_PERMISSION((player, perm) -> !player.hasPermission(perm));

    private final BiFunction<Player, String, Boolean> condition;

    public boolean apply(Player player, String value) {
        return condition.apply(player, value);
    }

    private static final Pattern conditionPattern = Pattern.compile("\\[(.*?): (.*?)\\]");

    public static Pair<Condition, String> extractCondition(String condition) {
        Matcher matcher = conditionPattern.matcher(condition);
        if (matcher.find()) {
            String type = matcher.group(1);
            for (Condition value : values()) {
                if (value.name().equalsIgnoreCase(type)) {
                    return new Pair<>(value, matcher.group(2));
                }
            }
        }
        return null;
    }

}
