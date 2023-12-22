package com.xg7network.xg7lobby.Module.Chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Selectors.SelectorManager;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class AntiSpam extends Module implements Listener {

    private static final FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);

    private final Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"), TimeUnit.SECONDS).build();

    private static List<TempPlayer> players = new ArrayList<>();

    private static HashMap<Player, String> lastMessage = new HashMap<>();

    public AntiSpam(XG7Lobby plugin) {
        super(plugin);
    }

    boolean contains(Player player) {

        for (TempPlayer tempPlayer : players) {
            if (tempPlayer.getPlayer().equals(player)) return true;
        }

        return false;

    }

    TempPlayer get(Player player) {
        for (TempPlayer tempPlayer : players) {
            if (tempPlayer.getPlayer().equals(player)) return tempPlayer;
        }
        return null;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player playerevent = event.getPlayer();
        if (playerevent.hasPermission(PermissionType.CHAT.getPerm())) return;

        if (!lastMessage.containsKey(playerevent)) {
            lastMessage.put(playerevent, "");
        }

        if (!contains(playerevent)) {
            players.add(new TempPlayer(playerevent));
        }

        TempPlayer player = get(playerevent);

        if (config.getBoolean("anti-spam.enabled")) {

            if (!PlayersManager.getData(player.getPlayer().getUniqueId().toString()).isMuted() && !configManager.getConfig(ConfigType.DATA).getBoolean("chat-locked")) {

                if (this.cooldown.asMap().containsKey(player.getPlayer().getUniqueId())) {
                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.message-cooldown").replace("SECONDS", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown.asMap().get(player.getPlayer().getUniqueId()) - System.currentTimeMillis())))).send(player.getPlayer());
                } else {
                    if (configManager.getConfig(ConfigType.SELECTORS).getLong("anti-spam.cooldown") > 0)
                        this.cooldown.put(player.getPlayer().getUniqueId(), System.currentTimeMillis() + configManager.getConfig(ConfigType.SELECTORS).getLong("anti-spam.cooldown") * 1000L);
                    if (lastMessage.get(playerevent).equals(event.getMessage())) {
                        event.setCancelled(true);
                        new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.repeated-message")).send(player.getPlayer());
                    } else {
                        lastMessage.put(playerevent, event.getMessage());
                    }


                    if (player.getSpam().size() >= config.getInt("anti-spam.warn-tolerance"))
                        new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.quick-messages")).send(player.getPlayer());


                    if (player.getSpam().size() > config.getInt("anti-spam.tolerance")) {

                        event.setCancelled(true);

                        player.getSpam().clear();


                        if (config.getBoolean("anti-spam.mute-on-spam-limit")) {
                            PlayerData data = PlayersManager.getData(player.getPlayer().getUniqueId().toString());

                            data.setMuted(true);

                            if (config.getBoolean("infraction-on-mute"))
                                data.addInfraction("Muted for spamming", System.currentTimeMillis());

                            String time = config.getString("anti-spam.unmute-delay");

                            if (!time.equalsIgnoreCase("Indeterminate")) {

                                if (time.contains("s")) {

                                    time = time.replace("s", "");

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.SECOND, Integer.parseInt(time));
                                    data.setLastDayToUnmute(calendar.getTime().getTime());

                                    new TextUtil(prefix + "&cYou got muted by spam for " + TimeUnit.MILLISECONDS.toSeconds((data.getLastDayToUnmute() - new Date().getTime())) + " seconds").send(player.getPlayer());

                                } else if (time.contains("min")) {

                                    time = time.replace("min", "");

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.MINUTE, Integer.parseInt(time));
                                    data.setLastDayToUnmute(calendar.getTime().getTime());

                                    new TextUtil(prefix + "&cYou got muted by spam for " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) + " minutes").send(player.getPlayer());

                                } else if (time.contains("h")) {

                                    time = time.replace("h", "");

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.HOUR, Integer.parseInt(time));
                                    data.setLastDayToUnmute(calendar.getTime().getTime());

                                    new TextUtil(prefix + "&cYou got muted by spam for " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) + " hours!").send(player.getPlayer());

                                } else if (time.contains("d")) {

                                    time = time.replace("d", "");

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.HOUR, Integer.parseInt(time) * 24);
                                    data.setLastDayToUnmute(calendar.getTime().getTime());

                                    new TextUtil(prefix + "&cYou got muted by spam for " + TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days!").send(player.getPlayer());

                                } else if (time.contains("mo")) {

                                    time = time.replace("mo", "");
                                    Date lastDay = new Date();
                                    lastDay.setMonth(new Date().getMonth() + Integer.parseInt(time));
                                    data.setLastDayToUnmute(lastDay.getTime());

                                    new TextUtil(prefix + "&cYou got muted by spam for " + TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " months!").send(player.getPlayer());

                                }

                                PlayersManager.update(data.getId(), data);

                            } else {
                                new TextUtil(prefix + "&cYou got muted by spam for undetermined time").send(player.getPlayer());
                            }

                        }

                        if (config.getBoolean("anti-spam.infraction-on-spam-limit")) {

                            PlayerData data = PlayersManager.getData(player.getPlayer().getUniqueId().toString());

                            data.addInfraction("Warned for spam", System.currentTimeMillis());

                            PlayersManager.update(data.getId(), data);

                            new TextUtil(prefix + "&cYou received a warning for spamming!").send(player.getPlayer());

                        }


                    } else {
                        player.getSpam().add(event.getMessage());
                    }
                }
            }

        }

    }


    @Override
    public void onEnable() {

        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) get(player).getSpam().remove(0);
        }, 0, config.getLong("anti-spam.spam-cooldown") * 20);

    }

    @Override
    public void onDisable() {

    }
}
