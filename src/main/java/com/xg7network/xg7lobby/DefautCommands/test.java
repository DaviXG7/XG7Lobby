package com.xg7network.xg7lobby.DefautCommands;

import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Action.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        switch (strings[0].toUpperCase()) {
            case "ACTION":

                commandSender.sendMessage("executando...");

                String action = String.join(" ", strings).replace("ACTION", "");
                Action Action = new Action((Player) commandSender, action);
                Action.execute();

                return true;

            case "ADD":

                Player target = Bukkit.getPlayer(strings[1]);


                PlayersManager.getData(target.getUniqueId().toString()).addInfractions("asdasdasd", new Date(System.currentTimeMillis()));

                PlayersManager.update(target.getUniqueId().toString(), PlayersManager.getData(target.getUniqueId().toString()));



                return true;

        }
        return true;
    }
}
