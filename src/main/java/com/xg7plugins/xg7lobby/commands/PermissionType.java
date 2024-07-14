package com.xg7plugins.xg7lobby.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum PermissionType {

    DEFAULT(""),

    ANTITAB_COMMAND_BYPASS("antitab.bypass"),

    RELOAD("command.reload.*"),
    RELOAD_DB("command.reload.db"),
    RELOAD_CONFIG("command.reload.config"),
    RELOAD_CACHE("command.reload.cache"),
    RELOAD_TASK("command.reload.task"),

    LOBBY("command.lobby.*"),
    SET_LOBBY("command.lobby.set");

    final String perm;

    PermissionType(String perm) {
        this.perm = perm;
    }

    public static void register() {

        Arrays.stream(PermissionType.values()).forEach(type -> Bukkit.getPluginManager().addPermission(new Permission(type.perm)));

        List<PermissionType> parents = Arrays.stream(PermissionType.values()).filter(type -> type.perm.endsWith(".*")).collect(Collectors.toList());

        parents.forEach(parent -> {
            List<PermissionType> children = Arrays.stream(PermissionType.values())
                    .filter(child -> child.perm.startsWith(parent.perm.substring(0, parent.perm.length() - 1)) && !child.perm.endsWith(".*"))
                    .collect(Collectors.toList());

            Permission parentPerm = Objects.requireNonNull(Bukkit.getPluginManager().getPermission(parent.perm));
            children.forEach(child -> parentPerm.getChildren().put(child.perm, true));
            parentPerm.recalculatePermissibles();
        });

    }

}
