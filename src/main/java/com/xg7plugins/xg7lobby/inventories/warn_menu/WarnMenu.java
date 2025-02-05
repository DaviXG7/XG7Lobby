package com.xg7plugins.xg7lobby.inventories.warn_menu;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.Slot;
import com.xg7plugins.libs.xg7menus.events.ClickEvent;
import com.xg7plugins.libs.xg7menus.events.MenuEvent;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.gui.PageMenu;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.utils.text.TextCentralizer;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class WarnMenu extends PageMenu {
    public WarnMenu() {
        super(XG7Lobby.getInstance(), "warn-menu", "lang:[warn-menu.title]", 54, Slot.of(2,2), Slot.of(5,8));
    }

    public List<Item> pagedItems(Player player, Player target) {

        Config config = XG7Lobby.getInstance().getConfig("config");

        List<Map> warnMenu = config.getList("warn-levels", Map.class).orElse(null);


        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        List<Item> items = new ArrayList<>();

        lobbyPlayer.getInfractions().forEach(infraction -> {

            Item item = Item.from((String) warnMenu.stream().filter(map -> map.get("level").equals(infraction.getLevel())).findFirst().orElse(null).get("menu-material"));

            item.name("lang:[warn-menu.warn-item.name]");

            List<String> lore = new ArrayList<>();

            lore.add("lang:[warn-menu.warn-item.reason]");
            lore.add("lang:[warn-menu.warn-item.date]");
            lore.add("lang:[warn-menu.warn-item.level]");
            lore.add("lang:[warn-menu.warn-item.click-to-copy-id]");

            item.lore(lore);

            item.setBuildPlaceholders(new HashMap<String,String>() {{
                put("[TARGET]", target.getName());
                put("[REASON]", infraction.getReason());
                put("[DATE]", new SimpleDateFormat("dd/MM/yy HH:mm").format(infraction.getDate()));
                put("[LEVEL]", String.valueOf(infraction.getLevel()));
                put("[ID]", String.valueOf(infraction.getId()));
            }});

            item.setNBTTag("warn-id", String.valueOf(infraction.getId()));

            items.add(item);
        });

        return items;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {

        return Arrays.asList(
                Item.from(Material.ARROW).name("lang:[warn-menu.go-back]").slot(45),
                Item.from(Material.BARRIER).name("lang:[warn-menu.close]").slot(49),
                Item.from(Material.ARROW).name("lang:[warn-menu.go-next]").slot(53)
        );
    }

    public void open(Player player, Player target) {
        WarnMenuHolder holder = new WarnMenuHolder(this.id, this.plugin, (Text.detectLangOrText(XG7Lobby.getInstance(), player, this.title).join().replace("[TARGET]", target.getName())).getTextCentralized(TextCentralizer.PixelsSize.INV), this.size, this.type, this, player, target);
        player.openInventory(holder.getInventory());
        this.putItems(player, holder);
        holder.goPage(0);
    }


    @Override
    public List<Item> pagedItems(Player player) {
        return Collections.emptyList();
    }

    public <T extends MenuEvent> void onClick(T event) {
        event.setCancelled(true);
        if (!(event instanceof ClickEvent)) return;
        ClickEvent clickEvent = (ClickEvent) event;

        Player player = (Player) clickEvent.getWhoClicked();

        WarnMenuHolder holder = (WarnMenuHolder) clickEvent.getInventoryHolder();

        if (clickEvent.getClickedItem().isAir()) return;

        switch (clickEvent.getClickedSlot()) {
            case 45:
                holder.previousPage();
                break;
            case 49:
                player.closeInventory();
                break;
            case 53:
                holder.nextPage();
                break;
            default:

                String id = clickEvent.getClickedItem().getTag("warn-id", String.class).orElse(null);

                Text.format(" ").send(player);

                String message = Text.formatLang(XG7Lobby.getInstance(), player, "warn-menu.id-message").join().replace("[ID]", id).getText();

                TextComponent textComponent = new TextComponent(message);
                textComponent.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, id));
                player.spigot().sendMessage(textComponent);

                Text.format(" ").send(player);

        }

    }

}
