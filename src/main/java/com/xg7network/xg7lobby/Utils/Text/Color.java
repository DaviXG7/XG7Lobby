package com.xg7network.xg7lobby.Utils.Text;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color {

    static boolean have1_16 = false;

    public Color() {

        String[] partes = Bukkit.getVersion().split("\\.");
        if (partes.length >= 2) {
            int vers = Integer.parseInt(partes[1]);
            have1_16 = vers >= 16;
        }

    }

    public String translateHexColor(String text) {

        if (have1_16) {

            if (text.contains("HEX:")) {
                Pattern pattern = Pattern.compile("HEX:#([a-fA-F0-9]{6})");

                for(Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
                    String cor = text.substring(matcher.start(), matcher.end()).replace("HEX:", "");
                    text = text.replace(cor, ChatColor.of(cor) + "");
                }
            }
        }

        return text.replace("HEX:", "").replace("INITHEX:", "").replace("ENDHEX:", "");
    }

}
