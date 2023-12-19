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

public class AntiSpam extends Module implements Listener {

    private static final FileConfiguration config = configManager.getConfig(ConfigType.CONFIG);

    private final Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"), TimeUnit.SECONDS).build();

    private static List<TempPlayer> players = new ArrayList<>();

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

        if (!contains(playerevent)) {
            players.add(new TempPlayer(playerevent));
        }

        TempPlayer player = get(playerevent);

        if (config.getBoolean("anti-spam.enabled")) {

            if (this.cooldown.asMap().containsKey(player.getPlayer().getUniqueId())) {
                new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.message-cooldown").replace("SECONDS", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown.asMap().get(player.getPlayer().getUniqueId()) - System.currentTimeMillis())))).send(player.getPlayer());
            } else {
                this.cooldown.put(player.getPlayer().getUniqueId(), System.currentTimeMillis() + configManager.getConfig(ConfigType.SELECTORS).getLong("anti-spam.cooldown") * 1000L);

                if (player.getLastMessage().equals(event.getMessage())) {
                    event.setCancelled(true);
                    new TextUtil(configManager.getConfig(ConfigType.MESSAGES).getString("events.repeated-message")).send(player.getPlayer());
                }

                if (player.getSpam().size() > config.getInt("anti-spam.tolerance")) {

                    event.setCancelled(true);

                    player.getSpam().clear();

                    if (config.getBoolean("anti-spam.mute-on-spam-limit")) {
                        PlayerData data = PlayersManager.getData(player.getPlayer().getUniqueId().toString());

                        data.setMuted(true);

                        String time = config.getString("anti-spam.unmute-delay");

                        if (!time.equalsIgnoreCase("Indeterminate")) {

                            if (time.contains("s")) {

                                time = time.replace("s" , "");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, Integer.parseInt(time));
                                data.setLastDayToUnmute(calendar.getTime());

                            } else if (time.contains("min")) {

                                time = time.replace("min" , "");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.MINUTE, Integer.parseInt(time));
                                data.setLastDayToUnmute(calendar.getTime());

                            } else if (time.contains("h")) {

                                time = time.replace("h" , "");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.HOUR, Integer.parseInt(time));
                                data.setLastDayToUnmute(calendar.getTime());

                            } else if (time.contains("d")) {

                                time = time.replace("d" , "");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.HOUR, Integer.parseInt(time) * 24);
                                data.setLastDayToUnmute(calendar.getTime());

                            } else if (time.contains("mo")) {

                                time = time.replace("mo" , "");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.HOUR, Integer.parseInt(time) * 720);
                                data.setLastDayToUnmute(calendar.getTime());

                            }




                        }

                    }

                    if (config.getBoolean("anti-spam.infraction-on-spam-limit")) {

                        PlayerData data = PlayersManager.getData(player.getPlayer().getUniqueId().toString());

                        data.addInfraction("Muted for spam", new Date());

                    }


                }
            }

        }

    }


    @Override
    public void onEnable() {

        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) get(player).getSpam().clear();
        }, 0, config.getInt("anti-spam.spam-cooldown") * 20);

    }

    @Override
    public void onDisable() {

    }
}
