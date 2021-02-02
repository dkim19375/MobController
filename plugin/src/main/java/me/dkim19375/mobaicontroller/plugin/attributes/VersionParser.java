package me.dkim19375.mobaicontroller.plugin.attributes;

import org.bukkit.Bukkit;

public class VersionParser {
    private static String VERSION;
    private static Boolean LEGACY;

    public static String getVersion() {
        if (VERSION == null) {
            VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].split("_")[1];
        }
        return VERSION;
    }

    public static boolean isLegacy() {
        if (LEGACY == null) {
            LEGACY = Integer.parseInt(getVersion()) < 9;
        }
        return LEGACY;
    }
}
