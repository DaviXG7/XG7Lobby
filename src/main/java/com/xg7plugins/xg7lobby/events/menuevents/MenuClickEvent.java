package com.xg7plugins.xg7lobby.events.menuevents;

import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.actions.Action;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;

public class MenuClickEvent implements Event {
    @Override
    public boolean isEnabled() {
        return !Config.getMenus().isEmpty();
    }

    @EventHandler
    public void onClick(com.xg7plugins.xg7menus.api.events.MenuClickEvent event) {
        FileConfiguration configuration = Config.getMenuFileById(event.getMenu().getId());

        if (configuration == null) return;
        if (configuration.getStringList("actions.slot-" + (event.getSlot() + 1)).isEmpty()) return;

        for (String action : configuration.getStringList("actions.slot-" + (event.getSlot() + 1))) {
            if (action.startsWith("[SWAP]")) {
                action += ", menuclickevent";
            }
            Action.execute(action, event.getPlayer());
        }
    }
}
