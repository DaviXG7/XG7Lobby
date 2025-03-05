package com.xg7plugins.xg7lobby.help.chat;

import com.xg7plugins.help.chathelp.HelpComponent;
import com.xg7plugins.help.chathelp.HelpPage;
import com.xg7plugins.utils.text.component.event.ClickEvent;
import com.xg7plugins.utils.text.component.event.HoverEvent;
import com.xg7plugins.utils.text.component.event.action.ClickAction;
import com.xg7plugins.utils.text.component.event.action.HoverAction;
import com.xg7plugins.xg7lobby.XG7Lobby;

public class Index extends HelpPage {
    
    public Index() {
        super("index");

        addMessages(
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "&m-&9&m-&6&m------------------&e*&6&m------------------&9&m-&f&m-"
                ).build(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.title]"
                ).build(),
                HelpComponent.empty(),
                HelpComponent.of(
                                XG7Lobby.getInstance(),
                                "lang:[help.chat.content]"
                        )
                        .clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7lobby help about"))
                        .hoverEvent(HoverEvent.of(HoverAction.SHOW_TEXT, "Click to see about the plugins"))
                        .build(),
                HelpComponent.empty(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.lang]"
                ).clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7plugins lang")).build(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.selector-guide]"
                ).clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7lobby help selector-guide")).build(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.commands]"
                ).clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7lobby help command-page1")).build(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.custom-commands-guide]"
                ).clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7lobby help custom-commands-guide")).build(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "&m-&9&m-&6&m------------------&e*&6&m------------------&9&m-&f&m-"
                ).build()
        );
    }
}
