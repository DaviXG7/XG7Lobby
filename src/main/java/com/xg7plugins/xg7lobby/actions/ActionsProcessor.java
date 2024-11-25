package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.utils.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionsProcessor {

    private final HashMap<String, List<Action>> actions = new HashMap<>();

    public void registerActions(String id, List<String> actions) {
        if (actions.isEmpty()) return;
        List<Action> actionList = new ArrayList<>();
        for (String line : actions) {

            String action = line.split(" ")[0];
            line = line.substring(action.length() + 1);

            ActionType type = ActionType.extractType(action);

            if (type == null) throw new ActionException(action, line);

            String cond = line.split(" ").length == 0 ? "" : line.split(" ")[0];
            Pair<Condition,String> condition = Condition.extractCondition(cond);

            if (condition != null) line = line.substring(cond.isEmpty() ? 0 : cond.length() + 1);

            String[] args = line.split(", ");

            actionList.add(new Action(type, type.isNeedArgs() ? args : new String[]{line}, condition));
        }
        this.actions.put(id, actionList);
    }

    public void process(String id, Player player) {
        if (!actions.containsKey(id)) return;
        actions.get(id).forEach(action -> action.execute(player));
    }



}
