package com.xg7plugins.xg7lobby.lobby.player;


import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Column;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.entity.Pkey;
import com.xg7plugins.data.database.entity.Table;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Data;
import lombok.Setter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Data
@Table(name = "player_data")
public class LobbyPlayer implements Entity<UUID,LobbyPlayer> {

    @Pkey
    private UUID playerUUID;
    @Column(name = "hiding_players")
    private boolean isPlayerHiding;
    @Column(name = "muted")
    private boolean isMuted;
    @Column(name = "time_for_unmute")
    private long timeForUnmute;
    @Column(name = "build_enabled")
    private boolean isBuildEnabled;
    @Column(name = "fly_enabled")
    private boolean isFlying;
    @Column(name = "global_pvp_kills")
    private int globalPVPKills;
    @Column(name = "global_pvp_deaths")
    private int globalPVPDeaths;
    private List<Warn> infractions = new ArrayList<>();

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

    public void setPlayerHiding(boolean playerHiding) {

        boolean before = isPlayerHiding;

        isPlayerHiding = playerHiding;

        if (before != isPlayerHiding) update().join();

        if (!getOfflinePlayer().isOnline()) return;

        Player player = getPlayer();

        if (XG7Lobby.getInstance().isInWorldEnabled(player)) {
            Bukkit.getOnlinePlayers().forEach(p -> {

                LobbyPlayer otherPlayer = LobbyPlayer.cast(p.getUniqueId(), false).join();

                if (playerHiding) player.hidePlayer(p);
                else player.showPlayer(p);

                if (otherPlayer.isPlayerHiding()) {
                    if (playerHiding) p.hidePlayer(player);
                    else p.showPlayer(player);
                }
            });
        }

    }

    public void addInfraction(Warn warn) {
        infractions.add(warn);

        update().join();

        Config config = XG7Lobby.getInstance().getConfig("config");

        OfflinePlayer target = getOfflinePlayer();

        config.getList("warn-levels", Map.class).ifPresent(levels -> {

            long warnCount = infractions.stream().filter(warning -> warning.getLevel() == warn.getLevel()).count();

            int totalWarnsToBan = config.get("total-warns-to-ban", Integer.class).orElse(-1);
            int totalWarnsToKick = config.get("total-warns-to-kick", Integer.class).orElse(-1);
            int totalWarnsToMute = config.get("total-warns-to-mute", Integer.class).orElse(-1);

            boolean banIp = config.get("warn-ban-by-ip", Boolean.class).orElse(false);

            levels.stream().filter(map -> map.get("level").equals(warn.getLevel())).findFirst().ifPresent(map -> {

                int warnsToBan = (int) map.get("min-to-ban");
                int warnsToKick = (int) map.get("min-to-kick");
                int warnsToMute = (int) map.get("min-to-mute");
                Bukkit.getScheduler().runTask(XG7Lobby.getInstance(), () -> {
                    if ((warnCount >= warnsToBan && warnsToBan > 0) || (warnCount >= totalWarnsToBan && totalWarnsToBan > 0)) {
                        if (banIp && target.isOnline())
                            Bukkit.getBanList(BanList.Type.IP).addBan(getPlayer().getAddress().getAddress().getHostAddress(), Text.fromLang(getPlayer(),XG7Lobby.getInstance(), "commands.warn.warn.ban").join().getText(), null, null);
                        else
                            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), Text.fromLang(getPlayer(),XG7Lobby.getInstance(), "commands.warn.warn-ban").join().replace("reason", warn.getReason()).getText(), null, null);
                        if (target.isOnline())
                            target.getPlayer().kickPlayer(Text.fromLang(getPlayer(),XG7Lobby.getInstance(), "commands.warn.warn-ban").join().replace("reason", warn.getReason()).getText());
                    }

                    if (((warnCount >= warnsToKick && warnsToKick > 0) || (warnCount >= totalWarnsToKick && totalWarnsToKick > 0)) && target.isOnline()) {
                        getPlayer().kickPlayer(Text.fromLang(getPlayer(),XG7Lobby.getInstance(), "commands.warn.warn-kick").join().replace("reason", warn.getReason()).getText());
                    }
                });
                if ((warnCount >= warnsToMute && warnsToMute > 0) || (warnCount >= totalWarnsToMute && totalWarnsToMute > 0)) {
                    setMuted(true);
                    if(config.get("warn-time-to-unmute",String.class).orElse("").toLowerCase().equals("forever")) setTimeForUnmute(System.currentTimeMillis() + config.getTime("warn-time-to-unmute").orElse(0L));

                    if (target.isOnline()) Text.fromLang(getPlayer(),XG7Lobby.getInstance(), "commands.warn.warn-mute").thenAccept(text -> text.replace("reason", warn.getReason()).send(getPlayer()));
                }
            });
        });
    }

    public void removeInfraction(Warn warn) {
        infractions.removeIf(warn::equals);
        update().join();
    }

    public CompletableFuture<Boolean> update() {
        return XG7Lobby.getInstance().getPlayerDAO().update(this);
    }

    @Override
    public boolean equals(LobbyPlayer lobbyPlayer) {
        return playerUUID.equals(lobbyPlayer.getPlayerUUID());
    }

    @Override
    public UUID getID() {
        return this.playerUUID;
    }
}
