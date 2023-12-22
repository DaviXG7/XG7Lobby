package com.xg7network.xg7lobby.Utils.Text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.xg7network.xg7lobby.Utils.PlaceHolder;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.placeholderapi;

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

        String[] partes = Bukkit.getVersion().split("\\.");
        if (partes.length >= 2) {
            int vers = Integer.parseInt(partes[1]);
            if (vers >= 13) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(get(player)));
            } else {
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

}
