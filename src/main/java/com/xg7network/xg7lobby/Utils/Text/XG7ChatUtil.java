package com.xg7network.xg7lobby.Utils.Text;

import com.xg7network.xg7chat.Text.CenterText;
import com.xg7network.xg7chat.Text.Color;
import org.bukkit.entity.Player;

import static com.xg7network.xg7lobby.XG7Lobby.xg7chat;

public class XG7ChatUtil {


    public static String getTexts(String text) {
        if (xg7chat) {
            Color color = new Color();
            text = color.translateHexColor(text);
        }
        return text;
    }

    public static String centralize(String text, Player player) {
        if (xg7chat) {
            CenterText centerText = new CenterText(text, player);
            text = centerText.getCentralizedText();
        }
        return text;
    }


}
