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
        isInEnabledWorldOnly = true,
        isPlayerOnly = true
)
public class SetLobby implements ICommand {


    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        String lobbyId = "lobby-" + UUID.randomUUID();

        if (args.len() > 0) lobbyId = args.get(0, String.class);

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
