package com.xg7plugins.xg7lobby.actions;

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

}
