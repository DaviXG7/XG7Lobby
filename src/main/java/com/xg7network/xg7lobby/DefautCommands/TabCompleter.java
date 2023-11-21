package com.xg7network.xg7lobby.DefautCommands;

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
            case "xg7lobbysetlobby":

                if (strings.length == 1) results.add("delete");

                break;

            case "xg7lobbyfly":

                if (strings.length == 1) Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));

                break;

            case "xg7lobbykick":

                if (strings.length == 1) Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));
                else if (strings.length == 2) {
                    Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));
                    results.add("<reason>");
                }
        }
        return results;
    }
}
