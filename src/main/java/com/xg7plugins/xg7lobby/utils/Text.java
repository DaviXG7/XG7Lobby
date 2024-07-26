package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import lombok.Getter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

    private static final Pattern GRADIENT_PATTERN = Pattern.compile("\\[g#([0-9a-fA-F]{6})](.*?)\\[/g#([0-9a-fA-F]{6})]");
    private final static Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");


    public static void send(String text, CommandSender sender) {
        if (text == null || text.isEmpty()) return;
        if (sender instanceof Player) {
            text = text.replace("[PLAYER]", sender.getName());
            if (text.startsWith("[ACTION] ")) {
                sendActionBar(text.substring(9), ((Player) sender));
                return;
            }

            sender.sendMessage(getFormatedText(((Player) sender), text));
            return;
        }
        sender.sendMessage(translateColorCodes(text));
    }

    @SneakyThrows
    public static void sendActionBar(String text, Player player) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getFormatedText(player, text)));
            return;
        }

        Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
        Object craftPlayer = craftPlayerClass.cast(player);

        Class<?> packetPlayOutChatClass = NMSUtil.getNMSClass("PacketPlayOutChat");
        Class<?> iChatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
        Class<?> chatComponentTextClass = NMSUtil.getNMSClass("ChatComponentText");

        Object chatComponent = chatComponentTextClass.getConstructor(String.class).newInstance(getFormatedText(player, text));
        Object packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class)
                .newInstance(chatComponent, (byte) 2);

        Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
        Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);
        playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMSClass("Packet")).invoke(playerConnection, packet);

    }

    public static String getFormatedText(Player player, String text) {
        if (text.startsWith("[CENTER] ")) return translateColorCodes(getCentralizedText(PixelsSize.CHAT.getPixels(), setPlaceholders(text.substring(9), player)));
        return translateColorCodes(setPlaceholders(text, player));
    }
    public static String setPlaceholders(String text, Player player) {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }

    public static String translateColorCodes(String text) {

        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 16) {
            text = applyGradients(text);
            Matcher matcher = HEX_PATTERN.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color.substring(1)) + "");
                matcher = HEX_PATTERN.matcher(text);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', text.replace("[PREFIX]", Config.getString(ConfigType.CONFIG, "plugin-prefix")));

    }

    public static String getCentralizedText(int pixels, String text) {

        int textWidht = 0;
        boolean cCode = false;
        boolean isBold = false;
        boolean isrgb = false;
        int rgbCount = 0;
        int cCodeCount = 0;
        int rgbToAdd = 0;
        for (char c : text.toCharArray()) {
            if (isrgb) {
                if (rgbCount == 6) {
                    isrgb = false;
                    continue;
                }
                if ("0123456789aAbBcCdDeEfF".contains(String.valueOf(c))) {
                    rgbToAdd = getCharSize(c, isBold);
                    rgbCount++;
                    continue;
                }
                rgbCount = 0;
                textWidht += rgbToAdd;
                continue;
            }
            if (c == '&') {
                cCode = true;
                cCodeCount++;
                continue;
            }
            if (cCode && net.md_5.bungee.api.ChatColor.ALL_CODES.contains(c + "")) {
                cCode = false;
                cCodeCount = 0;
                isBold = c == 'l' || c == 'L';
                continue;
            }
            if (cCode) {
                if (c == '#') {
                    cCode = false;
                    isrgb = true;
                    continue;
                }
                while (cCodeCount != 0) {
                    cCodeCount--;
                    textWidht += getCharSize('&', isBold);
                }
            }
            textWidht += getCharSize(c, isBold);
        }

        textWidht /= 2;

        if (textWidht > pixels) return text;

        StringBuilder builder = new StringBuilder();

        int compensated = 0;

        while (compensated < pixels - textWidht) {
            builder.append(ChatColor.COLOR_CHAR + "r ");
            compensated += 4;
        }

        return builder + text;

    }
    private static double[] linear(double from, double to, int max) {
        final double[] res = new double[max];
        for (int i = 0; i < max; i++) {
            res[i] = from + i * ((to - from) / (max - 1));
        }
        return res;
    }

    public static String applyGradients(String text) {

        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            Color from = Color.decode("#" + matcher.group(1));
            Color to = Color.decode("#" + matcher.group(3));
            String textHex = matcher.group(2);

            double[] red = linear(from.getRed(), to.getRed(), textHex.length());
            double[] green = linear(from.getGreen(), to.getGreen(), textHex.length());
            double[] blue = linear(from.getBlue(), to.getBlue(), textHex.length());

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < textHex.length(); i++) {
                builder.append(net.md_5.bungee.api.ChatColor.of(new Color(
                                (int) Math.round(red[i]),
                                (int) Math.round(green[i]),
                                (int) Math.round(blue[i]))))
                        .append(textHex.charAt(i));
            }
            matcher.appendReplacement(result, builder.toString() + net.md_5.bungee.api.ChatColor.RESET);
        }
        matcher.appendTail(result);

        return result.toString();
    }
    private static int getCharSize(char c, boolean isBold) {
        String[] chars = new String[]{"~@", "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZabcedjhmnopqrsuvxwyz/\\+=-_^?&%$#", "{}fk*\"<>()", "It[] ", "'l`", "!|:;,.i", "¨´"};
        for (int i = 0; i < chars.length; i++) {
            if (chars[i].contains(String.valueOf(c))) {
                return isBold && c != ' ' ? 8 - i : 7 - i;
            }
        }

        return 4;
    }

    public static long convertToMilliseconds(String timeStr) {
        long milliseconds = 0;
        Pattern pattern = Pattern.compile("(\\d+)([SMHD])");
        Matcher matcher = pattern.matcher(timeStr.toUpperCase());

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "S":
                    milliseconds += value * 1000;
                    break;
                case "M":
                    milliseconds += value * 60000;
                    break;
                case "H":
                    milliseconds += value * 3600000;
                    break;
                case "D":
                    milliseconds += value * 86400000;
                    break;
                default:
                    Log.severe("Invalid time unit: " + unit);
            }
        }

        return milliseconds;
    }

    @Getter
    public enum PixelsSize {

        CHAT(157),
        MOTD(127),
        INV(75);

        final int pixels;

        PixelsSize (int pixels) {
            this.pixels = pixels;
        }


    }

}
