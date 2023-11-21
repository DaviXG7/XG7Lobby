package com.xg7network.xg7lobby.Utils.Text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.xg7network.xg7chat.Text.CenterText;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.xg7chat;

public class Message {
    private String message;
    private Player player;
    private boolean isAction;

    public Message(String message, Player player) {
        this.message = message;
        this.player = player;
        this.isAction = message.startsWith("ACTION: ");
        if (isAction) this.message = message.replace("ACTION: ", "");

    }

    public void sendMessage() {
        if (message == null || message.equals("")) return;
        if (isAction) sendActionBar();
        else {
                if (message.startsWith("CENTER: ")) {
                    message = message.replace("CENTER: ", "");

                    player.sendMessage(XG7ChatUtil.centralize(message,player));

                } else player.sendMessage(getMessage(player));

        }
    }

    public String getMessage(Player player) {
        return PluginUtil.setPlaceHolders(message.replace("&", "§"), player);
    }

    public void sendActionBar() {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getMessage(player)));
        } catch (Exception ignored) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
            chat.getBytes().write(0, (byte) 2);
            chat.getChatComponents().write(0, WrappedChatComponent.fromText(message.replace("&", "§")));
            try {
                protocolManager.sendServerPacket(player, chat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
