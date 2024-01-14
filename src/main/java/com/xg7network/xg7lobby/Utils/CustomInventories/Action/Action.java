package com.xg7network.xg7lobby.Utils.CustomInventories.Action;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.CustomInventories.Inventory;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Action {

    private ActionType type;
    private Player player;
    private String action;
    private boolean valid;
    
    public Action(Player player, String action) {
        if (action == null || action.isEmpty()) {
            valid = false;
            return;
        }
        try {
            ActionType.valueOf(action);
            valid = true;
        } catch (Exception ignored) {
            valid = false;
        }
        try {
            if (!valid) ActionType.valueOf(action.substring(0, action.indexOf(":")));
        } catch (Exception ignored) {
            valid = false;
            return;
        }

        valid = true;
        for (ActionType actionType : ActionType.values()) {

            if (action.contains(": ")) {
                if (actionType.equals(ActionType.valueOf(action.substring(0, action.indexOf(":"))))) {
                    this.type = actionType;
                    this.player = player;
                    this.action = TextUtil.get(action, player).replace(actionType + ": ", "");

                    return;
                }
            } else {
                this.type = ActionType.valueOf(action);
                this.player = player;
                this.action = "1 function action!";
                return;
            }
        }
    }

    public void execute() {
        if (valid) {
            String toUse = TextUtil.get(action.replace("PLAYER", player.getName()), player);
            if (permission()) {

                switch (type) {

                    case SOUND:

                        PluginUtil.playSound(player, toUse);

                        return;

                    case MESSAGE:

                        TextUtil.send(toUse, player);

                        return;

                    case TITLE:

                        String[] title = toUse.split(", ");

                        if (title.length == 1) {
                            player.sendTitle(title[0].replace("&", "§"), "");
                        } else if (title.length == 4) {
                            player.sendTitle(title[0].replace("&", "§"), "", Integer.parseInt(title[1]), Integer.parseInt(title[2]), Integer.parseInt(title[3]));
                        }

                        return;

                    case SUBTITLE:

                        String[] subtitle = toUse.split(", ");

                        if (subtitle.length == 1) {
                            player.sendTitle("", subtitle[0].replace("&", "§"));
                        } else if (subtitle.length == 4) {
                            player.sendTitle("", subtitle[0].replace("&", "§"), Integer.parseInt(subtitle[1]), Integer.parseInt(subtitle[2]), Integer.parseInt(subtitle[3]));
                        }

                        return;

                    case TITSUBTIT:

                        String[] tstitle = toUse.split(", ");

                        String[] tit = tstitle[0].split(" // ");

                        if (tstitle.length == 1) {
                            player.sendTitle(tit[0].replace("&", "§"), tit[1].replace("&", "§"));
                        } else if (tstitle.length == 4) {
                            player.sendTitle(tit[0].replace("&", "§"), tit[1].replace("&", "§"), Integer.parseInt(tstitle[1]), Integer.parseInt(tstitle[2]), Integer.parseInt(tstitle[3]));
                        }

                        return;

                    case OPEN:

                        player.closeInventory();

                        for (String s : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("inventories").getKeys(false)) {
                            if (configManager.getConfig(ConfigType.SELECTORS).getInt("inventories." + s + ".id") == Integer.valueOf(action)) {
                                Inventory inv = new Inventory("inventories." + s, player);
                                if (inv.getInv() != null) inv.open();
                            }
                        }

                        return;

                    case CLOSE:

                        player.closeInventory();

                        return;

                    case GAMEMODE:

                        player.setGameMode(GameMode.valueOf(toUse.toUpperCase()));

                        return;

                    case TP:

                        String[] location = toUse.split(", ");
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

                        Bukkit.broadcastMessage(toUse.replace("&", "§"));

                        return;

                    case SUMMON:

                        player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(toUse.toUpperCase()));

                        return;

                    case EFFECT:

                        player.addPotionEffect(PluginUtil.getEffect(toUse));

                        return;

                    case COMMAND:

                        player.performCommand(toUse);

                        return;

                    case CONSOLE:

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), toUse);

                        return;

                    case FLY:

                        player.setFlying(!player.isFlying());

                        return;

                    case ACTIONBAR:

                        TextUtil.sendActionBar(toUse, player);

                        return;

                    case SWAP:

                        String[] item = toUse.split(", ");

                        SelectorItem selectorItem = new SelectorItem("selectors.items." + item[1], player);
                        if (item[0].startsWith("currentslot=")) {
                            item[0] = item[0].replace("currentslot=", "");
                            for (int i = 0; i < 9; i++) {
                                SelectorItem item2 = new SelectorItem("selectors.items." + item[0], player);
                                if (player.getInventory().getItem(i) != null)
                                    if (player.getInventory().getItem(i).isSimilar(item2.getItemStack()))
                                        player.getInventory().setItem(i, selectorItem.getItemStack());
                            }
                        } else {
                            selectorItem.setSlot(Integer.parseInt(item[0]));
                            player.getInventory().setItem(selectorItem.getSlot(), selectorItem.getItemStack());
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


    }


    boolean permission() {
        if (valid) {

            if (action.contains(" PERMISSION: ")) {
                String[] perm = action.split(" PERMISSION: ");
                this.action = perm[0];
                return player.hasPermission(perm[1]);
            } else if (action.contains(" !PERMISSION: ")) {
                String[] perm = action.split(" !PERMISSION: ");
                this.action = perm[0];
                return !player.hasPermission(perm[1]);
            }
        }

        return true;

    }


}
