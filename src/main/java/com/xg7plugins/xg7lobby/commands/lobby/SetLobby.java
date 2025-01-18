package com.xg7plugins.xg7lobby.commands.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.*;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.location.Location;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Command(
        name = "setlobby",
        permission = "xg7lobby.command.setlobby",
        syntax = "xg7lobby setlobby (id) (<id> ([world,x,y,z] or [world,x,y,z,yaw,pitch]))",
        description = "Sets the lobby location",
        isInEnabledWorldOnly = true
)
public class SetLobby implements ICommand {


    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        String lobbyId = "lobby-" + UUID.randomUUID();

        if (args.len() > 0) lobbyId = args.get(0, String.class);


        if (args.len() < 2) {
            if (!(sender instanceof Player)) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.not-a-player")
                        .thenAccept(text -> text.send(sender));
                Text.format("Console: setlobby <id> ([world,x,y,z] | [world,x,y,z,yaw,pitch])")
                        .send(sender);
                return;
            }

            Player player = (Player) sender;

            LobbyLocation location = new LobbyLocation(lobbyId, XG7Lobby.getInstance().getServerInfo(), Location.fromPlayer(player));

            if (!XG7Lobby.getInstance().isWorldEnabled(location.getLocation().getWorld())) {
                Text.formatLang(XG7Plugins.getInstance(), player, "commands.disabled-world").thenAccept(text -> text.send(player));
                return;
            }

            XG7Lobby.getInstance().getLobbyManager().saveLobby(location)
                    .exceptionally(e -> {
                        System.out.println("[ERROR] Failed to save lobby: " + e.getMessage());
                        Text.formatLang(XG7Lobby.getInstance(), player, "lobby.on-set.on-error")
                                .thenAccept(text -> text.replace("[ERROR]", e.getMessage()).send(player));
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }).thenRun(() -> {
                        System.out.println("[DEBUG] Lobby saved successfully. Sending success message...");
                        Text.formatLang(XG7Lobby.getInstance(), player, "lobby.on-set.on-success").join()
                                .replace("[ID]", location.getId())
                                .replace("[WORLD]", location.getLocation().getWorld().getName())
                                .replace("[X]", String.format("%.2f", location.getLocation().getX()))
                                .replace("[Y]", String.format("%.2f", location.getLocation().getY()))
                                .replace("[Z]", String.format("%.2f", location.getLocation().getZ()))
                                .replace("[YAW]", String.format("%.2f", location.getLocation().getYaw()))
                                .replace("[PITCH]", String.format("%.2f", location.getLocation().getPitch()))
                                .send(player);
                    });
            return;
        }

        if (args.len() != 5 && args.len() != 7) {
            syntaxError(sender, "/xg7lobby setlobby <id> ([world,x,y,z] or [world,x,y,z,yaw,pitch])");
            return;
        }

        try {
            World world = args.get(1, World.class);
            double x = args.get(2, double.class);
            double y = args.get(3, double.class);
            double z = args.get(4, double.class);
            float yaw = 0f;
            float pitch = 0f;

            if (args.len() == 7) {
                yaw = args.get(5, float.class);
                pitch = args.get(6, float.class);
            }

            if (world == null) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-set.on-error").thenAccept(text -> text.send(sender));
                return;
            }

            if (!XG7Lobby.getInstance().isWorldEnabled(world)) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-set.on-error").thenAccept(text -> text.send(sender));
                return;
            }

            LobbyLocation location = new LobbyLocation(lobbyId, XG7Lobby.getInstance().getServerInfo(),
                    Location.of(world.getName(), x, y, z, yaw, pitch));

            XG7Lobby.getInstance().getLobbyManager().saveLobby(location)
                    .exceptionally(e -> {
                        Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-set.on-error")
                                .thenAccept(text -> text.replace("[ERROR]", e.getMessage()).send(sender));
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }).thenRun(() -> {
                        System.out.println("[DEBUG] Lobby saved successfully. Sending success message...");
                        Text.formatLang(XG7Lobby.getInstance(), sender, "lobby.on-set.on-success").join()
                                .replace("[ID]", location.getId())
                                .replace("[WORLD]", location.getLocation().getWorld().getName())
                                .replace("[X]", String.format("%.2f", location.getLocation().getX()))
                                .replace("[Y]", String.format("%.2f", location.getLocation().getY()))
                                .replace("[Z]", String.format("%.2f", location.getLocation().getZ()))
                                .replace("[YAW]", String.format("%.2f", location.getLocation().getYaw()))
                                .replace("[PITCH]", String.format("%.2f", location.getLocation().getPitch()))
                                .send(sender);
                    });

        } catch (Exception e) {
            syntaxError(sender, "/setlobby ([world,x,y,z] or [world,x,y,z,yaw,pitch])");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {

        switch (args.len()) {
            case 1:
                return Collections.singletonList("id");
            case 2:
                return XG7Lobby.getInstance().getEnabledWorlds();
            case 3:
                return Collections.singletonList("x");
            case 4:
                return Collections.singletonList("y");
            case 5:
                return Collections.singletonList("z");
            case 6:
                return Collections.singletonList("yaw");
            case 7:
                return Collections.singletonList("pitch");
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.BLAZE_ROD, this);
    }

}
