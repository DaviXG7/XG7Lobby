package com.xg7plugins.xg7lobby.commands.lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.*;
import com.xg7plugins.libs.xg7menus.XSeries.XMaterial;
import com.xg7plugins.libs.xg7menus.builders.item.ItemBuilder;
import com.xg7plugins.utils.Location;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.model.LobbyLocation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Command(
        name = "xg7lobbysetlobby",
        aliasesPath = "setlobby",
        perm = "xg7lobby.command.setlobby",
        syntax = "setlobby",
        description = "Sets the lobby location",
        isOnlyInWorld = true
)
public class SetLobby implements ICommand {

    private final ISubCommand[] subCommands = new ISubCommand[]{new SetLobbyConsole()};

    @Override
    public ISubCommand[] getSubCommands() {
        return subCommands;
    }


    @Override
    public void onCommand(org.bukkit.command.Command command, CommandSender sender, String label) {

        if (!(sender instanceof Player)) {
            ICommand.super.onCommand(command,sender,label);
            return;
        }

        Player player = (Player) sender;

        LobbyLocation location = new LobbyLocation(Location.fromPlayer(player));

        try {
            XG7Plugins.getInstance().getJsonManager().saveJson(XG7Lobby.getInstance(),"lobby.json", location);
        } catch (IOException e) {

            Text.format("lang:[lobby.on-set.on-error]", XG7Lobby.getInstance()).send(player);

            throw new RuntimeException(e);
        }

        Text.format("lang:[lobby.on-set.on-success]", XG7Lobby.getInstance())
                .replace("[WORLD]", location.getLocation().getWorld().getName())
                .replace("[X]", String.format("%.2f",location.getLocation().getX()))
                .replace("[Y]", String.format("%.2f",location.getLocation().getY()))
                .replace("[Z]", String.format("%.2f",location.getLocation().getZ()))
                .replace("[YAW]", String.format("%.2f", location.getLocation().getYaw()))
                .replace("[PITCH]", String.format("%.2f", location.getLocation().getPitch()))
                .send(player);
    }

    @SubCommand(
            description = "Sets the lobby location on console",
            perm = "xg7lobby.command.setlobby",
            type = SubCommandType.ARGS,
            syntax = "setlobby [world,x,y,z] or [world,x,y,z,yaw,pitch]",
            isOnlyConsole = true
    )
    static class SetLobbyConsole implements ISubCommand {

        @Override
        public void onSubCommand(CommandSender sender, String[] args, String label) {

            LobbyLocation location = null;

            if (args.length == 4) {
                String world = args[0];
                if (Bukkit.getWorld(world) == null) {
                    Text.format("lang:[lobby.on-set.on-error]", XG7Lobby.getInstance()).send(sender);
                    return;
                }
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                location = new LobbyLocation(Location.of(world,x,y,z));

            }
            if (args.length == 6) {
                String world = args[0];
                if (Bukkit.getWorld(world) == null) {
                    Text.format("lang:[lobby.on-set.on-error]", XG7Lobby.getInstance()).send(sender);
                    return;
                }
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                float yaw = Float.parseFloat(args[4]);
                float pitch = Float.parseFloat(args[5]);
                location = new LobbyLocation(Location.of(world,x,y,z,yaw,pitch));
            }

            if (location == null) {
                syntaxError(sender, "setlobby [world,x,y,z] or [world,x,y,z,yaw,pitch]");
                return;
            }

            try {
                XG7Plugins.getInstance().getJsonManager().saveJson(XG7Lobby.getInstance(),"lobby.json", location);
            } catch (IOException e) {

                Text.format("lang:[lobby.on-set.on-error]", XG7Lobby.getInstance()).send(sender);

                throw new RuntimeException(e);
            }

            Text.format("lang:[lobby.on-set.on-success]", XG7Lobby.getInstance())
                    .replace("[WORLD]", location.getLocation().getWorld().getName())
                    .replace("[X]", String.format("%.2f",location.getLocation().getX()))
                    .replace("[Y]", String.format("%.2f",location.getLocation().getY()))
                    .replace("[Z]", String.format("%.2f",location.getLocation().getZ()))
                    .replace("[YAW]", String.format("%.2f", location.getLocation().getYaw()))
                    .replace("[PITCH]", String.format("%.2f", location.getLocation().getPitch()))
                    .send(sender);
        }


        @Override
        public ItemBuilder getIcon() {
            return ItemBuilder.subCommandIcon(XMaterial.BLAZE_ROD, this, XG7Lobby.getInstance());
        }

        @Override
        public ISubCommand[] getSubCommands() {
            return new ISubCommand[0];
        }
    }


    public List<String> onTabComplete(org.bukkit.command.Command command, CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) return Collections.emptyList();
        if (args.length == 1) {
            return Collections.singletonList("world");
        }
        if (args.length == 2) {
            return Collections.singletonList("x");
        }
        if (args.length == 3) {
            return Collections.singletonList("y");
        }
        if (args.length == 4) {
            return Collections.singletonList("z");
        }
        if (args.length == 5) {
            return Collections.singletonList("yaw");
        }
        if (args.length == 6) {
            return Collections.singletonList("pitch");
        }

        return Collections.singletonList(" ");
    }


    @Override
    public ItemBuilder getIcon() {
        return ItemBuilder.commandIcon(XMaterial.BLAZE_ROD, this, XG7Lobby.getInstance());
    }
}
