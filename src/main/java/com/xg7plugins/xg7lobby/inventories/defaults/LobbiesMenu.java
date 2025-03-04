package com.xg7plugins.xg7lobby.inventories.defaults;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.modules.xg7menus.Slot;
import com.xg7plugins.modules.xg7menus.events.ClickEvent;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.modules.xg7menus.menus.gui.Menu;
import com.xg7plugins.modules.xg7menus.menus.gui.PageMenu;
import com.xg7plugins.modules.xg7menus.menus.holders.PageMenuHolder;
import com.xg7plugins.utils.Pair;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LobbiesMenu extends PageMenu {
    public LobbiesMenu() {
        super(XG7Lobby.getInstance(), "lobbies-menu", "lang:[lobbies-menu.title]", 54, Slot.of(2,2), Slot.of(5,8));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {
        return Arrays.asList(
                Item.from(XMaterial.ARROW).name("lang:[lobbies-menu.go-back]").slot(45),
                Item.from(XMaterial.matchXMaterial("BARRIER").orElse(XMaterial.OAK_DOOR)).name("lang:[lobbies-menu.close]").slot(49),
                Item.from(XMaterial.ARROW).name("lang:[lobbies-menu.go-next]").slot(53)
        );
    }

    @Override
    public List<Item> pagedItems(Player player) {

        List<Item> pagedItems = new ArrayList<>();

        List<LobbyLocation> lobbies = XG7Lobby.getInstance().getLobbyManager().getDao().getAll().join();

        lobbies.forEach(lobby -> {
            Item lobbyItem = Item.from(XMaterial.NETHER_STAR);

            lobbyItem.name("lang:[lobbies-menu.lobby-item.name]");

            List<String> lore = new ArrayList<>();

            lore.add("lang:[lobbies-menu.lobby-item.server]");
            lore.add("lang:[lobbies-menu.lobby-item.location]");
            if (!player.hasPermission("xg7lobby.command.lobby.delete")) lore.add("lang:[lobby-menu.lobby-item.click-to-tp]");
            else {
                lore.add("lang:[lobbies-menu.lobby-item.for-admins.click-to-tp]");
                lore.add("lang:[lobbies-menu.lobby-item.for-admins.click-to-remove]");
            }

            lobbyItem.lore(lore);

            lobbyItem.setBuildPlaceholders(
                    Pair.of("server", lobby.getServer().getName()),
                    Pair.of("id", lobby.getID()),
                    Pair.of("x", String.format("%.2f", lobby.getLocation().getX())),
                    Pair.of("y", String.format("%.2f", lobby.getLocation().getY())),
                    Pair.of("z", String.format("%.2f", lobby.getLocation().getZ())),
                    Pair.of("world", lobby.getLocation().getWorld().getName()),
                    Pair.of("yaw", String.format("%.2f", lobby.getLocation().getYaw())),
                    Pair.of("pitch", String.format("%.2f", lobby.getLocation().getPitch())
                    )
            );

            lobbyItem.setNBTTag("lobby-id", lobby.getID());

            pagedItems.add(lobbyItem);
        });

        return pagedItems;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getClickedItem().isAir()) return;

        Player player = (Player) event.getWhoClicked();

        switch (event.getClickedSlot()) {
            case 45:
                ((PageMenuHolder)event.getInventoryHolder()).previousPage();
                break;
            case 49:
                player.closeInventory();
                break;
            case 53:
                ((PageMenuHolder)event.getInventoryHolder()).nextPage();
                break;
            default:

                Item item = event.getClickedItem();

                if (!item.getTag("lobby-id",String.class).isPresent()) return;

                String lobbyId = item.getTag("lobby-id",String.class).get();

                XG7Lobby.getInstance().getLobbyManager().getLobby(lobbyId).thenAccept(lobbyLocation -> {
                    if (lobbyLocation == null) return;

                    if (!player.hasPermission("xg7lobby.command.lobby-delete")) {
                        XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> {
                            player.closeInventory();
                            lobbyLocation.teleport(player);
                        });
                        return;
                    }

                    if (event.getClickAction().isLeftClick()) {
                        XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), () -> {
                            player.closeInventory();
                            lobbyLocation.teleport(player);
                        });
                    }

                    if (!event.getClickAction().isRightClick()) return;

                    XG7Lobby.getInstance().getLobbyManager().getDao().deleteLobby(lobbyLocation)
                            .exceptionally(e -> {
                                e.printStackTrace();
                                return null;
                            })
                            .thenRun(() -> {
                        Text.fromLang(player, XG7Lobby.getInstance(), "lobby.delete.on-success").thenAccept(text -> text.replace("id", lobbyLocation.getID()).send(player));
                    });

                }).join();

        }
    }


}
