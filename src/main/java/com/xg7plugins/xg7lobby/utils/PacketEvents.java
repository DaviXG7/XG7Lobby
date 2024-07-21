package com.xg7plugins.xg7lobby.utils;

import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.PacketPlayEvent;
import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketEvents {

    public static void create(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext context, Object packet)
                    throws Exception {

                Object modPacket = packet;

                for (PacketPlayEvent packetPlayEvent : EventManager.getPacketEvents()) {
                    if (packetPlayEvent instanceof PacketPlayEvent.PacketPlayInEvent) {
                        if (!packetPlayEvent.isEnabled()) break;
                        for (String packetName : packetPlayEvent.getPacketsNames()) {
                            if (packet.getClass().getName().contains(packetName)) {
                                modPacket = ((PacketPlayEvent.PacketPlayInEvent) packetPlayEvent).in(player, modPacket);
                                break;
                            }
                        }
                    }
                }
                super.channelRead(context, packet);
            }

            @Override
            public void write(ChannelHandlerContext context, Object packet,
                              ChannelPromise channelPromise) throws Exception {
                Object modPacket = packet;
                for (PacketPlayEvent packetPlayEvent : EventManager.getPacketEvents()) {
                    if (packetPlayEvent instanceof PacketPlayEvent.PacketPlayOutEvent) {
                        if (!packetPlayEvent.isEnabled()) break;
                        for (String packetName : packetPlayEvent.getPacketsNames()) {
                            if (packet.getClass().getName().contains(packetName)) {
                                modPacket = ((PacketPlayEvent.PacketPlayOutEvent) packetPlayEvent).out(player, modPacket);
                                break;
                            }
                        }
                    }
                }
                super.write(context, packet, channelPromise);
            }
        };

        try {
            Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);

            Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);


            Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);
            Object networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection);
            Object channel = networkManager.getClass().getField("channel").get(networkManager);
            Object pipeLine = channel.getClass().getMethod("pipeline").invoke(channel);

            ChannelPipeline channelPipeline = (ChannelPipeline) pipeLine;
            channelPipeline.addBefore("packet_handler", player.getName(),
                    channelDuplexHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopEvent(Player player) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 13) {
            try {
                Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
                Object craftPlayer = craftPlayerClass.cast(player);

                Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
                Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);
                Object networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection);

                Channel channel = (Channel) networkManager.getClass().getField("channel").get(networkManager);

                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(player.getName());
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
