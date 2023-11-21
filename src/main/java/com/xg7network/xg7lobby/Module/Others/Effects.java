package com.xg7network.xg7lobby.Module.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Effects extends Module {

    private HashMap<UUID, Player> players = Players.getPlayers();

    private List<String> Stringeffects = configManager.getConfig(ConfigType.CONFIG).getStringList("effects");

    private List<PotionEffect> effects = new ArrayList<>();

    public Effects(XG7Lobby plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {

        for (String s : Stringeffects) effects.add(PluginUtil.getEffect(s));

        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (Players.getPlayers().containsKey(player.getUniqueId()))
                    for (PotionEffect effect : effects)
                        players.get(player.getUniqueId()).addPotionEffect(effect, false);
                else
                    for (PotionEffect effect :  effects)
                        if (player.getActivePotionEffects().contains(effect))
                            player.getActivePotionEffects().remove(effect);

            }


        }, 0, 5);



    }

    @Override
    public void onDisable() {

    }

}
