package com.xg7network.xg7lobby.DefautCommands.Others.Warns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class WarnsGUIManager implements Listener {

    private static List<WarnsGUI> warnsGUIS = new ArrayList<>();

    public static void load() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            warnsGUIS.add(new WarnsGUI(player));
        }

    }

    public static WarnsGUI get(Player player) {

        for (WarnsGUI warnsGUI : warnsGUIS) {
            if (warnsGUI.getPlayer().equals(player)) {
                return warnsGUI;
            }
        }

        return null;


    }

    public static void update() {
        warnsGUIS.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            warnsGUIS.add(new WarnsGUI(player));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        warnsGUIS.add(new WarnsGUI(event.getPlayer()));
    }


}
