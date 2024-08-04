package com.xg7plugins.xg7lobby.events.menuevents;

import com.xg7plugins.xg7lobby.utils.XSeries.XMaterial;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.commands.implcommands.HelpCommand;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7menus.api.events.MenuClickEvent;
import com.xg7plugins.xg7menus.api.events.MenuOpenEvent;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.ItemPages;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;

public class HelpMenuEvent implements Event {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onOpen(MenuOpenEvent event) {
        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {;
        if (event.getMenu().getId().equals("xg7lhelpinit")) {
            PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
            event.getMenu().updateInventory(event.getPlayer(),
                    new InventoryItem.SkullInventoryItem(
                            "§aWelcome to help §b" + event.getPlayer().getName(),
                            Arrays.asList(
                                    "§aPlayer Informations",
                                    "§9Hiding players: §r" + data.isPlayerHiding(),
                                    "§9Muted: §r" + data.isMuted(),
                                    "§9Build enabled: §r" + data.isBuildEnabled(),
                                    "§9PVP enabled: §r" + data.isPVPEnabled(),
                                    "§9Is flying: §r" + data.isFlying(),
                                    "§6XG7Lobby version: 1.0-RETURN"
                            ),
                            1, 13
                    ).setOwner(event.getPlayer().getName(), Bukkit.getOnlineMode()));
        }
    }
        , 1L);
    }

    @EventHandler
    public void onMenuClick(MenuClickEvent event) {
        Player player = event.getPlayer();

        switch (event.getMenu().getId()) {
            case "xg7lhelpinit":

                switch (event.getSlot()) {
                    case 29:
                        HelpCommand.getPages().openMenu("xg7lhelpcommands", player);
                        return;
                    case 30:
                        HelpCommand.getPages().openMenu("xg7lhelpactions", player);
                        return;
                    case 31:
                        player.performCommand("xg7lobbysetlobby");
                        player.closeInventory();
                        HelpCommand.init();
                        HelpCommand.getPages().openMenu("xg7lhelpinit", player);
                        return;
                    case 32:
                        player.closeInventory();
                        openBook(getSelectorGuide(), player, true);
                        return;
                    case 33:
                        player.closeInventory();
                        openBook(getCommandsGuide(), player, false);
                        return;
                    case 53:
                        HelpCommand.getPages().openMenu("xg7lhelpcreators", player);
                        return;
                    case 45:
                        player.closeInventory();
                        return;
                }

                break;
            case "xg7lhelpcreators":
                if (event.getSlot() == 18) {
                    HelpCommand.getPages().openMenu("xg7lhelpinit", player);
                    return;
                }
                break;
            case "xg7lhelpcommands":
            case "xg7lhelpactions":
                switch (event.getSlot()) {
                    case 52:
                        ((ItemPages) event.getMenu()).previusPage(player);
                        return;
                    case 53:
                        ((ItemPages) event.getMenu()).nextPage(player);
                        return;
                    case 45:
                        HelpCommand.getPages().openMenu("xg7lhelpinit", player);
                        return;
                }
                break;


        }

    }

    @SneakyThrows
    public void openBook(ItemStack book, Player player, boolean isSelector) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 14) {
            player.openBook(book);
            return;
        }
        if (isSelector) {
            player.sendMessage(ChatColor.GREEN + "Selector guide!");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "Guide on how to use Selectors\n");
            player.sendMessage(ChatColor.WHITE + "Selectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file §bselectors.yml");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "In the part selectors -> Items -> and there where you create your items\n\nIn the Items part you can create another part with any name and should put the following things on the next message");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "Item description\n\nmaterial -> Where to place the Item's material\n\nname -> Item name\n\nlore -> Item Description\n\ngrow -> Enchantment or not\n\namount -> amount of the item\n\nslot -> Inventory slot where the item is\n\ncustom-model-data (+1.9) -> Is the custom model of the item\n\nitem-flags -> The flags of the item");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "To execute actions in slot go to actions and put slot-(slot) and put the list of actions");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "In the cooldown part is the item refresh time to be given to the player at all times");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            return;
        }
            player.sendMessage(ChatColor.GREEN + "Custom commnads guide!");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
            player.sendMessage(ChatColor.WHITE + "Guide on how to Create Commands\n\n");
            player.sendMessage(ChatColor.RED + "You Cannot put an argument in the command like: \"/test arg1/arg2 arg3\"\n\n");
            player.sendMessage(ChatColor.WHITE + "In part custom-commands on §bcommands.yml§r you can create your items\n\nWithin this section, you can create another section where the name of the command will be and to create one is very simple, just put the following items in the section:\n\n");
            player.sendMessage(ChatColor.WHITE + "description -> The description of the command\n\naliases -> Other Ways in Which You Can Use the\n\npermission -> The permission you need to use the\n\nactions -> The actions of the command");
            player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");

    }

    private static ItemStack getSelectorGuide() {
        ItemStack stack = XMaterial.WRITTEN_BOOK.parseItem();
        BookMeta meta = (BookMeta)stack.getItemMeta();
        meta.addPage("Guide on how to use Selectors\n\nSelectors are the items on the hotbar that when you click, do something\n\nTo create the items you need to go to the file \n\n->");
        meta.addPage("In the part selectors -> Items -> and there where you create your items\n\nIn the Items part you can create another part with any name and should put the following things on the next page");
        meta.addPage("Item description\n\nitem: Where to place the Item's material\n\nname: Item name\n\nlore: Item Description\n\ngrow: Enchantment or not\n\n->");
        meta.addPage("amount: amount of the item\n\nslot: Inventory slot where the item is in inventory\n\ncustom-model-data (+1.9): Is the custom model of the item\n\nitem-flags: The flags of the item\n\n->");
        meta.addPage("To execute actions in slot go to actions and put slot-(slot) and put the list of actions\n\nIn the cooldown part is the item refresh time to be given to the player at all times");
        meta.setTitle("Guide");
        meta.setAuthor("DaviXG7");
        meta.setDisplayName("Guide");
        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack getCommandsGuide() {
        ItemStack stack = XMaterial.WRITTEN_BOOK.parseItem();
        BookMeta meta = (BookMeta)stack.getItemMeta();
        meta.addPage("Guide on how to Create Commands\n\nYou Cannot put an argument in the command like: test arg1/arg3 arg2 ->");
        meta.addPage("In part custom-commands you can create your items\n\nWithin this section, you can create another section where the name of the command will be and to create one is very simple, just put the following items in the section ->");
        meta.addPage("description -> The description of the command\n\naliases -> Other Ways in Which You Can Use the\n\npermission -> The permission you need to use the\n\nactions -> The actions of the command");
        meta.setTitle("Guide");
        meta.setAuthor("DaviXG7");
        meta.setDisplayName("commands Guide");
        stack.setItemMeta(meta);
        return stack;
    }
}
