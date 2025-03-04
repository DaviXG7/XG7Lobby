package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.Pair;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Arrays;

@AllArgsConstructor
public class Action {

    private final ActionType actionType;
    private Pair<Condition, String> condition;
    private final String[] args;

    public void execute(Player player) {
        if (condition != null && !condition.getFirst().apply(new Condition.ConditionPack(player,condition.getSecond()))) return;
        actionType.execute(player, args);
    }

    public static Item actionItem(ActionType type) {
        Item item = Item.from(type.getIcon());

        item.name("lang:[help.menu.actions-menu.action-item.name]");
        item.lore(Arrays.asList("lang:[help.menu.actions-menu.action-item.lore.description]", "lang:[help.menu.actions-menu.action-item.lore.usage]"));

        item.setBuildPlaceholders(
                Pair.of("id", type.name().toLowerCase()),
                Pair.of("description", type.getDescription()),
                Pair.of("usage", type.getUsage())
        );

        return item;

    }



}
