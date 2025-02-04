package com.xg7plugins.xg7lobby.inventories.warn_menu;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.gui.PageMenu;
import com.xg7plugins.libs.xg7menus.menus.holders.PageMenuHolder;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.utils.text.TextCentralizer;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WarnMenu extends PageMenu {
    public WarnMenu() {
        super(XG7Lobby.getInstance(), "warn-menu", "lang:[warn-menu.title]", 54, pos1, pos2);
    }

    public List<Item> pagedItems(Player player, Player target) {

        Config config = XG7Lobby.getInstance().getConfig("config");

        List<Map> warnMenu = config.getList("warn-levels", Map.class).orElse(null);


        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        List<Item> items = new ArrayList<>();

        lobbyPlayer.getInfractions().forEach(infraction -> {

            Item item = Item.from((String) warnMenu.stream().filter(map -> map.get("level").equals(infraction.getLevel())).findFirst().orElse(null).get("menu-material"));

            item.name(Text.formatLang(XG7Lobby.getInstance(), player, "lang:[warn-menu.warn-item.name]").join().replace("[TARGET]", target.getName()).getText());

            items.add(item);
        });

        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {
        return Collections.emptyList();
    }

    public void open(Player player, Player target) {
        PageMenuHolder holder = new PageMenuHolder(this.id, this.plugin, (Text.detectLangOrText(XG7Plugins.getInstance(), player, this.title).join().replace("[TARGET]", target.getName())).getTextCentralized(TextCentralizer.PixelsSize.INV), this.size, this.type, this, target);
        player.openInventory(holder.getInventory());
        this.putItems(player, holder);
        holder.goPage(0);
    }


    @Override
    public List<Item> pagedItems(Player player) {
        return Collections.emptyList();
    }
}
