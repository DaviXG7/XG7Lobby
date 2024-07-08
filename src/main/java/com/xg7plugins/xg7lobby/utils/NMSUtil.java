package com.xg7plugins.xg7lobby.utils;

import lombok.Getter;
import org.bukkit.Bukkit;

public class NMSUtil {
    static String packageName = Bukkit.getServer().getClass().getPackage().getName();
    @Getter
    static String version = packageName.substring(packageName.lastIndexOf('.') + 1);


    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String fullName = "net.minecraft.server." + version + "." + className;
        return Class.forName(fullName);
    }

    public static Class<?> getCraftBukkitClass(String className) throws ClassNotFoundException {
        String fullName = "org.bukkit.craftbukkit." + version + "." + className;
        return Class.forName(fullName);
    }


}
