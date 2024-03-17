package com.xg7network.xg7lobby.Utils.Text;

/*

    This class was made by DaviXG7 to make it
    easier to handle the plugin texts.

    The class is free to use if this text is
    copied into your plugin and your plugin is free.

    Thanks for reading/using my code <3

 */

import com.xg7network.xg7lobby.Utils.NMSUtil;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class TextUtil {

    public static void send(String text, Player player) {
        if (text == null || text.equals("")) return;
        if (text.startsWith("[ACTION] ")) {
            text = text.replace("[ACTION] ", "");
            sendActionBar(text, player);
        }
        else player.sendMessage(get(text, player));

    }

    public static String get(String text, Player player) {

        text = PluginUtil.setPlaceHolders(text, player);

        text = new Color().translateHexColor(text);

        if (text.startsWith("CENTER: ")) {
            text = text.replace("CENTER: ", "");

            text = CenterText.getCentralizedText(text,player);

        }

        return prefix != null ? text.replace("&", "§").replace("[PREFIX]", prefix) : text.replace("&", "§");
    }

    public static String get(String text) {

        text = new Color().translateHexColor(text);

        if (text.startsWith("CENTER: ")) {
            text = text.replace("CENTER: ", "");

            text = CenterText.getCentralizedText(text);

        }

        return prefix != null ? text.replace("&", "§").replace("[PREFIX]", prefix) : text.replace("&", "§");

    }

    public static void sendActionBar(String text, Player player) {

        String[] partes = Bukkit.getVersion().split("\\.");
        if (partes.length >= 2) {
            int vers = Integer.parseInt(partes[1]);
            if (vers >= 13) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(get(text, player)));
            } else {
                try {

                    Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
                    Object craftPlayer = craftPlayerClass.cast(player);

                    Class<?> packetPlayOutChatClass = NMSUtil.getNMSClass("PacketPlayOutChat");
                    Class<?> iChatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
                    Class<?> chatComponentTextClass = NMSUtil.getNMSClass("ChatComponentText");

                    Object chatComponent = chatComponentTextClass.getConstructor(String.class).newInstance(get(text, player));
                    Object packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class)
                            .newInstance(chatComponent, (byte) 2);

                    Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
                    Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);

                    playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMSClass("Packet")).invoke(playerConnection, packet);

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         NoSuchFieldException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
