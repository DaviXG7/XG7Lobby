package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.tasks.TaskManager;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class AntiSpam implements Listener {

    private final HashMap<String, Integer> tolerance = new HashMap<>();
    private final Config config = XG7Lobby.getInstance().getConfig("config");

    private final HashMap<UUID, String> lastMessages = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return config.get("anti-spam.enabled", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        System.out.println("Player: " + player.getName() + " sent a message: " + event.getMessage());

        if (player.hasPermission("xg7lobby.chat.spam")) {
            System.out.println("Player has spam permission, skipping anti-spam checks.");
            return;
        }

        if (config.get("anti-spam.anti-spam-only-on-lobby", Boolean.class).orElse(false)) {
            if (!XG7Lobby.getInstance().isInWorldEnabled(player)) {
                System.out.println("Anti-spam only on lobby is enabled, but player is not in the enabled world.");
                return;
            }
        }

        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("lobby-chat-spam", player)) {
            double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("lobby-chat-spam", player);
            System.out.println("Player is on cooldown for spam. Remaining time: " + cooldownToToggle);
            Text.formatLang(XG7Lobby.getInstance(), player, "chat.message-cooldown").thenAccept(text -> text
                    .replace("[PLAYER]", player.getName())
                    .replace("[MILLISECONDS]", String.valueOf((cooldownToToggle)))
                    .replace("[SECONDS]", String.valueOf((int) ((cooldownToToggle) / 1000)))
                    .replace("[MINUTES]", String.valueOf((int) ((cooldownToToggle) / 60000)))
                    .replace("[HOURS]", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                    .send(player));
            event.setCancelled(true);
            return;
        }

        if (lastMessages.containsKey(player.getUniqueId())) {
            if (config.get("anti-spam.message-cannot-be-the-same", Boolean.class).orElse(true)) {
                String lastMessage = this.lastMessages.get(player.getUniqueId());
                if (lastMessage != null && lastMessage.equalsIgnoreCase(event.getMessage())) {
                    System.out.println("Player sent the same message as last time.");
                    Text.formatLang(XG7Lobby.getInstance(), player, "chat.same-message").thenAccept(text -> text.send(player));
                    event.setCancelled(true);
                    return;
                }
                lastMessages.put(player.getUniqueId(), event.getMessage());
            }
        }

        XG7Plugins.getInstance().getCooldownManager().addCooldown(player, "lobby-chat-spam", config.getTime("anti-spam.cooldown").orElse(500L));
        System.out.println("Added cooldown for player.");

        if (config.get("anti-spam.spam-tolerance", Integer.class).orElse(0) > 0) {
            tolerance.putIfAbsent(player.getName(), 0);
            tolerance.put(player.getName(), tolerance.get(player.getName()) + 1);
            System.out.println("Increased spam tolerance for player. Current tolerance: " + tolerance.get(player.getName()));

            Bukkit.getScheduler().runTaskLater(XG7Lobby.getInstance(), () -> {
                tolerance.put(player.getName(), tolerance.get(player.getName()) - 1);
                if (tolerance.get(player.getName()) == 0) {
                    tolerance.remove(player.getName());
                }
                System.out.println("Decreased spam tolerance for player. Current tolerance: " + tolerance.get(player.getName()));
            }, TaskManager.convertMillisToTicks(config.getTime("anti-spam.time-for-decrement-spam-tolerance").orElse(5000L)));

            if (tolerance.get(player.getName()) >= config.get("anti-spam.spam-tolerance", Integer.class).orElse(0)) {
                System.out.println("Player exceeded spam tolerance limit.");
                event.setCancelled(true);
                LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
                lobbyPlayer.addInfraction(new Warn(lobbyPlayer.getPlayerUUID(), config.get("anti-spam.spam-warn-level", Integer.class).orElse(0), "Spamming"));

                if (config.get("anti-spam.mute-on-spam-limit", Boolean.class).orElse(false)) {
                    lobbyPlayer.setMuted(true);
                    if (config.getTime("anti-spam.unmute-delay").orElse(0L) != 0) {
                        lobbyPlayer.setTimeForUnmute(System.currentTimeMillis() + config.getTime("anti-spam.unmute-delay").orElse(50000L));
                    }
                    System.out.println("Player has been muted for spamming.");
                }
                return;
            }

            if (tolerance.get(player.getName()) >= config.get("anti-spam.send-warning-on-message", Integer.class).orElse(0)) {
                System.out.println("Player is nearing spam tolerance limit.");
                Text.formatLang(XG7Lobby.getInstance(), player, "chat.send-much-messages").thenAccept(text -> text.send(player));
            }
        }
    }


}
