package com.xg7plugins.xg7lobby.pvp;


import com.xg7plugins.data.config.Config;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GlobalPVPManager {

    @Getter
    private final HashMap<UUID, LobbyPlayer> inPVP = new HashMap<>();
    private final Config config;
    @Getter
    private LobbySelector menu;

    public GlobalPVPManager(Config config) {
        this.config = config;
        this.menu = (LobbySelector) XG7Lobby.getInstance().getInventoryManager().getInventory(config.get("main-pvp-selector-id", String.class).orElse(null));
    }

    public void addPlayerToPVP(Player player) {

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(),false).join();

        boolean alreadyInPVP = inPVP.containsKey(player.getUniqueId());

        inPVP.put(player.getUniqueId(), lobbyPlayer);

        player.setMaxHealth(20);

        player.setAllowFlight(false);
        player.setFlying(false);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        LobbySelector mainMenu = (LobbySelector) XG7Lobby.getInstance().getInventoryManager().getInventory(Config.mainConfigOf(XG7Lobby.getInstance()).get("main-selector-id", String.class).orElse(null));

        if (mainMenu != null) {
            mainMenu.close(player);
        }

        if (config.get("global-pvp.on-enter-pvp.heal", Boolean.class).orElse(false)) player.setHealth(20);
        if (config.get("global-pvp.clear-inventory", Boolean.class).orElse(false)) player.getInventory().clear();

        Bukkit.getOnlinePlayers().forEach(p -> {
            player.showPlayer(player);
            if (inPVP.containsKey(p.getUniqueId())) p.showPlayer(player);
            if (config.get("global-pvp.hide-players-not-in-pvp", Boolean.class).orElse(true) && !inPVP.containsKey(p.getUniqueId())) player.hidePlayer(p);
        });

        XG7Lobby.getInstance().getActionsProcessor().process("on-pvp-enter", player);

        if (!alreadyInPVP) Text.fromLang(player,XG7Lobby.getInstance(), "pvp.on-enter").join().send(player);

    }

    public void removePlayerFromPVP(Player player) {

        inPVP.remove(player.getUniqueId());

        if (menu != null) menu.close(player);

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (config.get("global-pvp.hide-players-not-in-pvp", Boolean.class).orElse(true) && inPVP.containsKey(p.getUniqueId())) p.hidePlayer(player);
        });

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.fly();

        lobbyPlayer.setPlayerHiding(lobbyPlayer.isPlayerHiding());

        LobbySelector menu =(LobbySelector) XG7Lobby.getInstance().getInventoryManager().getInventory(Config.mainConfigOf(XG7Lobby.getInstance()).get("main-selector-id", String.class).orElse(null));

        if (menu != null) menu.open(player);

        player.setMaxHealth(config.get("hearts", Double.class).orElse(10D) * 2);
        player.setFoodLevel(config.get("hunger", Integer.class).orElse(10) * 2);
        if (config.get("on-join.heal", Boolean.class).orElse(true)) player.setHealth(player.getMaxHealth());
        if (config.get("on-join.clear-inventory", Boolean.class).orElse(true)) player.getInventory().clear();

        XG7Lobby.getInstance().getActionsProcessor().process("on-pvp-leave", player);


    }

    public boolean isPlayerInPVP(Player player) {
        return inPVP.containsKey(player.getUniqueId());
    }

}
