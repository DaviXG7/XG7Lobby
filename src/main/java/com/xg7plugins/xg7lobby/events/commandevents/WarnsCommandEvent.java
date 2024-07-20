package com.xg7plugins.xg7lobby.events.commandevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.events.MenuClickEvent;
import com.xg7plugins.xg7menus.api.menus.ItemPages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;

import java.util.List;

public class WarnsCommandEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.COMMANDS, "commands.xg7lobbywarns.enabled");
    }

    @EventHandler
    public void onMenuClick(MenuClickEvent event) {
        if (!event.getMenu().getId().equals("xg7lobby:warns")) return;

        if (event.getSlot() == Config.getInt(ConfigType.CONFIG, "warns-gui.go-next-page.slot") - 1) {
            ((ItemPages)event.getMenu()).nextPage(event.getPlayer());
            return;
        }
        if (event.getSlot() == Config.getInt(ConfigType.CONFIG, "warns-gui.go-back-page.slot") - 1) {
            ((ItemPages)event.getMenu()).previusPage(event.getPlayer());
            return;
        }

        if (event.getInventoryItem() == null) return;

        List<String> lore = event.getInventoryItem().getItemStack().getItemMeta().getLore();

        if (lore == null) return;

        if (lore.get(lore.size() - 1).contains("Click to copy the id!")) {
            TextComponent message = new TextComponent(com.xg7plugins.xg7lobby.utils.Text.getFormatedText(event.getPlayer(), "[CENTER] &aClick here to get the warn id!"));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, lore.get(lore.size() - 1).split("ID: ")[1]));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to get!").create()));
            event.getPlayer().sendMessage("");
            event.getPlayer().spigot().sendMessage(message);
            event.getPlayer().sendMessage("");
        }

    }

    public static void verify(PlayerData data) {
        if (Config.getInt(ConfigType.CONFIG, "warns-total-to-ban") > -1 && data.getInfractions().size() >= Config.getInt(ConfigType.CONFIG, "warns-total-to-ban")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(data.getId());
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-ban-message")), null, null);
            if (player.isOnline()) {
                Bukkit.getPlayer(data.getId()).kickPlayer(Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-ban-message")));
            }
            return;
        }
        if (Config.getInt(ConfigType.CONFIG, "min-warns-total-to-kick") > -1 && data.getInfractions().size() >= Config.getInt(ConfigType.CONFIG, "min-warns-total-to-kick")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(data.getId());
            if (player.isOnline()) {
                Bukkit.getPlayer(data.getId()).kickPlayer(Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-ban-message")));
            }
            return;
        }
        for (int level = 1; level <= 3; level++) {
            int minWarnsToKick = Config.getInt(ConfigType.CONFIG, "warns-level" + level + "-to-ban");
            if (minWarnsToKick > -1) {
                int finalLevel = level;
                long warnsCount = data.getInfractions().stream()
                        .filter(warn -> warn.getLevel() == finalLevel)
                        .count();
                if (warnsCount >= minWarnsToKick) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(data.getId());
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-ban-message")), null, null);
                    if (player.isOnline()) {
                        Bukkit.getPlayer(data.getId()).kickPlayer(Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-ban-message")));
                    }
                }
            }
        }
        for (int level = 1; level <= 3; level++) {
            int minWarnsToKick = Config.getInt(ConfigType.CONFIG, "min-warns-level" + level + "-to-kick");
            if (minWarnsToKick > -1) {
                int finalLevel = level;
                long warnsCount = data.getInfractions().stream()
                        .filter(warn -> warn.getLevel() == finalLevel)
                        .count();
                if (warnsCount >= minWarnsToKick) {
                    if (Bukkit.getOfflinePlayer(data.getId()).isOnline()) {
                        Bukkit.getPlayer(data.getId()).kickPlayer(Text.getFormatedText(Bukkit.getPlayer(data.getId()), Config.getString(ConfigType.MESSAGES, "moderation.warn-kick-message")));
                    }
                }
            }
        }
    }
}
