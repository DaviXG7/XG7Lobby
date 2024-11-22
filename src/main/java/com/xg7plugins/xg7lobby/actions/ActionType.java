package com.xg7plugins.xg7lobby.actions;

import com.xg7plugins.libs.xg7menus.XSeries.XPotion;
import com.xg7plugins.utils.Location;
import com.xg7plugins.utils.Parser;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum ActionType {

    MESSAGE((player, args) -> Text.format(String.join(" ", args), XG7Lobby.getInstance()).send(player)),
    COMMAND((player, args) -> player.performCommand(String.join(" ", args))),
    CONSOLE((player, args) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.join(" ", args))),
    TITLE((player, args) -> {
        if (args.length == 1) {
            player.sendTitle(args[0].equals("_") ? "" : Text.format(args[0] , XG7Lobby.getInstance()).getWithPlaceholders(player), "");
            return;
        }
        if (args.length == 2) {
            player.sendTitle(Text.format(args[0], XG7Lobby.getInstance()).getWithPlaceholders(player), Text.format(args[1], XG7Lobby.getInstance()).getWithPlaceholders(player));
        }
        if (args.length == 5) {
            player.sendTitle(Text.format(args[0], XG7Lobby.getInstance()).getWithPlaceholders(player), args[1].equals("_") ? "" : Text.format(args[1], XG7Lobby.getInstance()).getWithPlaceholders(player), Parser.INTEGER.convert(args[2]), Parser.INTEGER.convert(args[3]), Parser.INTEGER.convert(args[4]));
        }

        throw new ActionException("EFFECT", "Incorrectly amount of args: " + args.length + ". The right way to use is [TITLE] title, Optional:[subtitle], Optional:[<fade, fade in, fade out>].\n" +
                "Use \"_\" to remove the title in the first case or subtitle in the last case.");
    }),
    EFFECT((player, args) -> {

        try {
            switch (args.length) {
                case 3:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2])));
                case 4:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2]), Parser.BOOLEAN.convert(args[3])));
                case 5:
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(args[0]).getPotionEffectType(), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2]), Parser.BOOLEAN.convert(args[3]), Parser.BOOLEAN.convert(args[4])));
                default:
                    throw new ActionException("EFFECT", "Incorrectly amount of args: " + args.length + ". The right way to use is [EFFECT] potion, duration, amplifier, Optional:[ambient, Optional:[particles, Optional:[icon]]].");

            }
        } catch (Throwable e) {
            throw new ActionException("EFFECT", "Unable to convert text in values, check if the values are correct. potion: TEXT (ENUM_NAME), duration: INTEGER, amplifier: INTEGER, ambient: BOOLEAN, particles: BOOLEAN, icon: BOOLEAN");
        }

    }),
    TP((player, args) ->{
        switch (args.length) {
            case 4:
                Location
        }
    });


    private BiConsumer<Player, String[]> action;



}
