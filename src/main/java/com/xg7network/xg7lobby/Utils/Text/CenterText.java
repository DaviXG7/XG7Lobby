package com.xg7network.xg7lobby.Utils.Text;

import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.entity.Player;

public class CenterText {
    private String text;
    int chatPXS = 320;

    public CenterText(String text, Player player) {
        this.text = PluginUtil.setPlaceHolders(text, player);
        Color color = new Color();
        this.text = color.translateHexColor(text);
    }

    public String getCentralizedText() {
        int size = 0;
        int pararetirar = 0;

        int espacosExtras;
        for(espacosExtras = 0; espacosExtras < this.text.length() - 1; ++espacosExtras) {
            if (this.text.charAt(espacosExtras) == '&' && Character.isLetterOrDigit(this.text.charAt(espacosExtras + 1)) && this.text.charAt(espacosExtras + 1) != ' ') {
                pararetirar += 6 + this.getCharSize(this.text.charAt(espacosExtras + 1));
            }
        }

        int espacosAEsquerda;
        for(espacosExtras = 0; espacosExtras < this.text.length(); ++espacosExtras) {
            espacosAEsquerda = this.text.charAt(espacosExtras);
            size += this.getCharSize((char)espacosAEsquerda);
        }

        size -= pararetirar;
        espacosExtras = (this.chatPXS - size) / 4;
        if (espacosExtras <= 0) {
            return this.text;
        } else {
            espacosAEsquerda = espacosExtras / 2;
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < espacosAEsquerda; ++i) {
                builder.append(' ');
            }

            builder.append(this.text);
            return builder.toString().replace("&", "§");
        }
    }

    int getCharSize(char c) {
        String[] chars = new String[]{"~@", "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZabcedjhmnopqrsuvxwyz/\\+=-_^?&%$#", "{}fk*\"<>()", "It[] ", "'l`", "!|:;,.i", "¨´"};
        if (c == 167) {
            return 0;
        } else {
            for(int i = 0; i < chars.length; ++i) {
                if (chars[i].contains(String.valueOf(c))) {
                    return 7 - i;
                }
            }

            return 0;
        }
    }
}
