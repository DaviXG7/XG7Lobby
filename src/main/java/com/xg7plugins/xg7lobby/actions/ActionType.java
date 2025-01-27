package com.xg7plugins.xg7lobby.actions;

import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import com.xg7plugins.utils.Parser;
import com.xg7plugins.utils.location.Location;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

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
    OPEN(false, (player, args) -> {}),
    CLOSE(false, (player, args) -> {}),
    HIDE_PLAYERS(false, (player, args) -> {}),
    SHOW_PLAYERS(false, (player, args) -> {}),
    HIDE_CHAT(false, (player, args) -> {}),
    SHOW_CHAT(false, (player, args) -> {});

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



}
