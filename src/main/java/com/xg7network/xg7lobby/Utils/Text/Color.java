package com.xg7network.xg7lobby.Utils.Text;

/*

    This class was made by DaviXG7 to make it
    easier to handle the plugin texts.

    The class is free to use if this text is
    copied into your plugin and your plugin is free.

    Thanks for reading/using my code <3

 */

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color {

    public static String translateHexColor(String text) {


        if (Integer.parseInt(Bukkit.getVersion().split("\\.")[1]) >= 16) {


            Pattern pattern = Pattern.compile("&#([a-fA-F0-9]{6})");

            for(Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
                String cor = text.substring(matcher.start(), matcher.end()).replace("HEX:", "");
                text = text.replace(cor, ChatColor.of(cor) + "");
            }
        }


        return text;
    }

}
