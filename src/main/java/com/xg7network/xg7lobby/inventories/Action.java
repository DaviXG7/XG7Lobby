package com.xg7network.xg7lobby.inventories;

import com.xg7network.xg7lobby.inventories.inventory.InventoryManager;
import com.xg7network.xg7lobby.inventories.selectors.SelectorManager;
import com.xg7network.xg7lobby.utils.Other.PluginUtil;
import com.xg7network.xg7lobby.utils.Text.TextUtil;
import com.xg7network.xg7menus.API.Inventory.Menus.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.PlayerSelector;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Action {
    private ActionType type;
    private String args;

    public Action(String s) {
        if (s == null || s.isEmpty()) return;
        for (ActionType type1 : ActionType.values()) {
            if (s.startsWith("[" + type1.name() + "]")) {
                this.type = type1;
                this.args = s.substring(("[" + type1.name() + "]").length());
                return;
            }
        }
    }

    public void execute(Player player) {

        if (args.contains("[PERMISSION]") && args.contains("[!PERMISSION]")) throw new IllegalArgumentException();
        if (!player.hasPermission(args.split(" [PERMISSION] ")[1])) return;
        if (player.hasPermission(args.split(" [!PERMISSION] ")[1])) return;

        args = args.substring(0, args.indexOf(" [!PERMISSION] ")).substring(0, args.indexOf(" [PERMISSION] "));

        switch (type) {

            case SOUND:

                PluginUtil.playSound(player, args);

                return;

            case MESSAGE:

                TextUtil.send(args, player);

                return;

            case TITLE:

                String[] title = args.split(", ");

                if (title.length == 1) player.sendTitle(TextUtil.get(title[0], player), "");
                else if (title.length == 4) player.sendTitle(TextUtil.get(title[0], player), "", Integer.parseInt(title[1]), Integer.parseInt(title[2]), Integer.parseInt(title[3]));

                return;

            case SUBTITLE:

                String[] subtitle = args.split(", ");

                if (subtitle.length == 1) player.sendTitle("", TextUtil.get(subtitle[0], player));
                else if (subtitle.length == 4) player.sendTitle("", TextUtil.get(subtitle[0], player), Integer.parseInt(subtitle[1]), Integer.parseInt(subtitle[2]), Integer.parseInt(subtitle[3]));


                return;

            case TITSUBTIT:

                String[] tArgs = args.split(", ");

                String[] titSubtit = tArgs[0].split(" // ");

                if (tArgs.length == 1) player.sendTitle(TextUtil.get(titSubtit[0], player), TextUtil.get(titSubtit[1], player));
                else if (tArgs.length == 4) player.sendTitle(TextUtil.get(titSubtit[0], player), TextUtil.get(titSubtit[1], player), Integer.parseInt(tArgs[1]), Integer.parseInt(tArgs[2]), Integer.parseInt(tArgs[3]));


                return;

            case OPEN:

                player.closeInventory();

                try {
                    InventoryManager.get(args, player).open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return;

            case CLOSE:

                player.closeInventory();

                return;

            case GAMEMODE:

                player.setGameMode(GameMode.valueOf(args.toUpperCase()));

                return;

            case TP:

                String[] location = args.split(", ");
                if (location.length == 4) {

                    World world = Bukkit.getWorld(location[0]);
                    double x = Double.parseDouble(location[1]);
                    double y = Double.parseDouble(location[2]);
                    double z = Double.parseDouble(location[3]);

                    player.teleport(new Location(world, x, y, z));

                } else if (location.length == 6) {
                    World world = Bukkit.getWorld(location[0]);
                    double x = Double.parseDouble(location[1]);
                    double y = Double.parseDouble(location[2]);
                    double z = Double.parseDouble(location[3]);
                    float yaw = Float.parseFloat(location[4]);
                    float pitch = Float.parseFloat(location[5]);

                    player.teleport(new Location(world, x, y, z, yaw, pitch));
                }

                return;

            case BROADCAST:

                Bukkit.broadcastMessage(args.replace("&", "§"));

                return;

            case SUMMON:

                player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(args.toUpperCase()));

                return;

            case EFFECT:

                player.addPotionEffect(PluginUtil.getEffect(args));

                return;

            case COMMAND:

                player.performCommand(args);

                return;

            case CONSOLE:

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args);

                return;

            case FLY:

                player.setFlying(!player.isFlying());

                return;

            case ACTIONBAR:

                TextUtil.sendActionBar(args, player);

                return;

            case SWAP:

                String[] item = args.split(", ");

                PlayerSelector selector = SelectorManager.getSelectors().get(player.getUniqueId());

                InventoryItem targetStoredItem = SelectorManager.getStoredItems().get(item[1]);

                if (targetStoredItem == null) {
                    Bukkit.getLogger().severe("The inventory path doesn't exists!");
                    return;
                }
                
                targetStoredItem.setPlaceholders(player);

                if (item[0].startsWith("currentslot=")) {
                    item[0] = item[0].replace("currentslot=", "");
                    InventoryItem currentItem = SelectorManager.getStoredItems().get(item[0]);

                    targetStoredItem.setSlot(currentItem.getSlot());

                    selector.updateItem(targetStoredItem);

                } else {
                    targetStoredItem.setSlot(Integer.parseInt(item[0]));
                    selector.updateItem(targetStoredItem);
                }

                return;

            case HIDE:

                for (Player target : Bukkit.getOnlinePlayers()) player.hidePlayer(target);
                PlayerData data = PlayersManager.getData(player.getUniqueId().toString());
                data.setPlayershide(true);
                PlayersManager.update(player.getUniqueId().toString(), data);


                return;

            case SHOW:

                for (Player target : Bukkit.getOnlinePlayers()) player.showPlayer(target);

                PlayerData data2 = PlayersManager.getData(player.getUniqueId().toString());
                data2.setPlayershide(false);
                PlayersManager.update(player.getUniqueId().toString(), data2);

        }






    }

}
