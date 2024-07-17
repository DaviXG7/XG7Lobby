package com.xg7plugins.xg7lobby.scores;

import com.xg7plugins.xg7lobby.utils.NMSUtil;
import com.xg7plugins.xg7lobby.utils.Text;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class TabList {

    @SneakyThrows
    public static void sendTablist(Player player, List<String> headerl, List<String> footerl) {

        String header = headerl == null ? "" : Text.getFormatedText(player, headerl.stream().map(item -> item + "\n").collect(Collectors.joining()));
        String footer = footerl == null ? "" : Text.getFormatedText(player, footerl.stream().map(item -> item + "\n").collect(Collectors.joining()));

        if (Integer.parseInt(Bukkit.getVersion().split("\\.")[1]) >= 16) {
            player.setPlayerListHeader(header);
            player.setPlayerListFooter(footer);
            return;
        }

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
    }
}
