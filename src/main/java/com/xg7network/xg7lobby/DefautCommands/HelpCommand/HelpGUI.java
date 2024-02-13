package com.xg7network.xg7lobby.DefautCommands.HelpCommand;

import java.util.*;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.Lobby.LobbyLocation;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.BasicMenu;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.SkullInventoryItem;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.InventoryItem;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class HelpGUI {

    private static HashMap<String, Menu> menus = new HashMap<>();

    public static void openInventory(Player player) {
        //[00][01][02][03][04][05][06][07][08]
        //[09][10][11][12][13][14][15][16][17]
        //[18][19][20][21][22][23][24][25][26]
        //[27][28][29][30][31][32][33][34][35]
        //[36][37][38][39][40][41][42][43][44]
        //[45][46][47][48][49][50][51][52][53]


        //Initial Page

        BasicMenu inicialPage = new BasicMenu("&6Help", 54, player);

        menus.put("Initial Page", inicialPage.addItems(

                new SkullInventoryItem(
                        "&a" + player.getName(),
                        Collections.singletonList("&bWelcome to the help!"),
                        1,
                        13,
                        null,
                        player
                ),

                new InventoryItem(
                        Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("COMMAND_BLOCK") ? Material.getMaterial("COMMAND_BLOCK") : Material.getMaterial("COMMAND"),
                        "&cCommands",
                        Collections.singletonList("&aClick to see all commands"),
                        1,
                        29,
                        () -> {
                            player.closeInventory();
                            menus.get("Commands").open(player);
                        }
                ),

                new InventoryItem(
                        Material.BOOK,
                        "&cActions",
                        Collections.singletonList("&aClick to see all Actions"),
                        1,
                        30,
                        () -> {
                            player.closeInventory();
                            menus.get("Actions").open(player);
                        }
                ),

                new InventoryItem(
                        Material.COMPASS,
                        "&fSetLobby",
                        Arrays.asList("&aCurrent Lobby Location: /// &b%xg7lobby_lobby_location%".split(" /// ")),
                        1,
                        31,
                        () -> {
                            new LobbyLocation().setLocation(player, player.getLocation());
                        }
                ),

                new InventoryItem(
                        Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITABLE_BOOK") ? Material.getMaterial("WRITABLE_BOOK") : Material.getMaterial("BOOK_AND_QUILL"),
                        "&fOptions",
                        Collections.singletonList("&aClick to see options!"),
                        1,
                        32,
                        () -> {
                            player.closeInventory();
                            menus.get("Options").open(player);
                        }
                ),

                new InventoryItem(
                        Material.CHEST,
                        "&bSelectors Guide",
                        Collections.singletonList("&aClick to see the selectors guide"),
                        1,
                        33,
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
                                    player.sendMessage(ChatColor.WHITE + "Selectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file §bselectors.yml");
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
                ),

                new InventoryItem(
                        Material.BLAZE_ROD,
                        "&bCreate Commands Guide",
                        Collections.singletonList("&aClick to see the create commands guide"),
                        1,
                        40,
                        () -> {
                            player.closeInventory();
                            String[] partes = Bukkit.getVersion().split("\\.");
                            if (partes.length >= 2) {
                                int vers = Integer.parseInt(partes[1]);
                                if (vers >= 15) {
                                    player.openBook(getCommandsGuide());
                                } else {

                                    player.sendMessage(ChatColor.GREEN+ "Selector guide!");
                                    player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
                                    player.sendMessage(ChatColor.WHITE + "Guide on how to Create Commands\n\n");
                                    player.sendMessage(ChatColor.RED + "You Cannot put an argument in the command like: test arg1/arg3 arg2\n\n");
                                    player.sendMessage(ChatColor.WHITE + "In §bconfig.yml §rthe part custom-commands you can create your items\n\nWithin this section, you can create another section where the name of the command will be and to create one is very simple, just put the following items in the section:\n\n");
                                    player.sendMessage(ChatColor.WHITE + "description -> The description of the command\n" +
                                            "\n" +
                                            "aliases -> Other Ways in Which You Can Use the\n" +
                                            "\n" +
                                            "permission -> The permission you need to use the\n" +
                                            "\n" +
                                            "actions -> The actions of the command");
                                    player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");


                                }
                            }

                        }
                ),

                new InventoryItem(
                        Material.BARRIER,
                        "&cCLOSE",
                        new ArrayList<>(),
                        1,
                        45,
                        player::closeInventory

                ),

                new InventoryItem(
                        Material.PAPER,
                        "&dCollaborators",
                        Collections.singletonList("&aAll plugin helpers"),
                        1,
                        53,
                        () -> {
                            player.closeInventory();
                            menus.get("Collaborators").open(player);
                        }
                )

        ));


        //Commands


        BasicMenu commands = new BasicMenu("&8Commands", 54, player);

        menus.put("Commands", commands.addItems(

                new InventoryItem(
                        Material.IRON_PICKAXE,
                        "§e/§bbuild",
                        Arrays.asList("&f&iAllows you to build /// &bPermission: &axg7lobby.build".split(" /// ")),
                        1,
                        10,
                        null
                ),

                new InventoryItem(
                        Material.NOTE_BLOCK,
                        "§e/§bmute §2§i<Player>",
                        Arrays.asList("&f&iMutes a player /// &bPermission: &axg7lobby.command.mute".split(" /// ")),
                        1,
                        11,
                        null
                ),
                new InventoryItem(
                        Material.NOTE_BLOCK,
                        "§e/§btempmute §2§i<Player> <d/h/min or Date>",
                        Arrays.asList("&f&iMutes a player for a time /// &bPermission: &axg7lobby.command.mute".split(" /// ")),
                        1,
                        12,
                        null
                ),
                new InventoryItem(
                        Material.NOTE_BLOCK,
                        "§e/§bunmute §2§i<Player>",
                        Arrays.asList("&f&iUnmutes a player /// &bPermission: &axg7lobby.command.mute".split(" /// ")),
                        1,
                        13,
                        null
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§e/§bban §2§i<Player> [Reason]",
                        Arrays.asList("&f&iBan a player /// &bPermission: &axg7lobby.command.ban".split(" /// ")),
                        1,
                        14,
                        null
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§e/§btempban §2§i<Player> <Time> [Reason]",
                        Arrays.asList("&f&iBan a player for a time /// &bPermission: &axg7lobby.command.ban".split(" /// ")),
                        1,
                        15,
                        null
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§e/§bunban §2§i<Player>",
                        Arrays.asList("&f&iUnban a player /// &bPermission: &axg7lobby.command.ban".split(" /// ")),
                        1,
                        16,
                        null
                ),
                new InventoryItem(
                        Material.IRON_BOOTS,
                        "§e/§bkick §2§i<Player> [Reason]",
                        Arrays.asList("&f&iKick a player /// &bPermission: &axg7lobby.command.kick".split(" /// ")),
                        1,
                        19,
                        null
                ),

                new InventoryItem(
                        Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("OAK_SIGN") ? Material.getMaterial("OAK_SIGN") : Material.getMaterial("SIGN"),
                        "§e/§bwarn §2§i<Player> <Reason> §aor §e/§bwarn §2§i<remove> <Player> <warnId>",
                        Arrays.asList("&f&iWarn a player /// &bPermission: &axg7lobby.command.warn".split(" /// ")),
                        1,
                        20,
                        null
                ),

                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("WRITABLE_BOOK") ? Material.getMaterial("WRITABLE_BOOK") : Material.getMaterial("BOOK_AND_QUILL"),
                        "§e/§bwarns",
                        Arrays.asList("&f&iOpen your warns list /// &bPermission: &ano permission".split(" /// ")),
                        1,
                        21,
                        null
                ),

                new InventoryItem(
                        Material.COMPASS,
                        "§e/§bsetlobby §2§i((world, x, y, z) (world, x, y, z, yaw, pitch) on console)",
                        Arrays.asList("&f&iSet the lobby location /// &aClick to put! /// &bPermission: &axg7lobby.command.setlobby".split(" /// ")),
                        1,
                        22,
                        () -> player.performCommand("7lsetlobby")
                ),

                new InventoryItem(
                        Material.COMPASS,
                        "§e/§blobby",
                        Arrays.asList("&f&iTeleport to lobby location /// &aClick to teleport! /// &bPermission: &ano permission".split(" /// ")),
                        1,
                        23,
                        () -> player.performCommand("7llobby")
                ),

                new InventoryItem(
                        Material.FEATHER,
                        "§e/§bfly §2§i<Player> needs: xg7lobby.command.flyother",
                        Arrays.asList("&f&iMake the players fly /// &aClick to enable flight /// &bPermission: &axg7lobby.command.fly".split(" /// ")),
                        1,
                        24,
                        () -> player.performCommand("7lfly")
                ),
                new InventoryItem(
                        Material.PAPER,
                        "§e/§bgui §2§i<id>",
                        Arrays.asList("&f&iOpen a custom inventory /// &bPermission: &axg7lobby.command.gui".split(" /// ")),
                        1,
                        28,
                        null
                ),

                new InventoryItem(
                        Material.BEDROCK,
                        "§e/§bgmc §2§i<Player> needs this permission and: xg7lobby.gamemode.others",
                        Arrays.asList("&f&iChange the game mode to creative /// &bPermission: &axg7lobby.gamemode.creative".split(" /// ")),
                        1,
                        29,
                        null
                ),
                new InventoryItem(
                        Material.BEDROCK,
                        "§e/§bgms §2§i<Player> needs this permission and: xg7lobby.gamemode.others",
                        Arrays.asList("&f&iChange the game mode to survival /// &bPermission: &axg7lobby.gamemode.survival".split(" /// ")),
                        1,
                        30,
                        null
                ),
                new InventoryItem(
                        Material.BEDROCK,
                        "§e/§bgma §2§i<Player> needs this permission and: xg7lobby.gamemode.others",
                        Arrays.asList("&f&iChange the game mode to adventure /// &bPermission: &axg7lobby.gamemode.adventure".split(" /// ")),
                        1,
                        31,
                        null
                ),
                new InventoryItem(
                        Material.BEDROCK,
                        "§e/§bgmsp §2§i<Player> needs this permission and: xg7lobby.gamemode.others",
                        Arrays.asList("&f&iChange the game mode to spectator /// &bPermission: &axg7lobby.gamemode.spactator".split(" /// ")),
                        1,
                        32,
                        null
                ),

                new InventoryItem(
                        Material.BARRIER,
                        "§e/§blockchat",
                        Arrays.asList("&f&iLocks the chat /// &bPermission: &axg7lobby.command.lockchat".split(" /// ")),
                        1,
                        25,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("ENDER_EYE") ? Material.getMaterial("ENDER_EYE") : Material.getMaterial("EYE_OF_ENDER"),
                        "§e/§bvanish",
                        Arrays.asList("&f&iVanish the player on lobby world /// &bPermission: &axg7lobby.command.vanish".split(" /// ")),
                        1,
                        33,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("CLOCK") ? Material.getMaterial("CLOCK") : Material.getMaterial("WATCH"),
                        "§e/§b7lreloadconfig",
                        Arrays.asList("&f&iReloads the config /// &bPermission: &axg7lobby.command.reload".split(" /// ")),
                        1,
                        34,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("GOLDEN_AXE") ? Material.getMaterial("GOLDEN_AXE") : Material.getMaterial("GOLD_AXE"),
                        "§e/§b7lreportbug <Bug>",
                        Arrays.asList("&f&iSend a bug report to the plugin creator /// &bPermission: &axg7lobby.admin".split(" /// ")),
                        1,
                        37,
                        null
                ),
                new InventoryItem(
                        Material.BOOK,
                        "§e/§b7lsuggest <Suggestion>",
                        Arrays.asList("&f&iSend a plugin suggestion to the plugin creator /// &bPermission: &axg7lobby.admin".split(" /// ")),
                        1,
                        38,
                        null
                ),

                new InventoryItem(
                        Material.BARRIER,
                        "§cBack",
                        new ArrayList<>(),
                        1,
                        53,
                        () -> {
                            player.closeInventory();
                            menus.get("Initial Page").open(player);
                        }
                )
        ));


        //ACTIONS

        BasicMenu actions = new BasicMenu("&8Actions", 54, player);

        menus.put("Actions", actions.addItems(
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("OAK_SIGN") ? Material.getMaterial("OAK_SIGN") : Material.getMaterial("SIGN"),
                        "§aTitle",
                        Arrays.asList("&bSend a title to the player /// &dUsage: TITLE: (Title)".split(" /// ")),
                        1,
                        10,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("OAK_SIGN") ? Material.getMaterial("OAK_SIGN") : Material.getMaterial("SIGN"),
                        "§aSubTitle",
                        Arrays.asList("&bSend a subtitle to the player /// &dUsage: SUBTITLE: (Subtitle)".split(" /// ")),
                        1,
                        11,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("OAK_SIGN") ? Material.getMaterial("OAK_SIGN") : Material.getMaterial("SIGN"),
                        "§aTitle and subtitle",
                        Arrays.asList("&bSend a title and a subtitle to the player /// &dUsage: TITSUBTIT: (Title) // (SubTitle)".split(" /// ")),
                        1,
                        12,
                        null
                ),
                new InventoryItem(
                        Material.CHEST,
                        "§aOpen an inventory",
                        Arrays.asList("&bOpen a custom inventory /// &dUsage: OPEN: (id)".split(" /// ")),
                        1,
                        13,
                        null
                ),
                new InventoryItem(
                        Material.CHEST,
                        "§aClose the inventory",
                        Arrays.asList("&bClose the current inventory /// &dUsage: CLOSE".split(" /// ")),
                        1,
                        14,
                        null
                ),
                new InventoryItem(
                        Material.BEDROCK,
                        "§aGame mode change",
                        Arrays.asList("&bChange the game mode /// &dUsage: GAMEMODE: survival/creative/adventure/spectator".split(" /// ")),
                        1,
                        15,
                        null
                ),
                new InventoryItem(
                        Material.ENDER_PEARL,
                        "§aTeleport",
                        Arrays.asList("&bTeleport the player to a place /// &dUsage: TP: (world, x, y, z) (world, x, y, z, yaw, pitch)".split(" /// ")),
                        1,
                        16,
                        null
                ),
                new InventoryItem(
                        Material.PAPER,
                        "§aBroadcast",
                        Arrays.asList("&bBroadcast the message to all server /// &dUsage: BROADCAST: (message)".split(" /// ")),
                        1,
                        19,
                        null
                ),
                new InventoryItem(
                        Material.EGG,
                        "§aSummon",
                        Arrays.asList("&bSummon an entity on player's location /// &dUsage: SUMMON: (entity_name_on_spigot)".split(" /// ")),
                        1,
                        20,
                        null
                ),
                new InventoryItem(
                        Material.GLASS_BOTTLE,
                        "§aEffect",
                        Arrays.asList("&bGive a potion effect to de player /// &dUsage: EFFECT: (effect_name_on_spigot, duration, amplifier)".split(" /// ")),
                        1,
                        21,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("COMMAND_BLOCK") ? Material.getMaterial("COMMAND_BLOCK") : Material.getMaterial("COMMAND"),
                        "§aPlayer command",
                        Arrays.asList("&bMakes the player perform a command /// &dUsage: COMMAND: (command name)".split(" /// ")),
                        1,
                        22,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("COMMAND_BLOCK") ? Material.getMaterial("COMMAND_BLOCK") : Material.getMaterial("COMMAND"),
                        "§aConsole command",
                        Arrays.asList("&bMakes the console perform a command /// &dUsage: CONSOLE: (command name)".split(" /// ")),
                        1,
                        23,
                        null
                ),
                new InventoryItem(
                        Material.FEATHER,
                        "§aFly",
                        Arrays.asList("&bMakes the player fly /// &dUsage: FLY".split(" /// ")),
                        1,
                        24,
                        null
                ),
                new InventoryItem(
                        Material.PAPER,
                        "§aMessage",
                        Arrays.asList("&bSends a message to the player /// &dUsage: MESSAGE: (message)".split(" /// ")),
                        1,
                        25,
                        null
                ),
                new InventoryItem(
                        Material.NOTE_BLOCK,
                        "§aSound",
                        Arrays.asList("&bPlays a sound on player /// &dUsage: SOUND: (sound_on_spigot, volume, pitch)".split(" /// ")),
                        1,
                        28,
                        null
                ),
                new InventoryItem(
                        Material.EMERALD,
                        "§aSwap an item",
                        Arrays.asList("&bSwap a selector item /// &dUsage: SWAP: (item section 1, item section 2)".split(" /// ")),
                        1,
                        29,
                        null
                ),
                new InventoryItem(
                        Material.BLAZE_POWDER,
                        "§aAction bar",
                        Arrays.asList("&bSend an action bar /// &dUsage: ACTIONBAR: (message)".split(" /// ")),
                        1,
                        30,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("OAK_DOOR") ? Material.getMaterial("OAK_DOOR") : Material.getMaterial("WOOD_DOOR"),
                        "§aHide",
                        Arrays.asList("&bHide all the players /// &dUsage: HIDE".split(" /// ")),
                        1,
                        31,
                        null
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("ENDER_EYE") ? Material.getMaterial("ENDER_EYE") : Material.getMaterial("EYE_OF_ENDER"),
                        "§aShow",
                        Arrays.asList("&bShow all the players /// &dUsage: SHOW".split(" /// ")),
                        1,
                        32,
                        null
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§cBack",
                        new ArrayList<>(),
                        1,
                        53,
                        () -> {
                            player.closeInventory();
                            menus.get("Initial Page").open(player);
                        }
                )
        ));


        //OPTIONS

        BasicMenu options = new BasicMenu("&8Actions", 54, player);

        menus.put("Options", options.addItems(

                new InventoryItem(
                        Material.PAPER,
                        "§cAuto Broadcast: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        10,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("announcements.enabled", !configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(10).updateName("§cAuto Broadcast: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§cAntiSpam: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        11,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("anti-spam.enabled", !configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(11).updateName("§cAntiSpam: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("anti-spam.enabled"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Material.BOOK,
                        "§cInfraction on warn: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        19,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("infraction-on-warn", !configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(19).updateName("§cInfraction on warn: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-warn"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Material.BOOK,
                        "§cInfraction on mute: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        20,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("infraction-on-mute", !configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(20).updateName("§cInfraction on mute: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD") ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : Material.getMaterial("SKULL_ITEM").getNewData((byte)3),
                        "§cVoid: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        28,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("cancel-death-by-void", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(28).updateName("§cVoid: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-death-by-void"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD") ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : Material.getMaterial("SKULL_ITEM").getNewData((byte)3),
                        "§cEnter portal: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        29,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("cancel-portal", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(29).updateName("§cEnter portal: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-portal"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD") ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : Material.getMaterial("SKULL_ITEM").getNewData((byte)3),
                        "§cAll with Items: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        30,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("drop-items", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                            configManager.getConfig(ConfigType.CONFIG).set("pickup-items", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(30).updateName("§cAll with Items: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("pickup-items") && configManager.getConfig(ConfigType.CONFIG).getBoolean("drop-items")));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("PLAYER_HEAD") ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : Material.getMaterial("SKULL_ITEM").getNewData((byte)3),
                        "§cAll with Blocks: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        31,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("break-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                            configManager.getConfig(ConfigType.CONFIG).set("place-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                            configManager.getConfig(ConfigType.CONFIG).set("interact-with-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(31).updateName("§cAll with Blocks: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("break-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("place-blocks") && configManager.getConfig(ConfigType.CONFIG).getBoolean("interact-with-blocks")));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("GRASS_BLOCK") ? Material.getMaterial("GRASS_BLOCK") : Material.getMaterial("GRASS"),
                        "§cSpawn mobs: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        37,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("spawn-mobs", !configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(37).updateName("§cSpawn mobs: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("spawn-mobs"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("GRASS_BLOCK") ? Material.getMaterial("GRASS_BLOCK") : Material.getMaterial("GRASS"),
                        "§cWeather and Day cycles: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        38,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("day-cycle", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                            configManager.getConfig(ConfigType.CONFIG).set("weather-cycle", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(38).updateName("§cWeather and Day cycles: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("weather-cycle") && configManager.getConfig(ConfigType.CONFIG).getBoolean("day-cycle")));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("GRASS_BLOCK") ? Material.getMaterial("GRASS_BLOCK") : Material.getMaterial("GRASS"),
                        "§cLeaves decay: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        39,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("leaves-decay", !configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(39).updateName("§cLeaves decay: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("leaves-decay"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("GRASS_BLOCK") ? Material.getMaterial("GRASS_BLOCK") : Material.getMaterial("GRASS"),
                        "§cBurn: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        40,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("burn-blocks", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                            configManager.getConfig(ConfigType.CONFIG).set("block-spread", !(configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(40).updateName("§cBurn: " + (configManager.getConfig(ConfigType.CONFIG).getBoolean("block-spread") && configManager.getConfig(ConfigType.CONFIG).getBoolean("burn-blocks")));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Arrays.stream(Material.values())
                                .map(Material::name)
                                .collect(Collectors.toList())
                                .contains("GRASS_BLOCK") ? Material.getMaterial("GRASS_BLOCK") : Material.getMaterial("GRASS"),
                        "§cExplosions: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"),
                        Arrays.asList("&aClick to change!".split(" /// ")),
                        1,
                        41,
                        () -> {
                            configManager.getConfig(ConfigType.CONFIG).set("cancel-explosions", !configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"));
                            configManager.saveConfig(ConfigType.CONFIG);
                            configManager.reloadConfig(ConfigType.CONFIG);
                            options.getItem(41).updateName("§cExplosions: " + configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-explosions"));
                            player.sendMessage(ChatColor.GREEN + "Changed!");
                        }
                ),
                new InventoryItem(
                        Material.BARRIER,
                        "§cBack",
                        new ArrayList<>(),
                        1,
                        53,
                        () -> {
                            player.closeInventory();
                            menus.get("Initial Page").open(player);
                        }
                )

        ));


        // Helpers

        BasicMenu collaborators = new BasicMenu("&8Collaborators", 27, player);

        menus.put("Collaborators", collaborators.addItems(
                new SkullInventoryItem(
                        "§bDaviXG7",
                        Collections.singletonList("&aCreator of all plugin!"),
                        1,
                        12,
                        null,
                        "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY0OWZjZmFmNTU5OTA0ZWM4MzBiY2QyNGFjN2EyMzFkM2ViZTcxOGQ0MDdmNTczYmQzNGFkZjYyMjEwMWI0NCJ9fX0="
                ),
                new SkullInventoryItem(
                        "§bBultzzXG7",
                        Collections.singletonList("&aVideo Helper and Beta tester!"),
                        1,
                        13,
                        null,
                        "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjMDFjNGJjYWU3NjA2ZjJhZTcwOWZmNjU3YzFjZjRiNGEwNmMxZmZhN2NmYzI0ZWExZDBmYjhkMDRlNTY5YiJ9fX0="
                ),
                new SkullInventoryItem(
                        "§bSadness",
                        Collections.singletonList("&aVideo Helper and Beta tester!"),
                        1,
                        14,
                        null,
                        "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg4ZjU5OWFkYjBlNmM4MjY1Nzg4ZTE1ZTk3NjU0NjI0ZTA5MjgxN2Y4MDU1ZjNkM2IxNjhkYzM2YWFhZTBhMiJ9fX0="
                ),

                new InventoryItem(
                        Material.BARRIER,
                        "§cBack",
                        new ArrayList<>(),
                        1,
                        18,
                        () -> {
                            player.closeInventory();
                            menus.get("Initial Page").open(player);
                        }
                )
        ));


        menus.get("Initial Page").open(player);

    }


    private static ItemStack getSelectorGuide() {

        ItemStack stack = new ItemStack(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITTEN_BOOK") ? Material.valueOf("WRITTEN_BOOK") : Material.valueOf("BOOK_AND_QUILL"));

        BookMeta meta = (BookMeta) stack.getItemMeta();

        meta.addPage("Guide on how to use Selectors\n\nSelectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file §bselectors.yml§r\n\n->");
        meta.addPage("In the part selectors -> Items -> and there where you create your items\n\nIn the Items part you can create another part with any name and should put the following things on the next page");
        meta.addPage("Item description\n\nitem: Where to place the Item's material\n\nname: Item name\n\nlore: Item Description\n\ngrow: Enchantment or not\n\n->");
        meta.addPage("amount: amount of the item\n\nslot: Inventory slot where the item is in inventory\n\nactions: All actions will be performed when the player clicks");
        meta.addPage("In the cooldown part is the item refresh time to be given to the player at all times");

        meta.setTitle("§aSelectors Guide");
        meta.setAuthor("DaviXG7");

        meta.setDisplayName("§aSelectors Guide");

        stack.setItemMeta(meta);

        return stack;
    }

    private static ItemStack getCommandsGuide() {

        ItemStack stack = new ItemStack(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("WRITTEN_BOOK") ? Material.valueOf("WRITTEN_BOOK") : Material.valueOf("BOOK_AND_QUILL"));

        BookMeta meta = (BookMeta) stack.getItemMeta();

        meta.addPage("Guide on how to Create Commands\n\nYou Cannot put an argument in the command like: test arg1/arg3 arg2 ->");
        meta.addPage("In §bconfig.yml §rthe part custom-commands you can create your items\n\nWithin this section, you can create another section where the name of the command will be and to create one is very simple, just put the following items in the section ->");
        meta.addPage("description -> The description of the command\n" +
                "\n" +
                "aliases -> Other Ways in Which You Can Use the\n" +
                "\n" +
                "permission -> The permission you need to use the\n" +
                "\n" +
                "actions -> The actions of the command");

        meta.setTitle("§aSelectors Guide");
        meta.setAuthor("DaviXG7");

        meta.setDisplayName("§aCreate commands Guide");

        stack.setItemMeta(meta);

        return stack;
    }
}
