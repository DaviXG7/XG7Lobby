package com.xg7plugins.xg7lobby.actions;

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

        throw new ActionException(this, "Incorrectly amount of args: " + args.length + ". The right way to use is [TITLE] title, Optional:[subtitle], Optional:[<fade, fade in, fade out>].\n" +
                "Use \"_\" to remove the title in the first case or subtitle in the last case.");
    }),
    EFFECT((player, args) -> {

        switch (args.length) {
            case 2
                player.addPotionEffect(new PotionEffect(XPotion.valueOf("name").getPotionEffectType()), Parser.BOOLEAN.convert(args[1]));
            case 3:
                player.addPotionEffect(new PotionEffect(XPotion.valueOf("name").getPotionEffectType()), Parser.INTEGER.convert(args[1]), Parser.INTEGER.convert(args[2]));
            case 4:

        }

    });


    private BiConsumer<Player, String[]> action;

    public enum Parser {
        INTEGER(Integer::parseInt),
        STRING(s -> s),
        BOOLEAN(Boolean::parseBoolean),
        LONG(Long::parseLong),
        DOUBLE(Double::parseDouble),
        FLOAT(Float::parseFloat),
        SHORT(Short::parseShort),
        BYTE(Byte::parseByte),
        CHAR(s -> s.charAt(0));

        private final Function<String, ?> converter;

        Parser(Function<String, ?> converter) {
            this.converter = converter;
        }

        public <T> T convert(String value) {
            return (T) converter.apply(value);
        }
    }



}
