package com.xg7plugins.xg7lobby.Enums;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum PermissionType {

    LOBBY("lobby.*"),
    SET_LOBBY("lobby.set");

    @Getter
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
