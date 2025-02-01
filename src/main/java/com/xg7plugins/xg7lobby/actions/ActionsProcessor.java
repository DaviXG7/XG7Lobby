package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ActionsProcessor {

    private final HashMap<String, List<Action>> actions = new HashMap<>();

    public void registerActions(String id, List<String> actions) {
        if (actions.isEmpty()) return;
        List<Action> actionList = new ArrayList<>();
        for (String line : actions) actionList.add(getActionOf(line));

        this.actions.put(id, actionList);
    }

    public void process(String id, Player player) {
        if (!actions.containsKey(id)) return;
        actions.get(id).forEach(action -> action.execute(player));
    }
    public void process(List<String> actions) {
        actions.forEach(action -> getActionOf(action).execute(null));
    }


    public Action getActionOf(String line) {
        String action = line.split(" ")[0];
        line = line.substring(action.length() + 1);

        ActionType type = ActionType.extractType(action);

        if (type == null) throw new ActionException(action, line);

        Pair<Condition,String> condition = Condition.extractCondition(line);

        if (condition != null) line = line.split("] ")[1];
        String[] args = line.split(", ");
        return new Action(type, condition, type.isNeedArgs() ? args : new String[]{line});
    }

}
