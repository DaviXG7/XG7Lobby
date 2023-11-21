package com.xg7network.xg7lobby.Module.Scores;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.xg7network.xg7lobby.Utils.Text.XG7ChatUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Tablist {

    private String header;
    private String footer;
    private Player player;

    public Tablist(Player player, List<String> header, List<String> footer) {
        this.header = header == null ? "" : XG7ChatUtil.getTexts(header.stream().map(item -> item + "\n").collect(Collectors.joining()).replace("&", "§"));
        this.footer = footer == null ? "" : XG7ChatUtil.getTexts(footer.stream().map(item -> item + "\n").collect(Collectors.joining()).replace("&", "§"));
        this.player = player;
    }

    public void sendTabList() {

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer playerListHeaderFooter = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        playerListHeaderFooter.getChatComponents().write(0, WrappedChatComponent.fromText(header));
        playerListHeaderFooter.getChatComponents().write(1, WrappedChatComponent.fromText(footer));

        try {
            protocolManager.sendServerPacket(player, playerListHeaderFooter);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
