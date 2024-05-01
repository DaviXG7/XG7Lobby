package com.xg7network.xg7lobby.utils.Text;

public class CenterText {
    private static final int chatPXS = 320;

    public static String getCentralizedText(String text) {
        text = Color.translateHexColor(text);
        return centralize(text);
    }

    private static String centralize(String text) {
        int size = 0;
        int pararetirar = 0;

        int espacosExtras;
        for(espacosExtras = 0; espacosExtras < text.length() - 1; ++espacosExtras) {
            if (text.charAt(espacosExtras) == '&' && Character.isLetterOrDigit(text.charAt(espacosExtras + 1)) && text.charAt(espacosExtras + 1) != ' ') {
                pararetirar += 6 + getCharSize(text.charAt(espacosExtras + 1));
            }
        }

        int espacosAEsquerda;
        for(espacosExtras = 0; espacosExtras < text.length(); ++espacosExtras) {
            espacosAEsquerda = text.charAt(espacosExtras);
            size += getCharSize((char)espacosAEsquerda);
        }

        size -= pararetirar;
        espacosExtras = (chatPXS - size) / 4;
        if (espacosExtras <= 0) {
            return text;
        } else {
            espacosAEsquerda = espacosExtras / 2;
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < espacosAEsquerda; ++i) {
                builder.append(' ');
            }

            builder.append(text);
            return builder.toString().replace("&", "§");
        }
    }

    private static int getCharSize(char c) {
        String[] chars = new String[]{"~@", "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZabcedjhmnopqrsuvxwyz/\\+=-_^?&%$#", "{}fk*\"<>()", "It[] ", "'l`", "!|:;,.i", "¨´"};
        if (c != 167) {
            for (int i = 0; i < chars.length; ++i) {
                if (chars[i].contains(String.valueOf(c))) {
                    return 7 - i;
                }
            }

        }
        return 0;
    }
}
