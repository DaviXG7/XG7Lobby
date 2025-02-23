package com.xg7plugins.xg7lobby.inventories.menu;

import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class LobbyItem {

    private Item item;
    private String path;
    private Pair<Condition, String> condition;
    private String otherItemPath;

}
