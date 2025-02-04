package com.xg7plugins.xg7lobby.actions;

import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.libs.xg7menus.menus.gui.Menu;
import com.xg7plugins.libs.xg7menus.menus.holders.MenuHolder;
import com.xg7plugins.libs.xg7menus.menus.holders.PlayerMenuHolder;
import com.xg7plugins.utils.Parser;
import com.xg7plugins.utils.location.Location;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.menu.LobbyItem;
import com.xg7plugins.xg7lobby.inventories.menu.LobbyMenu;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
public enum ActionType {

    MESSAGE(false, (player, args) -> Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().send(player)),
    COMMAND(false, (player, args) -> player.performCommand(Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText())),
    CONSOLE(false, (player, args) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText())),
    TITLE(true,(player, args) -> {
        if (args.length == 1) {
            player.sendTitle(Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText(), "");
            return;
        }
        if (args.length == 2) {
            player.sendTitle(Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText(), Text.detectLangOrText(XG7Lobby.getInstance(),player,args[1]).join().getText());
            return;
        }
        if (args.length == 5) {
            player.sendTitle(args[0].equals("_") ? "" : Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText(), args[1].equals("_") ? "" : Text.detectLangOrText(XG7Lobby.getInstance(),player,args[1]).join().getText(), Parser.INTEGER.convert(args[2]), Parser.INTEGER.convert(args[3]), Parser.INTEGER.convert(args[4]));
            return;
        }

        throw new ActionException("TITLE", "Incorrectly amount of args: " + args.length + ". The right way to use is [TITLE] title, Optional:[subtitle], Optional:[<fade, fade in, fade out>].\n" +
                "Use \"_\" to remove the title in the second case or subtitle or title in the last case.");
    }),
    EFFECT(true,(player, args) -> {

        try {
            switch (args.length) {
                case 3:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2])));
                    break;
                case 4:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2]), Parser.BOOLEAN.convert(args[3])));
                    break;
                case 5:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2]), Parser.BOOLEAN.convert(args[3]), Parser.BOOLEAN.convert(args[4])));
                    break;
                default:
                    throw new ActionException("EFFECT", "Incorrectly amount of args: " + args.length + ". The right way to use is [EFFECT] potion, duration, amplifier, Optional:[ambient, Optional:[particles, Optional:[icon]]].");

            }
        } catch (Throwable e) {
            throw new ActionException("EFFECT", "Unable to convert text in values, check if the values are correct. potion: TEXT (ENUM_NAME), duration: INTEGER, amplifier: INTEGER, ambient: BOOLEAN, particles: BOOLEAN, icon: BOOLEAN");
        }

    }),
    TP(true,(player, args) ->{
        try {
            switch (args.length) {
                case 4:
                    Location location = Location.of(args[0], Parser.DOUBLE.convert(args[1]), Parser.DOUBLE.convert(args[2]), Parser.DOUBLE.convert(args[3]));
                    player.teleport(location.getBukkitLocation());
                    break;
                case 6:
                    Location location2 = Location.of(args[0], Parser.DOUBLE.convert(args[1]), Parser.DOUBLE.convert(args[2]), Parser.DOUBLE.convert(args[3]), Parser.FLOAT.convert(args[4]), Parser.FLOAT.convert(args[5]));
                    player.teleport(location2.getBukkitLocation());
                    break;
                default:
                    throw new ActionException("TP", "Incorrectly amount of args: " + args.length + ". The right way to use is [TP] world, x, y, z, Optional:[yaw,pitch].");
            }
        } catch (Throwable e) {
            throw new ActionException("TP", "Unable to convert text in values, check if the values are correct. world: TEXT: (WORLD NAME), x: DECIMAL, y: DECIMAL, z: DECIMAL, yaw: DECIMAL, pitch: DECIMAL");
        }
    }),
    BROADCAST(false,(player, args) -> Bukkit.broadcastMessage(Text.detectLangOrText(XG7Lobby.getInstance(),player,args[0]).join().getText())),
    SUMMON(true,(player, args) -> player.getWorld().spawnEntity(player.getLocation(), XEntityType.valueOf(args[0].toUpperCase()).get())),
    SOUND(true,(player, args) -> {
        try {
            switch (args.length) {
                case 1:
                    player.playSound(player.getLocation(), XSound.of(args[0]).orElse(XSound.MUSIC_DISC_13).get(), 1, 1);
                    return;
                case 2:
                    player.playSound(player.getLocation(), XSound.of(args[0]).orElse(XSound.MUSIC_DISC_13).get(), Parser.FLOAT.convert(args[1]), 1);
                    return;
                case 3:
                    player.playSound(player.getLocation(), XSound.of(args[0]).orElse(XSound.MUSIC_DISC_13).get(), Parser.FLOAT.convert(args[1]), Parser.FLOAT.convert(args[2]));
                    return;
                default:
                    throw new ActionException("SOUND", "Incorrectly amount of args: " + args.length + ". The right way to use is [SOUND] sound, Optional:[volume, Optional:[pitch]].");
            }
        } catch (Throwable e) {
            throw new ActionException("SOUND", "Unable to convert text in values, check if the values are correct. sound: TEXT (ENUM NAME), volume: DECIMAL, pitch: DECIMAL");
        }

    }),
    PARTICLE(true,(player, args) -> {
        try {
            switch (args.length) {
                case 1:
                    player.spawnParticle(XParticle.valueOf(args[0].toUpperCase()).get(), player.getLocation(),1);
                    return;
                case 2:
                    player.spawnParticle(XParticle.valueOf(args[0].toUpperCase()).get(), player.getLocation(), Parser.INTEGER.convert(args[1]));
                    return;
                case 5:
                    player.spawnParticle(XParticle.valueOf(args[0].toUpperCase()).get(), player.getLocation(), Parser.INTEGER.convert(args[1]), Parser.DOUBLE.convert(args[2]), Parser.DOUBLE.convert(args[3]), Parser.DOUBLE.convert(args[4]));
                    return;
                default:
                    throw new ActionException("PARTICLE", "Incorrectly amount of args: " + args.length + ". The right way to use is [PARTICLE] particle, Optional:[amount, Optional:[offset x, offset y, offset z]].");
            }
        } catch (Throwable e) {
            throw new ActionException("PARTICLE", "Unable to convert text in values, check if the values are correct. particle: TEXT (ENUM NAME), amount: INTEGER, offset x: DECIMAL, offset y: DECIMAL, offset z: DECIMAL");
        }

    }),
    FIREWORK(true,(player, args) -> {
        try {
            if (args.length != 6) {
                throw new ActionException("FIREWORK", "Incorrectly amount of args: " + args.length + ". The right way to use is [FIREWORK] type, color, colorfade, trail, flicker, power.");
            }
            Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), XEntityType.FIREWORK_ROCKET.get());

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder.with(FireworkEffect.Type.valueOf(args[0]));
            builder.withColor(Color.fromRGB(Integer.parseInt(args[1].replace("#", ""), 16)));
            builder.withFade(Color.fromRGB(Integer.parseInt(args[2].replace("#", ""), 16)));
            builder.trail(Parser.BOOLEAN.convert(args[3]));
            builder.flicker(Parser.BOOLEAN.convert(args[4]));

            FireworkEffect effect = builder.build();
            fireworkMeta.addEffect(effect);
            fireworkMeta.setPower(Parser.INTEGER.convert(args[5]));

            firework.setFireworkMeta(fireworkMeta);

            try {
                firework.detonate();
            } catch (Exception ignored) {}
        } catch (Throwable e) {
            throw new ActionException("FIREWORK", "Unable to convert text in values, check if the values are correct. type: TEXT: (ENUM NAME), color: HEXDECIMAL, colorfade: HEXDECIMAL, trail: BOOLEAN, flicker: BOOLEAN, power: INTEGER");
        }

    }),
    CLEAR_CHAT(false,(player, args) -> IntStream.range(0, 100).mapToObj(i -> "").forEach(player::sendMessage)),
    OPEN(true, (player, args) -> {

        BaseMenu menu = XG7Lobby.getInstance().getInventoryManager().getInventory(args[0]);

        if (menu == null) {
            throw new ActionException("OPEN", "The menu with id: " + args[0] + " doesn't exist.");
        }

        menu.open(player);

    }),
    CLOSE(false, (player, args) -> {
        player.closeInventory();
    }),
    SWAP(true, (player, args) -> {

        if (args.length != 3) {
            throw new ActionException("SWAP", "Incorrectly amount of args: " + args.length + ". The right way to use is [SWAP] menuId, slot, itemPath.");
        }

        BaseMenu menu = XG7Lobby.getInstance().getInventoryManager().getInventory(args[0]);

        if (menu == null) {
            throw new ActionException("SWAP", "The menu with id: " + args[0] + " doesn't exist.");
        }

        int slot = Parser.INTEGER.convert(args[1]);

        if (menu instanceof Menu) {

            LobbyMenu lobbyMenu = (LobbyMenu) menu;

            LobbyItem item = lobbyMenu.getItems().get(args[2]);

            MenuHolder holder = (MenuHolder) player.getOpenInventory().getTopInventory().getHolder();

            if (item == null) {
                throw new ActionException("SWAP", "The item with path: " + args[1] + " doesn't exist in the menu with id: " + args[0]);
            }

            Menu.update(player, item.getItem().slot(slot), holder);
            return;
        }

        if (menu instanceof LobbySelector) {
            LobbySelector playerMenu = (LobbySelector) menu;

            LobbyItem item = playerMenu.getItems().get(args[2]);

            PlayerMenuHolder holder = XG7Plugins.getInstance().getMenuManager().getPlayerMenusMap().get(player.getUniqueId());

            if (item == null) {
                throw new ActionException("SWAP", "The item with path: " + args[1] + " doesn't exist in the menu with id: " + args[0]);
            }

            Menu.update(player, item.getItem().slot(slot), holder);
        }
    }),
    REFRESH(false, (player, args) -> {
        MenuHolder holder = (MenuHolder) player.getOpenInventory().getTopInventory().getHolder();
        Menu.refresh(holder);
    }),
    HIDE_PLAYERS(false, (player, args) -> {
        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.setPlayerHiding(true);
    }),
    SHOW_PLAYERS(false, (player, args) -> {
        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();
        lobbyPlayer.setPlayerHiding(false);
    }),
    CLEAR_INVENTORY(false, (player, args) -> {
        player.getInventory().clear();
    }),
    PVP(false, (player, args) -> {
        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(player)) XG7Lobby.getInstance().getGlobalPVPManager().addPlayerToPVP(player);
        else XG7Lobby.getInstance().getGlobalPVPManager().removePlayerFromPVP(player);
    }),
    EQUIP(true, (player, args) -> {
        Slot slot = Slot.valueOf(args[0].toUpperCase());
        XMaterial material = XMaterial.valueOf(args[1]);
        switch (slot) {
            case HELMET:
                player.getInventory().setHelmet(material.parseItem());
                break;
            case CHESTPLATE:
                player.getInventory().setChestplate(material.parseItem());
                break;
            case LEGGINGS:
                player.getInventory().setLeggings(material.parseItem());
                break;
            case BOOTS:
                player.getInventory().setBoots(material.parseItem());
                break;
            case OFFHAND:
                if (XG7Plugins.getMinecraftVersion() < 9) {
                    System.err.println("The offhand slot is only available in 1.9 or higher.");
                    return;
                }
                player.getInventory().setItemInOffHand(material.parseItem());
                break;
        }
    });

    private final boolean needArgs;
    private final BiConsumer<Player, String[]> action;

    public void execute(Player player, String[] args) {
        action.accept(player, args);
    }

    public static ActionType extractType(String s) {
        s = s.replace("[", "").replace("]", "");
        for (ActionType type : values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        return null;
    }

    private enum Slot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        OFFHAND
    }



}
