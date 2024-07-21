package com.xg7plugins.xg7lobby.events.chatevents;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.data.player.model.Warn;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.Text;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntiSpamEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "anti-spam.enabled");
    }

    @Getter
    private static final HashMap<UUID, List<String>> spamRange = new HashMap<>();

    @EventHandler
    public void onAsyncChatEvent(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(PermissionType.SPAM.getPerm())) return;

        Player player = event.getPlayer();

        if (PlayerManager.getPlayerData(player.getUniqueId()).isMuted()) return;

        spamRange.putIfAbsent(player.getUniqueId(), new ArrayList<>());

        if (!spamRange.get(player.getUniqueId()).isEmpty()) {
            if (Config.getBoolean(ConfigType.CONFIG, "anti-spam.message-cannot-be-the-same") && spamRange.get(player.getUniqueId()).get(spamRange.get(player.getUniqueId()).size() - 1).equals(event.getMessage())) {
                event.setCancelled(true);
                Text.send(Config.getString(ConfigType.MESSAGES, "chat.on-same-message"), player);
                return;
            }
        }

        if (spamRange.get(player.getUniqueId()).size() > Config.getInt(ConfigType.CONFIG, "anti-spam.tolerance")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "chat.on-spam-limit"), player);
            event.setCancelled(true);

            if (Config.getBoolean(ConfigType.CONFIG, "anti-spam.mute-on-spam-limit")) {
                PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());
                data.setMuted(true);
                data.setTimeForUnmute(System.currentTimeMillis() + Text.convertToMilliseconds(Config.getString(ConfigType.CONFIG, "anti-spam.unmute-delay")));
                CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
                SQLHandler.update("UPDATE players SET ismuted = ?, timeforunmute = ? WHERE id = ?", data.isMuted(), data.getTimeForUnmute(), data.getId());

                if (Config.getBoolean(ConfigType.CONFIG, "anti-spam.warn-on-spam-limit")) {
                    int level = Config.getInt(ConfigType.CONFIG, "anti-spam.spam-warn-level");

                    if (level < 1 || level > 3) {
                        Log.severe(Text.translateColorCodes(Config.getString(ConfigType.MESSAGES, "moderation.warn-invalid-level").replace("[LEVEL]", level + "")));
                        return;
                    }

                    Warn warn = new Warn(level, Text.translateColorCodes(Config.getString(ConfigType.CONFIG, "anti-spam.warning")), System.currentTimeMillis(), UUID.randomUUID());

                    data.getInfractions().add(warn);

                    CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);

                    SQLHandler.update("INSERT INTO warns (playerid, level, warnid, warn, whenw) VALUES (?,?,?,?,?)", data.getId(), warn.getLevel(), warn.getId().toString(), warn.getWarn(), warn.getDate());
                }
            }
            return;
        }

        if (spamRange.get(player.getUniqueId()).size() >= Config.getInt(ConfigType.CONFIG, "anti-spam.warn-tolerance")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "chat.on-spam-tolerance"), player);
        }

        if (CacheManager.getSpamChache().asMap().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Text.send(Config.getString(ConfigType.MESSAGES, "chat.on-spam-time").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getSelectorCache().asMap().get(event.getPlayer().getUniqueId()) - System.currentTimeMillis()) + 1) + ""), event.getPlayer());
            return;
        }

        CacheManager.put(event.getPlayer().getUniqueId(), CacheType.ANTI_SPAM, null);

        spamRange.get(player.getUniqueId()).add(event.getMessage());

    }
}
