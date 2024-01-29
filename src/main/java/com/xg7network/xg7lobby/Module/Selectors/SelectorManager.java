package com.xg7network.xg7lobby.Module.Selectors;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Utils.CustomInventories.Action.Action;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.XG7Lobby;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SelectorManager extends Module implements Listener {

    private static HashMap <UUID, Selector> players = new HashMap<>();

    public SelectorManager(XG7Lobby plugin) {
        super(plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (Players.getPlayers().containsKey(player.getUniqueId())) {
                players.put(player.getUniqueId(), new Selector(player));
                players.get(player.getUniqueId()).giveItems();

            }
        }, 15);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (players.containsKey(player.getUniqueId())) {
            players.get(player.getUniqueId()).removeItems();
            players.remove(player.getUniqueId());
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) {
                players.get(player.getUniqueId()).removeItems();
                players.remove(player.getUniqueId());

            } else {
                if (!players.containsKey(player.getUniqueId())) {
                    players.put(player.getUniqueId(), new Selector(player));
                    if (!player.hasPermission(PermissionType.INV.getPerm())) {
                        if (players.containsKey(player.getUniqueId())) {
                            players.get(player.getUniqueId()).removeItems();
                        }
                    }
                }
            }

        }, 15);

    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(p -> {
            if (Players.getPlayers().containsKey(p.getUniqueId())) players.put(p.getUniqueId(), new Selector(p));
            if (players.containsKey(p.getUniqueId())) {
                players.get(p.getUniqueId()).giveItems();


            }
        }), 15);


    }

    @Override
    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (players.containsKey(player.getUniqueId())) {
                players.get(player.getUniqueId()).removeItems();
                players.remove(player.getUniqueId());
            }
        }

    }


    public static void execute(Player player, ItemStack stack) {

        if (stack != null) {

            if (players.containsKey(player.getUniqueId())) {
                for (SelectorItem item : players.get(player.getUniqueId()).getItems())
                    if (Objects.equals(item.getId(), new NBTItem(stack).getString("xg7lid"))) {

                        for (String ac : item.getActions()) {
                            Action action = new Action(player, ac);
                            action.execute();
                        }
                    }
            }
        }

    }

    public static boolean containsItemInHand(Player player) {
        if (player.getItemInHand() != null)
            if (players.containsKey(player.getUniqueId())) {
                for (SelectorItem item : players.get(player.getUniqueId()).getItems()) {
                    if (Objects.equals(item.getId(), new NBTItem(player.getItemInHand()).getString("xg7lid"))) return true;
                }
            }



        return false;
    }



    public static Selector getSelector(Player player) {
        return players.get(player.getUniqueId());
    }
}
