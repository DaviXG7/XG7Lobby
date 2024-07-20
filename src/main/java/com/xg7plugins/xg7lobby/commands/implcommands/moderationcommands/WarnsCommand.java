package com.xg7plugins.xg7lobby.commands.implcommands.moderationcommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.ItemPages;
import com.xg7plugins.xg7menus.api.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WarnsCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbywarns";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.NAME_TAG.parseMaterial(), "&6Warns command", Arrays.asList("&9Description: " + getDescription(), "&9Usage: &7&o" + getSyntax(), "&9Permission: &b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Opens the warn GUI";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbywarns";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            if (!sender.hasPermission(PermissionType.WARNS_OTHER.getPerm())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-not-found"), sender);
                return true;
            }

            PlayerData data = PlayerManager.getPlayerData(target.getUniqueId());

            List<InventoryItem> warnItems = data.getInfractions().stream().map(warn -> {

                List<String> lore = Config.getList(ConfigType.CONFIG, "warns-gui.warn-item.lore").stream().map(text -> Text.translateColorCodes(text).replace("[WARN]", warn.getWarn()).replace("[ID]", warn.getId().toString()).replace("[DATE]", new SimpleDateFormat("dd/MM/yy HH:mm").format(warn.getDate())).replace("[LEVEL]", warn.getLevel() + "")).collect(Collectors.toList());

                lore.add(Text.translateColorCodes("&aClick to copy the id! &fID: " + warn.getId()));

                return new InventoryItem(
                        XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.warn-item-material-level-" + warn.getLevel())).parseItem().getData(),
                        Text.getFormatedText(target.getPlayer(), Config.getString(ConfigType.CONFIG, "warns-gui.warn-item.name").replace("[WARN]", warn.getWarn()).replace("[ID]", warn.getId().toString()).replace("[DATE]", new SimpleDateFormat("dd/MM/yy HH:mm").format(warn.getDate())).replace("[LEVEL]", warn.getLevel() + "")),
                        lore, 1, -1
                );
            }).collect(Collectors.toList());

            ItemPages menu = new ItemPages("xg7lobby:warns", Text.translateColorCodes("Warns of " + target.getName() + ": " + warnItems.size()), 54, warnItems, Menu.InventoryCoordinate.fromList(Config.getIntegerList(ConfigType.CONFIG, "warns-gui.inv-pos-1")), Menu.InventoryCoordinate.fromList(Config.getIntegerList(ConfigType.CONFIG, "warns-gui.inv-pos-2")));

            if (Config.getString(ConfigType.CONFIG, "warns-gui.fill-item") != null && !Config.getString(ConfigType.CONFIG, "warns-gui.fill-item").equals("AIR")) menu.setFillItem(new InventoryItem(XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.fill-item")).parseItem().getData(), " ", new ArrayList<>(), 1, -1));

            menu.addItems(
                    new InventoryItem(
                            XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.go-next-page.material")).parseItem().getData(),
                            Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.go-next-page.name")),
                            Config.getList(ConfigType.CONFIG, "warns-gui.go-next-page.lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()),
                            1,
                            Config.getInt(ConfigType.CONFIG, "warns-gui.go-next-page.slot") - 1
                    ),
                    new InventoryItem(
                            XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.go-back-page.material")).parseItem().getData(),
                            Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.go-back-page.name")),
                            Config.getList(ConfigType.CONFIG, "warns-gui.go-back-page.lore").stream().map(Text::translateColorCodes).collect(Collectors.toList()),
                            1,
                            Config.getInt(ConfigType.CONFIG, "warns-gui.go-back-page.slot") - 1
                    )
            );

            menu.open(player);

            return true;
        }


        PlayerData data = PlayerManager.createPlayerData(player.getUniqueId());

        List<InventoryItem> warnItems = data.getInfractions().stream().map(warn -> new InventoryItem(
                XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.warn-item-material-level-" + warn.getLevel())).parseItem().getData(),
                Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.warn-item.name").replace("[WARN]", warn.getWarn()).replace("[ID]", warn.getId().toString()).replace("[DATE]", new SimpleDateFormat("dd/MM/yy HH:mm").format(warn.getDate())).replace("[LEVEL]", warn.getLevel() + "")),
                Config.getList(ConfigType.CONFIG, "warns-gui.warn-item.lore").stream().map(text -> Text.getFormatedText(player, text).replace("[WARN]", warn.getWarn()).replace("[ID]", warn.getId().toString()).replace("[DATE]", new SimpleDateFormat("dd/MM/yy HH:mm").format(warn.getDate())).replace("[LEVEL]", warn.getLevel() + "")).collect(Collectors.toList()),
                1, -1
        )).collect(Collectors.toList());

        ItemPages menu = new ItemPages("xg7lobby:warns", Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.title")), 54, warnItems, Menu.InventoryCoordinate.fromList(Config.getIntegerList(ConfigType.CONFIG, "warns-gui.inv-pos-1")), Menu.InventoryCoordinate.fromList(Config.getIntegerList(ConfigType.CONFIG, "warns-gui.inv-pos-2")));

        if (Config.getString(ConfigType.CONFIG, "warns-gui.fill-item") != null && !Config.getString(ConfigType.CONFIG, "warns-gui.fill-item").equals("AIR")) menu.setFillItem(new InventoryItem(XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.fill-item")).parseItem().getData(), " ", new ArrayList<>(), 1, -1));

        menu.addItems(
                new InventoryItem(
                        XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.go-next-page.material")).parseItem().getData(),
                        Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.go-next-page.name")),
                        Config.getList(ConfigType.CONFIG, "warns-gui.go-next-page.lore").stream().map(text -> Text.getFormatedText(player, text)).collect(Collectors.toList()),
                        1,
                        Config.getInt(ConfigType.CONFIG, "warns-gui.go-next-page.slot") - 1
                ),
                new InventoryItem(
                        XMaterial.valueOf(Config.getString(ConfigType.CONFIG, "warns-gui.go-back-page.material")).parseItem().getData(),
                        Text.getFormatedText(player, Config.getString(ConfigType.CONFIG, "warns-gui.go-back-page.name")),
                        Config.getList(ConfigType.CONFIG, "warns-gui.go-back-page.lore").stream().map(text -> Text.getFormatedText(player, text)).collect(Collectors.toList()),
                        1,
                        Config.getInt(ConfigType.CONFIG, "warns-gui.go-back-page.slot") - 1
                )
        );

        menu.open(player);


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return sender.hasPermission(PermissionType.WARN_REMOVE.getPerm()) && args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
