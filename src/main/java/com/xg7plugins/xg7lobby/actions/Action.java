package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.Pair;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class Action {

    private final ActionType actionType;
    private final String[] args;
    private final Pair<Condition, String> condition;

    public void execute(Player player) {
        if (condition != null) if (!condition.getFirst().apply(player, condition.getSecond())) return;
        actionType.execute(player, args);
    }

}
