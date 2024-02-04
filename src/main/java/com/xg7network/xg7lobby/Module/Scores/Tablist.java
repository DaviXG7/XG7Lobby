package com.xg7network.xg7lobby.Module.Scores;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.xg7network.xg7lobby.Utils.NMSUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class Tablist {

    private String header;
    private String footer;
    private Player player;

    public Tablist(Player player, List<String> header, List<String> footer) {
        this.header = header == null ? "" : TextUtil.get(header.stream().map(item -> item + "\n").collect(Collectors.joining()).replace("&", "§"), player);
        this.footer = footer == null ? "" : TextUtil.get(footer.stream().map(item -> item + "\n").collect(Collectors.joining()).replace("&", "§"), player);
        this.player = player;
    }

    public void sendTabList() {
        if (Integer.parseInt(Bukkit.getVersion().split("\\.")[1]) < 16) {


            try {


                Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
                Object craftPlayer = craftPlayerClass.cast(player);
                Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
                Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);

                Class<?> PlacketPlayOutPlayerListHeaderFooterClass = NMSUtil.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
                Class<?> chatComponentTextClass = NMSUtil.getNMSClass("ChatComponentText");

                Object headerComponent = chatComponentTextClass.getConstructor(String.class).newInstance(header);
                Object footerComponent = chatComponentTextClass.getConstructor(String.class).newInstance(footer);

                Object PlacketPlayOutPlayerListHeaderFooterInstance = PlacketPlayOutPlayerListHeaderFooterClass.newInstance();

                Field fieldA = PlacketPlayOutPlayerListHeaderFooterClass.getDeclaredField("a");
                fieldA.setAccessible(true);
                fieldA.set(PlacketPlayOutPlayerListHeaderFooterInstance, headerComponent);

                Field fieldB = PlacketPlayOutPlayerListHeaderFooterClass.getDeclaredField("b");
                fieldB.setAccessible(true);
                fieldB.set(PlacketPlayOutPlayerListHeaderFooterInstance, footerComponent);

                playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMSClass("Packet")).invoke(playerConnection, PlacketPlayOutPlayerListHeaderFooterInstance);



            } catch (ClassNotFoundException | NoSuchFieldException | InvocationTargetException |
                     IllegalAccessException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        } else {

            player.setPlayerListHeader(header);
            player.setPlayerListFooter(footer);

        }

    }
}
