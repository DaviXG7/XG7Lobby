package com.xg7network.xg7lobby.DefautCommands.HelpCommand;

import java.util.*;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Utils.PluginInventories.InventoryUtil;
import com.xg7network.xg7lobby.Utils.PluginInventories.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class HelpGUI {

    private Player player;
    private static HashMap<String, InventoryUtil> inventoryUtils = new HashMap<>();

    public HelpGUI(Player player) {
        this.player = player;
        //#[01][02][03][04][05][06][07][08][09]
        //#[10][11][12][13][14][15][16][17][18]
        //#[19][20][21][22][23][24][25][26][27]
        //#[28][29][30][31][32][33][34][35][36]
        //#[37][38][39][40][41][42][43][44][45]
        //#[46][47][48][49][50][51][52][53][54]


        //Initial Page

        InventoryUtil inicialPage = new InventoryUtil(player, 6, "&6Help");

        inicialPage.createItemStack(player,
                "PLAYER_HEAD, OWNER=THIS_PLAYER",
                "&a" + player.getName(),
                "&bWelcome to the help!",
                false,
                14,
                1,
                null

        );

        inicialPage.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("COMMAND_BLOCK") ? "COMMAND_BLOCK" : "COMMAND",
                "&cCommands",
                "&aClick to see all commands",
                false,
                30,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Commands").getInventory());
                }
        );

        inicialPage.createItemStack(player,
                "BOOK",
                "&cActions",
                "&aClick to see all Actions",
                false,
                31,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Actions").getInventory());
                }
        );
        inicialPage.createItemStack(player,
                "COMPASS",
                "&fSetLobby",
                "&aCurrent Lobby Location: /// &b%xg7lobby_lobby_location%",
                false,
                32,
                1,
                () -> {
                    player.closeInventory();
                    new LobbyLocation().setLocation(player, player.getLocation());
                }
        );

        inicialPage.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITABLE_BOOK") ? "WRITABLE_BOOK" : "BOOK_AND_QUILL",
                "&fOptions",
                "&aClick to see options!",
                false,
                33,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Options").getInventory());
                }
        );

        inicialPage.createItemStack(player,
                "CHEST",
                "&bSelectors Guide",
                "&aClick to see the selectors guide",
                false,
                34,
                1,
                () -> {
                    player.closeInventory();
                    String[] partes = Bukkit.getVersion().split("\\.");
                    if (partes.length >= 2) {
                        int vers = Integer.parseInt(partes[1]);
                        if (vers >= 15) {
                            player.openBook(getSelectorGuide());
                        } else {

                            player.sendMessage(ChatColor.GREEN+ "Selector guide!");
                            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
                            player.sendMessage(ChatColor.WHITE + "Guide on how to use Selectors\n");
                            player.sendMessage(ChatColor.WHITE + "Selectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file selectors.yml");
                            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
                            player.sendMessage(ChatColor.WHITE + "In the part selectors -> Items -> and there where you create your items\n\nIn the Items part you can create another part with any name and should put the following things on the next message");
                            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
                            player.sendMessage(ChatColor.WHITE + "Item description\n\nmaterial -> Where to place the Item's material\n\nname -> Item name\n\nlore -> Item Description\n\ngrow -> Enchantment or not\n\namount -> amount of the item\n\nslot -> Inventory slot where the item is\n\nactions -> All actions will be performed when the player clicks");
                            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
                            player.sendMessage(ChatColor.WHITE + "In the cooldown part is the item refresh time to be given to the player at all times");
                            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");


                        }
                    }

                }
        );

        inicialPage.createItemStack(player,
                "BARRIER",
                "&cCLOSE",
                "",
                false,
                46,
                1,
                player::closeInventory

        );

        inicialPage.createItemStack(player,
                "PAPER",
                "&dCollaborators",
                "&aAll plugin helpers",
                false,
                54,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Collaborators").getInventory());
                }
        );

        inventoryUtils.put("Initial Page", inicialPage);


        //Commands


        InventoryUtil commands = new InventoryUtil(player, 6, "&8Commands");

        commands.createItemStack(player,
                "IRON_PICKAXE",
                "§e/§bbuild",
                "&f&iAllows you to build /// &bPermission: &axg7lobby.build",
                false,
                11,
                1,
                null
        );

        commands.createItemStack(player,
                "NOTE_BLOCK",
                "§e/§bmute §2§i<Player>",
                "&f&iMutes a player /// &bPermission: &axg7lobby.command.mute",
                false,
                12,
                1,
                null
        );

        commands.createItemStack(player,
                "NOTE_BLOCK",
                "§e/§btempmute §2§i<Player> <d/h/min or Date>",
                "&f&iMutes a player for a time /// &bPermission: &axg7lobby.command.mute",
                false,
                13,
                1,
                null
        );

        commands.createItemStack(player,
                "NOTE_BLOCK",
                "§e/§bunmute §2§i<Player>",
                "&f&iUnmute a player /// &bPermission: &axg7lobby.command.mute",
                false,
                14,
                1,
                null
        );

        commands.createItemStack(player,
                "BARRIER",
                "§e/§bban §2§i<Player> [Reason]",
                "&f&iBan a player /// &bPermission: &axg7lobby.command.ban",
                false,
                15,
                1,
                null
        );

        commands.createItemStack(player,
                "BARRIER",
                "§e/§btempban §2§i<Player> <Time> [Reason]",
                "&f&iBan a player for a time /// &bPermission: &axg7lobby.command.ban",
                false,
                16,
                1,
                null
        );

        commands.createItemStack(player,
                "BARRIER",
                "§e/§bunban §2§i<Player>",
                "&f&iUnban a player /// &bPermission: &axg7lobby.command.ban",
                false,
                17,
                1,
                null
        );

        commands.createItemStack(player,
                "IRON_BOOTS",
                "§e/§bkick §2§i<Player> [Reason]",
                "&f&iKick a player /// &bPermission: &axg7lobby.command.kick",
                false,
                20,
                1,
                null
        );

        commands.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_SIGN") ? "OAK_SIGN" : "SIGN",
                "§e/§bwarn §2§i<Player> <Reason>",
                "&f&iWarn a player /// &bPermission: &axg7lobby.command.warn",
                false,
                21,
                1,
                null
        );

        commands.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITABLE_BOOK") ? "WRITABLE_BOOK" : "BOOK_AND_QUILL",
                "§e/§bwarns",
                "&f&iOpen you warns list /// &bPermission: &ano permission",
                false,
                22,
                1,
                null
        );

        commands.createItemStack(player,
                "COMPASS",
                "§e/§bsetlobby §2§i((world, x, y, z) (world, x, y, z, yaw, pitch) on console)",
                "&f&iSet the lobby location /// &aClick to put! /// &bPermission: &axg7lobby.command.setlobby",
                false,
                23,
                1,
                () -> new LobbyLocation().setLocation(player, player.getLocation())
        );

        commands.createItemStack(player,
                "COMPASS",
                "§e/§blobby",
                "&f&iTeleport to lobby location /// &aClick to teleport! /// &bPermission: &ano permission",
                false,
                24,
                1,
                () -> player.teleport(new LobbyLocation().getLocation())
        );

        commands.createItemStack(player,
                "FEATHER",
                "§e/§bfly §2§i(Player) needs: xg7lobby.command.flyother",
                "&f&iMake the players fly /// &aClick to enable flight /// &bPermission: &axg7lobby.command.fly",
                false,
                25,
                1,
                () -> player.performCommand("7lfly")
        );

        commands.createItemStack(player,
                "PAPER",
                "§e/§bgui §2§i<id>",
                "&f&iOpen a custom inventory /// &bPermission: &axg7lobby.command.gui",
                false,
                29,
                1,
                null
        );

        commands.createItemStack(player,
                "BEDROCK",
                "§e/§bgmc §2§i(Player) needs this permission and: xg7lobby.gamemode.others",
                "&f&iChange the game mode to creative /// &bPermission: &axg7lobby.gamemode.creative",
                false,
                30,
                1,
                null
        );

        commands.createItemStack(player,
                "BEDROCK",
                "§e/§bgms §2§i(Player) needs this permission and: xg7lobby.gamemode.others",
                "&f&iChange the game mode to survival /// &bPermission: &axg7lobby.gamemode.survival",
                false,
                31,
                1,
                null
        );

        commands.createItemStack(player,
                "BEDROCK",
                "§e/§bgma §2§i(Player) needs this permission and: xg7lobby.gamemode.others",
                "&f&iChange the game mode to adventure /// &bPermission: &axg7lobby.gamemode.adventure",
                false,
                32,
                1,
                null
        );

        commands.createItemStack(player,
                "BEDROCK",
                "§e/§bgmsp §2§i(Player) needs this permission and: xg7lobby.gamemode.others",
                "&f&iChange the game mode to spectator /// &bPermission: &axg7lobby.gamemode.spactator",
                false,
                33,
                1,
                null
        );

        commands.createItemStack(player,
                "BARRIER",
                "§e/§blockchat",
                "&f&iLocks the chat /// &bPermission: &axg7lobby.gamemode.spactator",
                false,
                26,
                1,
                null
        );

        commands.createItemStack(player,
                "BARRIER",
                "§cBack",
                "",
                false,
                54,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Initial Page").getInventory());
                }
        );

        inventoryUtils.put("Commands", commands);

        //ACTIONS

        InventoryUtil actions = new InventoryUtil(player, 6, "&8Actions");

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_SIGN") ? "OAK_SIGN" : "SIGN",
                "§aTitle",
                "&bSend a title to the player /// &dUsage: TITLE: (Title)",
                false,
                11,
                1,
                null
        );

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_SIGN") ? "OAK_SIGN" : "SIGN",
                "§aSubTitle",
                "&bSend a subtitle to the player /// &dUsage: SUBTITLE: (Subtitle)",
                false,
                12,
                1,
                null
        );

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_SIGN") ? "OAK_SIGN" : "SIGN",
                "§aTitle and subtitle",
                "&bSend a title and a subtitle to the player /// &dUsage: TITSUBTIT: (Title) // (SubTitle)",
                false,
                13,
                1,
                null
        );

        actions.createItemStack(player,
                "CHEST",
                "§aOpen an inventory",
                "&bOpen a custom inventory /// &dUsage: OPEN: (id)",
                false,
                14,
                1,
                null
        );

        actions.createItemStack(player,
                "CHEST",
                "§aClose the inventory",
                "&bClose the current inventory /// &dUsage: CLOSE",
                false,
                15,
                1,
                null
        );

        actions.createItemStack(player,
                "BEDROCK",
                "§aGame mode change",
                "&bChange the game mode /// &dUsage: GAMEMODE: survival/creative/adventure/spectator",
                false,
                16,
                1,
                null
        );

        actions.createItemStack(player,
                "ENDER_PEARL",
                "§aTeleport",
                "&bTeleport the player to a place /// &dUsage: TP: (world, x, y, z) (world, x, y, z, yaw, pitch)",
                false,
                17,
                1,
                null
        );

        actions.createItemStack(player,
                "PAPER",
                "§aBroadcast",
                "&bBroadcast the message to all server /// &dUsage: BROADCAST: (message)",
                false,
                20,
                1,
                null
        );

        actions.createItemStack(player,
                "EGG",
                "§aSummon",
                "&bSummon an entity on player's location /// &dUsage: SUMMON: (entity_name_on_spigot)",
                false,
                21,
                1,
                null
        );

        actions.createItemStack(player,
                "GLASS_BOTTLE",
                "§aEffect",
                "&bGive a potion effect to de player /// &dUsage: EFFECT: (effect_name_on_spigot, duration, amplifier)",
                false,
                22,
                1,
                null
        );
        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("COMMAND_BLOCK") ? "COMMAND_BLOCK" : "COMMAND",
                "§aPlayer command",
                "&bMakes the player perform a command /// &dUsage: COMMAND: (command name)",
                false,
                23,
                1,
                null
        );

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("COMMAND_BLOCK") ? "COMMAND_BLOCK" : "COMMAND",
                "§aConsole command",
                "&bMakes the console perform a command /// &dUsage: CONSOLE: (command name)",
                false,
                24,
                1,
                null
        );

        actions.createItemStack(player,
                "FEATHER",
                "§aFly",
                "&bMakes the player fly /// &dUsage: FLY",
                false,
                25,
                1,
                null
        );

        actions.createItemStack(player,
                "PAPER",
                "§aMessage",
                "&bSends a message to the player /// &dUsage: MESSAGE: (message)",
                false,
                26,
                1,
                null
        );

        actions.createItemStack(player,
                "NOTE_BLOCK",
                "§aSound",
                "&bPlays a sound on player /// &dUsage: SOUND: (sound_on_spigot, volume, pitch)",
                false,
                29,
                1,
                null
        );

        actions.createItemStack(player,
                "EMERALD",
                "§aSwap an item",
                "&bSwap a selector item /// &dUsage: SWAP: (item section 1, item section 2)",
                false,
                30,
                1,
                null
        );

        actions.createItemStack(player,
                "BLAZE_POWDER",
                "§aAction bar",
                "&bSend an action bar /// &dUsage: ACTIONBAR: (message)",
                false,
                31,
                1,
                null
        );

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_DOOR") ? "OAK_DOOR" : "WOOD_DOOR",
                "§aHide",
                "&bHide all the players /// &dUsage: HIDE",
                false,
                32,
                1,
                null
        );

        actions.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("ENDER_EYE") ? "ENDER_EYE" : "EYE_OF_ENDER",
                "§aShow",
                "&bShow all the players /// &dUsage: SHOW",
                false,
                33,
                1,
                null
        );

        actions.createItemStack(player,
                "BARRIER",
                "§cBack",
                "",
                false,
                54,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Initial Page").getInventory());
                }
        );

        inventoryUtils.put("Actions", actions);

        //OPTIONS

        InventoryUtil options = new InventoryUtil(player, 6, "&8Options");

        options.createItemStack(player,
                "PAPER",
                "§cAuto Broadcast: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"),
                "&aClick to change!",
                false,
                11,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("announcements.enabled", !configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(0), "§cAuto Broadcast: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                "BARRIER",
                "§cAntiSpam: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"),
                "&aClick to change!",
                false,
                12,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("anti-spam.enabled", !configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(1), "§cAntiSpam: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                "BOOK",
                "§cInfraction on warn: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"),
                "&aClick to change!",
                false,
                20,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("infraction-on-warn", !configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(2), "§cInfraction on warn: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );
        options.createItemStack(player,
                "BOOK",
                "§cInfraction on mute: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"),
                "&aClick to change!",
                false,
                21,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("infraction-on-mute", !configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(3), "§cInfraction on mute: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"));

                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD") ? "PLAYER_HEAD" : "SKULL_ITEM, 3",
                "§cVoid: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"),
                "&aClick to change!",
                false,
                29,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("cancel-death-by-void", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(4), "§cVoid: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD") ? "PLAYER_HEAD" : "SKULL_ITEM, 3",
                "§cEnter portal: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"),
                "&aClick to change!",
                false,
                30,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("cancel-portal", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(5), "§cEnter portal: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD") ? "PLAYER_HEAD" : "SKULL_ITEM, 3",
                "§cAll with Items: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")),
                "&aClick to change!",
                false,
                31,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("drop-items", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                    configManager.getConfig(ConfigType.CONFIG).set("pickup-items", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(6), "§cAll with Items: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD") ? "PLAYER_HEAD" : "SKULL_ITEM, 3",
                "§cAll with Blocks: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")),
                "&aClick to change!",
                false,
                32,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("break-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                    configManager.getConfig(ConfigType.CONFIG).set("place-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                    configManager.getConfig(ConfigType.CONFIG).set("interact-with-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(7), "§cAll with Blocks: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GRASS_BLOCK") ? "GRASS_BLOCK" : "GRASS",
                "§cSpawn mobs: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"),
                "&aClick to change!",
                false,
                38,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("spawn-mobs", !configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(8), "§cSpawn mobs: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GRASS_BLOCK") ? "GRASS_BLOCK" : "GRASS",
                "§cWeather and Day cycles: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")),
                "&aClick to change!",
                false,
                39,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("day-cycle", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                    configManager.getConfig(ConfigType.CONFIG).set("weather-cycle", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(9), "§cWeather and Day cycles: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GRASS_BLOCK") ? "GRASS_BLOCK" : "GRASS",
                "§cLeaves decay: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"),
                "&aClick to change!",
                false,
                40,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("leaves-decay", !configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(10), "§cLeaves decay: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GRASS_BLOCK") ? "GRASS_BLOCK" : "GRASS",
                "§cBurn: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")),
                "&aClick to change!",
                false,
                41,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("burn-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                    configManager.getConfig(ConfigType.CONFIG).set("block-spread", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(11), "§cBurn: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GRASS_BLOCK") ? "GRASS_BLOCK" : "GRASS",
                "§cExplosions: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"),
                "&aClick to change!",
                false,
                42,
                1,
                () -> {
                    configManager.getConfig(ConfigType.CONFIG).set("cancel-explosions", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"));
                    configManager.saveConfig(ConfigType.CONFIG);
                    configManager.reloadConfig(ConfigType.CONFIG);
                    options.updateItem(options.getItemStacks().get(12), "§cExplosions: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"));
                    player.sendMessage(ChatColor.GREEN + "Changed!");
                }
        );

        options.createItemStack(player,
                "BARRIER",
                "§cBack",
                "",
                false,
                54,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Initial Page").getInventory());
                }
        );

        inventoryUtils.put("Options", options);

        // Helpers

        InventoryUtil collaborators = new InventoryUtil(player, 3, "&8Collaborators");

        collaborators.createItemStack(player,
                "PLAYER_HEAD, OWNER=DaviXG7",
                "§bDaviXG7",
                "&aCreator of all plugin!",
                false,
                13,
                1,
                null
        );


        collaborators.createItemStack(player,
                "PLAYER_HEAD, OWNER=pewtuck90",
                "§bBultzzXG7",
                "&aVideo Helper and Beta tester!",
                false,
                14,
                1,
                null
        );

        collaborators.createItemStack(player,
                "PLAYER_HEAD, OWNER=Enzo270622",
                "§bSadness",
                "&aVideo Helper and Beta tester!",
                false,
                15, 1,
                null
        );

        collaborators.createItemStack(player,
                "BARRIER",
                "§cBack",
                "",
                false,
                19,
                1,
                () -> {
                    player.closeInventory();
                    player.openInventory(inventoryUtils.get("Initial Page").getInventory());
                }
        );

        inventoryUtils.put("Collaborators", collaborators);

    }

    public void open() {

        player.openInventory(inventoryUtils.get("Initial Page").getInventory());

    }

    public boolean equals(Inventory inventory) {

        for (InventoryUtil inventoryUtil : inventoryUtils.values()) {
            if (inventoryUtil.getInventory().equals(inventory)) return true;
        }

        return false;
    }
    private InventoryUtil getInventory(Inventory inventory) {
        for (InventoryUtil inventoryUtil : inventoryUtils.values()) {
            if (inventoryUtil.getInventory().equals(inventory)) return inventoryUtil;
        }
        return null;
    }

    public void execute(ItemStack stack, Inventory inventory) {
        InventoryUtil inventoryUtil = getInventory(inventory);
        if (inventoryUtil != null) inventoryUtil.execute(stack);
    }

    ItemStack getSelectorGuide() {

        ItemStack stack = new ItemStack(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITTEN_BOOK") ? Material.valueOf("WRITTEN_BOOK") : Material.valueOf("BOOK_AND_QUILL"));

        BookMeta meta = (BookMeta) stack.getItemMeta();

        meta.addPage("Guide on how to use Selectors\n\nSelectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file selectors.yml\n\n->");
        meta.addPage("In the part selectors -> Items -> and there where you create your items\n\nIn the Items part you can create another part with any name and should put the following things on the next page");
        meta.addPage("Item description\n\nitem -> Where to place the Item's material\n\nname -> Item name\n\nlore -> Item Description\n\ngrow -> Enchantment or not\n\n->");
        meta.addPage("amount -> amount of the item\n\nslot -> Inventory slot where the item is actions -> All actions will be performed when the player clicks");
        meta.addPage("In the cooldown part is the item refresh time to be given to the player at all times");

        meta.setTitle("§aSelectors Guide");
        meta.setAuthor("DaviXG7");

        meta.setDisplayName("§aSelectors Guide");

        stack.setItemMeta(meta);

        return stack;
    }
}
