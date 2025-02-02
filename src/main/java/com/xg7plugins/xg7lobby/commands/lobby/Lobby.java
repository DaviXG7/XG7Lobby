package com.xg7plugins.xg7lobby.commands.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.tasks.CooldownManager;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Command(
        name = "lobby",
        syntax = "lobby (id) (<id> <player>)",
        description = "Teleport to the lobby",
        permission = "xg7lobby.command.lobby.teleport"
)
public class Lobby implements ICommand {

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        String id;

        if (args.len() > 0) {
            if (!sender.hasPermission("xg7lobby.command.lobby.teleport.id")) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            id = args.get(0, String.class);
        } else {
            id = null;
        }

        Player targetToTeleport = null;
        boolean targetIsOther = false;

        if (args.len() < 2) {
            if (!(sender instanceof Player)) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.not-a-player").thenAccept(text -> text.send(sender));
                return;
            }
            targetToTeleport = (Player) sender;
        }

        if (args.len() > 1) {
            if (!sender.hasPermission("xg7lobby.command.lobby.teleport.other")) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            OfflinePlayer target = args.get(1, Player.class);

            if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
                return;
            }
            if (!target.isOnline()) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "commands.not-online").thenAccept(text -> text.send(sender));
                return;
            }
            targetToTeleport = target.getPlayer();
            targetIsOther = true;
        }

        final Player finalTargetToTeleport = targetToTeleport;
        final boolean finalTargetIsOther = targetIsOther;

        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("lobby-cooldown-before", targetToTeleport)) {
            XG7Plugins.getInstance().getCooldownManager().removePlayer("lobby-cooldown-before", targetToTeleport.getUniqueId());
            if (targetIsOther) Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-teleporting-message").thenAccept(text -> text.send(sender));
            return;
        }

        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("lobby-cooldown-after", targetToTeleport)) {
            double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("lobby-cooldown-after", targetToTeleport);
            Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-teleport.on-cooldown" + (finalTargetIsOther ? "-other" : "")).thenAccept(text -> text
                    .replace("[PLAYER]", finalTargetToTeleport.getName())
                    .replace("[MILLISECONDS]", String.valueOf((cooldownToToggle)))
                    .replace("[SECONDS]", String.valueOf((int) ((cooldownToToggle) / 1000)))
                    .replace("[MINUTES]", String.valueOf((int) ((cooldownToToggle) / 60000)))
                    .replace("[HOURS]", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                    .send(sender));
            return;
        }

        Consumer<LobbyLocation> teleport = lobby -> {

            if (lobby == null) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-teleport.on-error-doesnt-exist" + (sender.hasPermission("xg7lobby.commands.lobby.setlobby") ? "-adm" : "")).thenAccept(text -> text.send(sender));
                return;
            }

            XG7Plugins.getInstance().getCooldownManager().addCooldown(finalTargetToTeleport,
                    new CooldownManager.CooldownTask(
                            "lobby-cooldown-before",
                            finalTargetToTeleport.hasPermission("xg7lobby.command.lobby.bypass-cooldown") ? 0 : XG7Lobby.getInstance().getConfig("config").getTime("lobby-teleport-cooldown.before-teleport").orElse(5000L),
                            player -> Text.formatLang(
                                    XG7Lobby.getInstance(),
                                    player,
                                    "lobby.on-teleporting-message"
                            ).thenAccept(text -> {
                                double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("lobby-cooldown-before", finalTargetToTeleport);

                                text.
                                        replace("[MILLISECONDS]", String.valueOf((cooldownToToggle)))
                                        .replace("[SECONDS]", String.valueOf((int) ((cooldownToToggle) / 1000)))
                                        .replace("[MINUTES]", String.valueOf((int) ((cooldownToToggle) / 60000)))
                                        .replace("[HOURS]", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                                        .send(player);
                            }),
                            ((player, aBoolean) -> {
                                if (aBoolean) {
                                    Text.formatLang(XG7Lobby.getInstance(), player, "lobby.teleport-cancelled").thenAccept(text -> text.send(player));
                                    return;
                                }
                                XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> lobby.teleport(finalTargetToTeleport));
                                if (!finalTargetToTeleport.hasPermission("xg7lobby.command.lobby.bypass-cooldown")) XG7Plugins.getInstance().getCooldownManager().addCooldown(finalTargetToTeleport, "lobby-cooldown-after", XG7Lobby.getInstance().getConfig("config").getTime("lobby-teleport-cooldown.after-teleport").orElse(5000L));

                            })
                    )
            );

            if (finalTargetIsOther) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-teleport.on-success-other").thenAccept(text -> {
                    text.replace("[PLAYER]", finalTargetToTeleport.getName()).send(sender);
                });
            }
        };

        if (id == null) {
            XG7Lobby.getInstance().getLobbyManager().getRandomLobby().thenAccept(teleport);
            return;
        }
        XG7Lobby.getInstance().getLobbyManager().getLobby(id).thenAccept(teleport);
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        if (args.len() == 1 && sender.hasPermission("xg7lobby.command.lobby.id")) {
            return XG7Plugins.getInstance().getDatabaseManager().getCachedEntities().asMap().join().values().stream().filter(ob -> ob instanceof LobbyLocation).map(e -> ((LobbyLocation)e).getId()).collect(Collectors.toList());
        }
        if (args.len() == 2 && sender.hasPermission("xg7lobby.command.lobby.other")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return ICommand.super.onTabComplete(sender, args);
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.ENDER_PEARL, this);
    }
}
