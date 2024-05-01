package com.xg7network.xg7lobby.config;

public enum PermissionType {


    ADMIN("admin");

    String perm;

    PermissionType(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return "xg7lobby." + perm;
    }

}
