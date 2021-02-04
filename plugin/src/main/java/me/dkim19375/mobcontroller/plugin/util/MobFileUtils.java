package me.dkim19375.mobcontroller.plugin.util;

import me.dkim19375.mobcontroller.plugin.MobController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EntityEquipment;

import java.util.UUID;

@SuppressWarnings("unused")
public class MobFileUtils {
    private MobFileUtils() {}

    private static boolean modified = false;

    public static boolean isModified() {
        return modified;
    }

    public static void setModified(boolean isModified) {
        modified = isModified;
    }

    public static void addMobToFile(final MobController plugin, UUID mob) {
        getConfigurationSection(plugin, mob);
    }

    public static void removeMobFromFile(final MobController plugin, UUID mob) {
        if (getConfigurationSection(plugin).contains(mob.toString())) {
            getConfigurationSection(plugin).set(mob.toString(), null);
        }
    }

    public static void setInvincible(final MobController plugin, final UUID mob, final boolean invincible) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        section.set("invincible", invincible);
        modified = true;
    }

    public static boolean isInvincible(final MobController plugin, final UUID mob) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        return section.getBoolean("invincible");
    }

    public static void setEntityEquipment(final MobController plugin, final UUID mob, final EntityEquipment equipment) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        section.set("entity-equipment", equipment);
        modified = true;
    }

    public static EntityEquipment getEntityEquipment(final MobController plugin, final UUID mob) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        try {
            return ((EntityEquipment) section.get("entity-equipment"));
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static void setLocation(final MobController plugin, final UUID mob, final Location loc) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        section.set("location", loc);
        modified = true;
    }

    public static Location getLocation(final MobController plugin, final UUID mob) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        return section.getLocation("location");
    }

    public static ConfigurationSection getConfigurationSection(final MobController plugin) {
        return plugin.getMobsFile().getConfig();
    }

    public static ConfigurationSection getConfigurationSection(final MobController plugin, final UUID mob) {
        ConfigurationSection section = plugin.getMobsFile().getConfig().getConfigurationSection(mob.toString()) == null
                ? plugin.getMobsFile().getConfig().createSection(mob.toString())
                : plugin.getMobsFile().getConfig().getConfigurationSection(mob.toString());
        modified = true;
        return section;
    }
}
