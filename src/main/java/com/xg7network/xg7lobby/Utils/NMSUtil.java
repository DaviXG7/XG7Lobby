package com.xg7network.xg7lobby.Utils;

import org.bukkit.Bukkit;

public class NMSUtil {

    static String packageName = Bukkit.getServer().getClass().getPackage().getName();
    static String version = packageName.split("\\.")[3];


    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String fullName = "net.minecraft.server." + version + "." + className;
        return Class.forName(fullName);
    }

    public static Class<?> getCraftBukkitClass(String className) throws ClassNotFoundException {
        String fullName = "org.bukkit.craftbukkit." + version + "." + className;
        return Class.forName(fullName);
    }


    public static String getVersion() {
        return version;
    }

}
