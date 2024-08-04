package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.CommandManager;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.actions.ActionType;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.ItemPages;
import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.menus.MenuPages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbyhelp";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.DIAMOND.parseItem(), "§6Help command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Opens the menu gui";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyhelp";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.HELP;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Getter
    private static MenuPages pages;

    public static void init() {
        Menu initialMenu = new Menu("xg7lhelpinit", "Help", 54);

        initialMenu.addItems(
                new InventoryItem(XMaterial.COMMAND_BLOCK.parseItem(), "§bCommands", Collections.singletonList("§aClick to see the commands"),1,29),
                new InventoryItem(XMaterial.OAK_SIGN.parseItem(), "§bActions", Collections.singletonList("§aClick to see the actions"),1,30),
                new InventoryItem(XMaterial.CHEST.parseItem(), "§bSelectors guide", Collections.singletonList("§aClick to see the selectors guide"),1,32),
                new InventoryItem(XMaterial.BLAZE_ROD.parseMaterial(), "§bCustom commands guide", Collections.singletonList("§aClick to see the custom commands guide"),1,33),

                new InventoryItem(XMaterial.COMPASS.parseMaterial(), "§bSet lobby position",
                        Arrays.asList(
                                "§aClick to set your lobby position",
                                "§aCurrent lobby position: §r[" + Config.getString(ConfigType.DATA, "spawn-location.world") + ", " + (int) Config.getDouble(ConfigType.DATA, "spawn-location.x") + ", " + (int) Config.getDouble(ConfigType.DATA, "spawn-location.y") + ", " + (int) Config.getDouble(ConfigType.DATA, "spawn-location.z") + "]"
                        ),1,31),

                new InventoryItem(XMaterial.BARRIER.parseItem().getData(), "§cClose", Collections.singletonList("§aClick to close"),1,45),
                new InventoryItem(XMaterial.PAPER.parseMaterial(), "§bCollaborators", Collections.singletonList("§aAll plugin helpers"),1,53)
        );

        List<InventoryItem> commandsItem = CommandManager.getCommands().stream().map(Command::getIcon).collect(Collectors.toList());

        ItemPages commands = new ItemPages("xg7lhelpcommands", "Commands", 54, commandsItem, new Menu.InventoryCoordinate(2,2), new Menu.InventoryCoordinate(8,4));

        commands.addItems(
                new InventoryItem.SkullInventoryItem(
                        "§bPrevious page",
                        Collections.singletonList("§aClick to go back"),
                        1, 52
                ).setValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM5OWFhNmZjMmVjY2UzNTY2NWQ5NDhhMDEzMjUxNTNmZTUzZmMxNzcxZmIyNzg0ZjU3OTY3ZjEwZTJkZGNmOCJ9fX0="),
                new InventoryItem.SkullInventoryItem(
                        "§bNext page",
                        Collections.singletonList("§aClick to go forward"),
                        1, 53
                ).setValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThhZTExYTljOTQwYzVhYzYyYjkwNTgzN2QyMTUzN2RiZTJmM2U1MDExZjBiYmJmZGMxMTIyNGI4NjAzZGJiZCJ9fX0="),
                new InventoryItem(XMaterial.BARRIER.parseMaterial(), "§cBack to main menu", Collections.singletonList("§aClick to go to main menu"),1,45)
        );

        List<InventoryItem> actionItem = ActionType.getIcons();
        ItemPages actions = new ItemPages("xg7lhelpactions", "Actions", 54, actionItem, new Menu.InventoryCoordinate(2,2), new Menu.InventoryCoordinate(8,4));

        actions.addItems(
                new InventoryItem.SkullInventoryItem(
                        "§bPrevious page",
                        Collections.singletonList("§aClick to go back"),
                        1, 52
                ).setValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM5OWFhNmZjMmVjY2UzNTY2NWQ5NDhhMDEzMjUxNTNmZTUzZmMxNzcxZmIyNzg0ZjU3OTY3ZjEwZTJkZGNmOCJ9fX0="),
                new InventoryItem.SkullInventoryItem(
                        "§bNext page",
                        Collections.singletonList("§aClick to go forward"),
                        1, 53
                ).setValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThhZTExYTljOTQwYzVhYzYyYjkwNTgzN2QyMTUzN2RiZTJmM2U1MDExZjBiYmJmZGMxMTIyNGI4NjAzZGJiZCJ9fX0="),
                new InventoryItem(XMaterial.BARRIER.parseMaterial(), "§cBack to main menu", Collections.singletonList("§aClick to go to main menu"),1,45)
        );

        Menu creatorsMenu = new Menu("xg7lhelpcreators", "Creators", 27);

        creatorsMenu.addItems(
                new InventoryItem.SkullInventoryItem("§beduardo10YT", Collections.singletonList("§aBeta tester"), 1,11).setPlayerSkinValue(UUID.fromString("7fb536ea-ec01-4bce-9fc9-bdc2d759de3c")),
                new InventoryItem.SkullInventoryItem("§bG0RrXD18", Collections.singletonList("§aBeta tester"), 1,12).setPlayerSkinValue(UUID.fromString("f66d01bf-0e1c-4800-9a50-060411bff0bd")),
                new InventoryItem.SkullInventoryItem("§bDaviXG7", Collections.singletonList("§aCreator of all plugin"), 1,13).setPlayerSkinValue(UUID.fromString("45766b7f-9789-40e1-bd0b-46fa0d032bde")),
                new InventoryItem.SkullInventoryItem("§bSadness_Sad", Collections.singletonList("§aVideo helper and plugin helper"), 1,14).setPlayerSkinValue(UUID.fromString("f12b8505-8b77-4046-9d86-8b5303690096")),
                new InventoryItem.SkullInventoryItem("§bBultzzXG7", Collections.singletonList("§aVideo helper"), 1,15).setPlayerSkinValue(UUID.fromString("696581df-4256-4028-b55e-9452b4de40b6"))
                ,new InventoryItem(XMaterial.BARRIER.parseMaterial(), "§cBack to main menu", Collections.singletonList("§aClick to go to main menu"),1,18)

        );


        pages = new MenuPages(initialMenu,commands,actions,creatorsMenu);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Player player = (Player) sender;

        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(),
        () -> pages.openMenu("xg7lhelpinit", player),5L);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
