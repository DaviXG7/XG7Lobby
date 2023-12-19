package com.xg7network.xg7lobby.Utils.Text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class TextUtil {
    private String text;
    private boolean isAction;

    public TextUtil(String message) {
        this.text = message;
        this.isAction = message.startsWith("ACTION: ");
        if (isAction) this.text = message.replace("ACTION: ", "");

    }

    public void send(Player player) {
        if (text == null || text.equals("")) return;
        if (isAction) sendActionBar(player);
        else player.sendMessage(get(player));

    }

    public String get(Player player) {

        text = PluginUtil.setPlaceHolders(text, player);

        text = new Color().translateHexColor(text);

        if (text.startsWith("CENTER: ")) {
            text = text.replace("CENTER: ", "");

            text = new CenterText(text,player).getCentralizedText();

        }

        return text.replace("&", "§");
    }

    public String get() {

        text = new Color().translateHexColor(text);

        if (text.startsWith("CENTER: ")) {
            text = text.replace("CENTER: ", "");

            text = new CenterText(text).getCentralizedText();

        }

        return text.replace("&", "§");

    }

    public void sendActionBar(Player player) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(get(player)));
        } catch (Exception ignored) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
            chat.getBytes().write(0, (byte) 2);
            chat.getChatComponents().write(0, WrappedChatComponent.fromText(text.replace("&", "§")));
            try {
                protocolManager.sendServerPacket(player, chat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
