package com.xg7network.xg7lobby.Utils.Other.Placeholders;

public enum XG7LobbyPlaceholders {

    WARNS("warns", "Número de advertências do jogador"),
    CHAT_LOCKED("chat_locked", "Se o chat está bloqueado"),
    MUTED("muted", "Se o jogador está silenciado"),
    TIME_FOR_UNMUTE("time_for_unmute", "Quando o jogador será dessilenciado"),
    FIRST_JOIN("first_join", "Primeira entrada do jogador no servidor"),
    LOBBY_LOCATION("lobby_location", "Localização do lobby"),
    PLAYER_HIDING("players_hide", "Se o jogador está escondendo outros jogadores");

    private final String name;
    private final Object placeholder;

    XG7LobbyPlaceholders(String name, Object placeholder) {
        this.name = name;
        this.placeholder = placeholder;
    }

    public Object getPlaceholder() {
        return this.placeholder;
    }

    public static String setPlaceHolders(String s) {
        for (XG7LobbyPlaceholders placeholder : XG7LobbyPlaceholders.values())
            s = s.replace("%xg7lobby_" + placeholder.name + "%", placeholder.placeholder.toString());
        return s;
    }


}
