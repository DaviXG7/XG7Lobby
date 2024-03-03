package com.xg7network.xg7lobby.Module.Inventories;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Module.Inventories.Actions.Action;
import com.xg7network.xg7lobby.Utils.CustomInventories.Config.ConfigSelectorInventoryItem;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.XG7Lobby;
import com.xg7network.xg7menus.API.Inventory.InvAndItems.Menus.PlayerSelector;
import com.xg7network.xg7menus.API.Inventory.SuperClasses.InventoryItem;
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

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class SelectorManager extends Module implements Listener {

    private static HashMap<UUID, PlayerSelector> playerSelector;


    public SelectorManager(XG7Lobby plugin) {
        super(plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (playerSelector == null) return;

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (Players.getPlayers().containsKey(player.getUniqueId())) {
                playerSelector.put(player.getUniqueId(), new PlayerSelector());
                playerSelector.get(player.getUniqueId()).setCancelEvents(true);

                for (String path : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("selectors.items").getKeys(false)) {
                    playerSelector.get(player.getUniqueId()).addItems(
                            ConfigSelectorInventoryItem.fromConfig(path, player)
                    );
                }

                for (int i = 0 ; i < playerSelector.get(player.getUniqueId()).getItems().size() ; i++) {
                    ((ConfigSelectorInventoryItem) playerSelector.get(player.getUniqueId()).getItems().get(i)).setCooldown(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown") * 1000);
                    ((ConfigSelectorInventoryItem) playerSelector.get(player.getUniqueId()).getItems().get(i)).setCooldownMessage(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown"));
                }


                playerSelector.get(player.getUniqueId()).open(player);

            }
        }, 15);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (playerSelector == null) return;

        Player player = event.getPlayer();
        playerSelector.get(player.getUniqueId()).removeItems(player);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {

        if (playerSelector == null || playerSelector.get(event.getPlayer().getUniqueId()) == null) return;

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Players.getPlayers().containsKey(player.getUniqueId())) {
                playerSelector.get(player.getUniqueId()).removeItems(player);
            } else {
                playerSelector.get(player.getUniqueId()).open(player);
            }

        }, 15);

    }

    @Override
    public void onEnable() {
        if (!configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.enabled")) return;

        playerSelector = new HashMap<>();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(p -> {
            if (Players.getPlayers().containsKey(p.getUniqueId())) {
                playerSelector.put(p.getUniqueId(), new PlayerSelector());
                playerSelector.get(p.getUniqueId()).setCancelEvents(true);

                for (String path : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("selectors.items").getKeys(false)) {
                    playerSelector.get(p.getUniqueId()).addItems(
                            ConfigSelectorInventoryItem.fromConfig(path, p)
                    );
                }

                for (int i = 0 ; i < playerSelector.get(p.getUniqueId()).getItems().size() ; i++) {
                    ((ConfigSelectorInventoryItem) playerSelector.get(p.getUniqueId()).getItems().get(i)).setCooldown(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown") * 1000);
                    ((ConfigSelectorInventoryItem) playerSelector.get(p.getUniqueId()).getItems().get(i)).setCooldownMessage(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown"));
                }

                playerSelector.get(p.getUniqueId()).open(p);

            }
        }), 15);


    }

    @Override
    public void onDisable() {

        if (playerSelector == null) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerSelector.get(player.getUniqueId()).removeItems(player);
        }

    }
}