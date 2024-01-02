package com.xg7network.xg7lobby.DefautCommands;

import com.xg7network.xg7lobby.Player.PlayersManager;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        List<String> results = new ArrayList<>();

        switch (command.getName()) {
            case "xg7lobbyfly":
            case "xg7lobbymute":
            case "xg7lobbyunmute":
            case "xg7lobbygma":
            case "xg7lobbygmc":
            case "xg7lobbygms":
            case "xg7lobbygmsp":


                if (strings.length == 1) Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));

                break;

            case "xg7lobbykick":
            case "xg7lobbyban":
            case "xg7lobbytempban":
            case "xg7lobbywarn":

                if (strings.length == 1) Bukkit.getOnlinePlayers().forEach(player -> PlayersManager.getData(player.getUniqueId().toString()).getPlayer().getName());
                else if (strings.length >= 2) results.add("<REASON>");

                break;
            case "xg7lobbyunban":

                if (strings.length == 1) {
                    for (BanEntry entry : Bukkit.getBanList(BanList.Type.NAME).getBanEntries()) results.add(entry.getTarget());
                }

            case "xg7lobbytempmute":

                if (strings.length == 1) Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));

                else if (strings.length == 2) {
                    results.add("30min");
                    results.add("2h");
                    results.add("1d");
                    results.add("dd/mm/yyyy");
                    results.add("26/04/2010 (Example)");
                }
                else if (strings.length == 3) {
                    results.add("h:min");
                    results.add("14:36 (Example)");
                }

                break;

            case "xg7lobbygui":

                if (strings.length == 1) results.add("id");
        }
        return results;
    }
}
