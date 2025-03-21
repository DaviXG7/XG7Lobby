package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.tasks.CooldownManager;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        name = "pvp",
        permission = "xg7lobby.command.pvp",
        syntax = "/pvp",
        description = "Enable or disable PVP",
        isInEnabledWorldOnly = true,
        isPlayerOnly = true
)
public class PVPCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("global-pvp.enabled", Boolean.class).orElse(false);
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        Player player = (Player) sender;

        if (!XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(player)) XG7Lobby.getInstance().getGlobalPVPManager().addPlayerToPVP(player);
        else {

            Config config = XG7Lobby.getInstance().getConfig("config");

            if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("pvp-disable", player)) {
                XG7Plugins.getInstance().getCooldownManager().removePlayer("pvp-disable", player.getUniqueId());
                return;
            }

            XG7Plugins.getInstance().getCooldownManager().addCooldown(player, new CooldownManager.CooldownTask(
                    "pvp-disable",
                    config.get("global-pvp.on-leave-pvp.dont-move",Boolean.class).orElse(true) ? config.getTime("global-pvp.disable-cooldown").orElse(5000L) : 0,
                    p -> {

                        double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("pvp-disable", p);

                        Text.fromLang(player,XG7Lobby.getInstance(), "pvp.pvp-disabling").join()
                                .replace("player", p.getName())
                                .replace("milliseconds", String.valueOf((cooldownToToggle)))
                                .replace("seconds", String.valueOf((int) ((cooldownToToggle) / 1000)))
                                .replace("minutes", String.valueOf((int) ((cooldownToToggle) / 60000)))
                                .replace("hours", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                                .send(sender);
                    },
                    (p, b) -> {
                        if (b) {
                            Text.fromLang(player,XG7Lobby.getInstance(), "pvp.disable-cancelled").thenAccept(text -> text.send(player));
                            return;
                        }
                        XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> XG7Lobby.getInstance().getGlobalPVPManager().removePlayerFromPVP(player));
                    })
            );

        }
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.DIAMOND_SWORD, this);
    }
}
