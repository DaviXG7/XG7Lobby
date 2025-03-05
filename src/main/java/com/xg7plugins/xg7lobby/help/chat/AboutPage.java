package com.xg7plugins.xg7lobby.help.chat;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.help.chathelp.HelpComponent;
import com.xg7plugins.help.chathelp.HelpPage;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.utils.text.component.Component;
import com.xg7plugins.utils.text.component.event.ClickEvent;
import com.xg7plugins.utils.text.component.event.action.ClickAction;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AboutPage extends HelpPage {

    public AboutPage() {
        super("about");
        addMessages(
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "&m-&9&m-&6&m------------------&e*&6&m------------------&9&m-&f&m-"
                ).build(),
                new AboutComponent(),
                HelpComponent.empty(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "lang:[help.chat.back]"
                ).clickEvent(ClickEvent.of(ClickAction.SUGGEST_COMMAND, "/xg7lobby help")).build(),
                HelpComponent.empty(),
                HelpComponent.of(
                        XG7Lobby.getInstance(),
                        "&m-&9&m-&6&m------------------&e*&6&m------------------&9&m-&f&m-"
                ).build()

        );
    }

    private class AboutComponent extends HelpComponent {
        public AboutComponent() {
            super(XG7Lobby.getInstance(),null,null, null);
        }


        @Override
        public Component buildFor(Player player) {

            Config lang = XG7Plugins.getInstance().getLangManager().getLangByPlayer(XG7Lobby.getInstance(), player).join().getLangConfiguration();

            String about = lang.getList("help.about", String.class).orElse(new ArrayList<>()).stream().collect(Collectors.joining("\n"));

            return Component.text(
                    Text.detectLangs(player, XG7Plugins.getInstance(),about).join()
                            .replace("discord", "discord.gg/jfrn8w92kF")
                            .replace("github", "github.com/DaviXG7")
                            .replace("website", "xg7plugins.com")
                            .replace("version", XG7Plugins.getInstance().getDescription().getVersion())
                            .getText()
            ).build();

        }


    }

}
