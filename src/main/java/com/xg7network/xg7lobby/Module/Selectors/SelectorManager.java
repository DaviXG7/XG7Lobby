package com.xg7network.xg7lobby.Module.Selectors;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Utils.Action.Action;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.XG7Lobby;
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

public class SelectorManager extends Module implements Listener {

    private static HashMap <Player, Selector> players = new HashMap<>();

    public SelectorManager(XG7Lobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (Players.getPlayers().containsKey(player.getUniqueId())) {
                if (player.hasPermission(PermissionType.INV.getPerm())) {
                    Selector selector = new Selector(player);
                    selector.giveItems();
                }
            }
        }, 15);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Selector selector = new Selector(player);
        selector.removeItems();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) players.remove(player);
        }, 15);

    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (Players.getPlayers().containsKey(p.getUniqueId())) players.put(p, new Selector(p));
                if (players.containsKey(p)) {
                    if (!p.hasPermission(PermissionType.INV.getPerm())) {
                        Selector selector = new Selector(p);
                        selector.giveItems();
                    }

                }
            });
        }, 0, 5);


    }

    @Override
    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            Selector selector = new Selector(player);
            selector.removeItems();
        }

    }


    public static void execute(Player player, ItemStack stack) {

        if (stack != null) {

            if (players.containsKey(player)) {
                for (SelectorItem item : players.get(player).getItems())
                    if (item.getItemStack().isSimilar(stack)) {

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
            if (players.containsKey(player))
                for (SelectorItem item : players.get(player).getItems())
                    if (item.getItemStack().isSimilar(player.getItemInHand())) return true;



        return false;
    }
}
