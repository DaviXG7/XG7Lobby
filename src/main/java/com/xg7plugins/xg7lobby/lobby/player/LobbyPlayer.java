package com.xg7plugins.xg7lobby.lobby.player;


import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Column;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Data;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Data
@Table(name = "player_data")
public class LobbyPlayer implements Entity {

    @Pkey
    private UUID playerUUID;
    @Column(name = "hiding_players")
    private boolean isPlayerHiding;
    @Column(name = "chat_hide")
    private boolean isChatHide;
    @Column(name = "muted")
    private boolean isMuted;
    @Column(name = "time_for_unmute")
    private long timeForUnmute;
    @Column(name = "build_enabled")
    private boolean isBuildEnabled;
    @Column(name = "fly_enabled")
    private boolean isFlying;
    @Column(name = "global_pvp_enabled")
    private boolean isGlobalPVPEnabled;
    private List<Warn> infractions;

    @Setter
    private transient boolean isFirstJoin;

    public boolean isFirstJoin() {
        return isFirstJoin;
    }

    private LobbyPlayer() {
        isFirstJoin = false;
    }

    public LobbyPlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.isFirstJoin = true;
    }

    public static CompletableFuture<LobbyPlayer> cast(UUID playerUUID, boolean loadAsync) {

        PlayerDAO dao = XG7Lobby.getInstance().getPlayerDAO();

        CompletableFuture<LobbyPlayer> future = dao.get(playerUUID).thenApply(playerData -> {
            if (playerData == null) {
                LobbyPlayer data = new LobbyPlayer(playerUUID);
                try {
                    dao.add(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return data;
            }
            return playerData;
        });

        return loadAsync ? future : CompletableFuture.completedFuture(future.join());

    }
    public OfflinePlayer getOfflinePlayer() {
        return XG7Lobby.getInstance().getServer().getOfflinePlayer(playerUUID);
    }
    public Player getPlayer() {
        return XG7Lobby.getInstance().getServer().getPlayer(playerUUID);
    }


    public void fly() {
        Player player = this.getPlayer();

        Config config = XG7Lobby.getInstance().getConfigsManager().getConfig("config");

        if (player == null) return;

        if (!XG7Lobby.getInstance().isInWorldEnabled(player.getPlayer())) return;

        player.setAllowFlight(
                isFlying || (
                        (
                                config.get("multi-jumps.enabled", Boolean.class).orElse(false) &&
                                player.hasPermission("xg7lobby.multi-jumps")
                        ) || (
                                player.getGameMode() == GameMode.CREATIVE ||
                                        player.getGameMode() == GameMode.SPECTATOR
                        )
                )
        );
        if (player.getAllowFlight()) player.setFlying(isFlying);

    }

}
