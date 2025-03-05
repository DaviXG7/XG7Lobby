package com.xg7plugins.xg7lobby.help.chat;

import com.xg7plugins.help.chathelp.HelpInChat;
import com.xg7plugins.xg7lobby.XG7Lobby;

public class XG7LobbyHelpInChat extends HelpInChat {

    public XG7LobbyHelpInChat() {
        super(XG7Lobby.getInstance(), new Index());

        registerPage(new CustomCommandPage());
        registerPage(new SelectorGuidePage());
        registerPage(new AboutPage());
    }

}
