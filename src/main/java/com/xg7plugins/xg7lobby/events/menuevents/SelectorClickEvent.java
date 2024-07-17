package com.xg7plugins.xg7lobby.events.menuevents;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.events.MenuClickType;
import org.bukkit.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class SelectorClickEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.SELECTOR, "enabled");
    }

    @EventHandler
    public void onClick(com.xg7plugins.xg7menus.api.events.MenuClickEvent event) {
        if (
                !event.getClickType().equals(MenuClickType.LEFT_CLICK_BLOCK) &&
                        !event.getClickType().equals(MenuClickType.LEFT_CLICK_AIR) &&
                        !event.getClickType().equals(MenuClickType.RIGHT_CLICK_BLOCK) &&
                        !event.getClickType().equals(MenuClickType.RIGHT_CLICK_AIR)
        ) return;
        if (!event.getMenu().getId().equals("xg7lselector")) return;

        if (CacheManager.getSelectorCache().asMap().containsKey(event.getPlayer().getUniqueId())) {
            Text.send(Config.getString(ConfigType.MESSAGES, "selector-cooldown").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getSelectorCache().asMap().get(event.getPlayer().getUniqueId()) - System.currentTimeMillis()) + 1) + ""), event.getPlayer());
            return;
        }

        CacheManager.put(event.getPlayer().getUniqueId(), CacheType.SELECTOR_COOLDOWN, null);

        if (Config.getList(ConfigType.SELECTOR, "actions.slot-" + (event.getSlot() + 1)).isEmpty()) return;

        for (String action : Config.getList(ConfigType.SELECTOR, "actions.slot-" + (event.getSlot() + 1))) {
            if (action.startsWith("[SWAP]")) {
                action += ", selectorclickevent";
            }
            Action.execute(action, event.getPlayer());
        }


    }
}
