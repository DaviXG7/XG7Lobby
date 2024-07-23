package com.xg7plugins.xg7lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum PermissionType {

    DEFAULT(""),

    ADMIN("*"),

    ANTITAB_BYPASS("antitab.bypass"),
    ANTITAB_PLUGIN_BYPASS("antitab.plugin.bypass"),

    SPAM("spam"),

    HELP("help"),

    COMMAND("command.*"),

    BUG("xg7lobby.command.bug"),
    SUGGEST("xg7lobby.command.suggest"),

    RELOAD("command.reload.*"),
    RELOAD_DB("command.reload.db"),
    RELOAD_CONFIG("command.reload.config"),
    RELOAD_CACHE("command.reload.cache"),
    RELOAD_TASK("command.reload.task"),
    RELOAD_MENUS("command.reload.menus"),

    ACTION("command.action"),

    GUI("command.gui"),

    MOD("command.mod.*"),

    WARN("command.mod.warn"),
    WARN_REMOVE("command.mod.warn.remove"),
    WARNS_OTHER("command.mod.warns.other"),
    KICK("command.mod.kick"),

    LOCKCHAT("command.lockchat"),
    CHAT("command.chat.*"),
    CHAT_SWEAR("command.chat.swear"),
    CHAT_COMMANDS("command.chat.commands"),

    BAN("command.mod.ban"),
    TEMPBAN("command.mod.tempban"),
    BANIP("command.mod.banip"),
    UNBAN("command.mod.unban"),

    GAMEMODE("command.gamemode.*"),
    GAMEMODE_OTHERS("command.gamemode.others"),
    GAMEMODE_SURVIVAL("command.gamemode.survival"),
    GAMEMODE_CREATIVE("command.gamemode.creative"),
    GAMEMODE_SPECTATOR("command.gamemode.spectator"),
    GAMEMODE_ADVENTURE("command.gamemode.adventure"),

    MUTE("command.mod.mute"),
    UNMUTE("command.mod.unmute"),
    TEMPMUTE("command.mod.tempmute"),

    LOBBY("command.lobby.*"),
    SET_LOBBY("command.lobby.set"),

    FLY("command.fly"),
    FLY_OTHER("command.fly.other"),
    DOUBLE_JUMP("double-jump"),

    ENTITY_ATTACK("entity-attack"),

    PVP("command.pvp"),
    PVP_OTHER("command.pvp.others"),

    VANISH("command.vanish"),
    VANISH_OTHER("command.vanish.others"),

    BUILD("command.lobby.build"),
    BUILD_OTHER("command.lobby.build.others"),

    BLOCK("block.*"),
    BLOCK_BUILD("block.place"),
    BLOCK_INTERACT("block.interact"),
    BLOCK_BREAK("block.break"),

    ITEM("block.item.*"),
    ITEM_PICKUP("block.item.pickup"),
    ITEM_DROP("block.item.drop");

    final String perm;

    public String getPerm() {
        return "xg7lobby." + perm;
    }

    PermissionType(String perm) {
        this.perm = perm;
    }

    public static void register() {

        Arrays.stream(PermissionType.values()).forEach(type -> Bukkit.getPluginManager().addPermission(new Permission(type.getPerm())));

        List<PermissionType> parents = Arrays.stream(PermissionType.values()).filter(type -> type.perm.endsWith(".*")).collect(Collectors.toList());

        parents.forEach(parent -> {
            List<PermissionType> children = Arrays.stream(PermissionType.values())
                    .filter(child -> child.perm.startsWith(parent.perm.substring(0, parent.perm.length() - 1)) && child != parent)
                    .collect(Collectors.toList());

            Permission parentPerm = Objects.requireNonNull(Bukkit.getPluginManager().getPermission(parent.getPerm()));
            children.forEach(child -> parentPerm.getChildren().put(child.getPerm(), true));
            parentPerm.recalculatePermissibles();
        });

    }

}
