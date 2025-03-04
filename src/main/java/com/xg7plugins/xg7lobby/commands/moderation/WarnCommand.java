package com.xg7plugins.xg7lobby.commands.moderation;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.data.database.query.Query;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Command(
        name = "warn",
        description = "Warn a player",
        syntax = "/warn <player|pardon> <level|id> <reason>",
        permission = "xg7lobby.command.moderation.warn",
        isAsync = true
)
public class WarnCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    ICommand[] subCommands = new ICommand[]{new Pardon()};

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.BOOK, this);
    }

    public ICommand[] getSubCommands() {
        return subCommands;
    }

    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() < 3) {
            syntaxError(sender, "warn <player|pardon> <level|id> <reason>");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);
        int level = args.get(1, int.class);
        String reason = Strings.join(Arrays.asList(Arrays.copyOfRange(args.getArgs(), 2, args.len())), ' ');

        Config config = XG7Lobby.getInstance().getConfig("config");

        if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
            Text.fromLang(sender, XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        if (level < 1) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "warn-level-invalid").thenAccept(text -> text.send(sender));
            return;
        }

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        if (!config.get("warn-admin",Boolean.class).orElse(false) && target.isOp()) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.warn.warn-admin").thenAccept(text -> text.send(sender));
            return;
        }

        lobbyPlayer.addInfraction(new Warn(lobbyPlayer.getPlayerUUID(), level, reason));

        Text.fromLang(sender, XG7Lobby.getInstance(), "commands.warn.on-warn-sender").thenAccept(text -> text.replace("target", target.getName()).replace("reason", reason).send(sender));
        if (target.isOnline()) Text.fromLang(lobbyPlayer.getPlayer(), XG7Lobby.getInstance(), "commands.warn.on-warn").thenAccept(text -> text.replace("reason", reason).send(target.getPlayer()));


    }

    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {

        switch (args.len()) {
            case 1:
                List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                names.add("pardon");
                return names;
            case 2:
                if (args.get(0, String.class).equalsIgnoreCase("pardon")) {

                    List<Entity> lobbyPlayers = XG7Plugins.getInstance().getDatabaseManager().getCachedEntities().asMap().join().values().stream().filter(entity -> entity instanceof LobbyPlayer).collect(Collectors.toList());
                    List<String> warnIds = new ArrayList<>();

                    for (Entity entity : lobbyPlayers) {
                        LobbyPlayer lobbyPlayer = (LobbyPlayer) entity;
                        warnIds.addAll(lobbyPlayer.getInfractions().stream().map(warn -> warn.getID().toString()).collect(Collectors.toList()));
                    }

                    return warnIds;
                }
                return XG7Lobby.getInstance().getConfig("config").getList("warn-levels", Map.class).orElse(new ArrayList<>()).stream().map(map -> map.get("level").toString()).collect(Collectors.toList());
            case 3:
                return Collections.singletonList("Reason");
        }

        return Collections.emptyList();
    }

    @Command(
            name = "pardon",
            description = "Pardon a warn",
            syntax = "/warn pardon <warnid>",
            permission = "xg7lobby.command.moderation.warn",
            isAsync = true
    )
    static class Pardon implements ICommand {


        @Override
        public Plugin getPlugin() {
            return XG7Lobby.getInstance();
        }

        @Override
        public void onCommand(CommandSender sender, CommandArgs args) {

            if (args.len() != 1) {
                syntaxError(sender, "warn pardon <warnid>");
                return;
            }

            String warnId = args.get(0, String.class);

            if (warnId.split("-").length != 5) {
                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.warn.invalid-id").thenAccept(text -> text.send(sender));
                return;
            }

            try {
                Warn warn = Query.selectFrom(XG7Lobby.getInstance(), Warn.class, UUID.fromString(warnId)).waitForResult().get(Warn.class);

                if (warn == null) {
                    Text.fromLang(sender, XG7Lobby.getInstance(), "commands.warn.warn-not-found").thenAccept(text -> text.send(sender));
                    return;
                }

                LobbyPlayer player = XG7Lobby.getInstance().getPlayerDAO().get(warn.getPlayerUUID()).join();

                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.warn.on-pardon").thenAccept(text -> text.replace("reason", warn.getReason()).replace("id", warnId).send(sender));

                player.removeInfraction(warn);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Item getIcon() {
            return Item.commandIcon(XMaterial.DIAMOND, this);
        }
    }
}
