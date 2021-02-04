package me.dkim19375.mobcontroller.plugin.util;

import me.dkim19375.mobcontroller.plugin.MobController;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EntityEquipment;

import java.util.UUID;

@SuppressWarnings("unused")
public class MobFileUtils {
    private MobFileUtils() {}

    public static void setInvincible(final MobController plugin, final UUID mob, final boolean invincible) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        section.set("invincible", invincible);
        plugin.getMobsFile().save();
    }

    public static boolean isInvincible(final MobController plugin, final UUID mob) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        return section.getBoolean("invincible");
    }

    public static void setEntityEquipment(final MobController plugin, final UUID mob, final EntityEquipment equipment) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        section.set("entity-equipment", equipment);
        plugin.getMobsFile().save();
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
        plugin.getMobsFile().save();
    }

    public static Location getLocation(final MobController plugin, final UUID mob) {
        final ConfigurationSection section = getConfigurationSection(plugin, mob);
        return section.getLocation("location");
    }

    public static ConfigurationSection getConfigurationSection(final MobController plugin, final UUID mob) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(mob.toString()) == null
                ? plugin.getConfig().createSection(mob.toString())
                : plugin.getConfig().getConfigurationSection(mob.toString());
        plugin.getMobsFile().save();
        return section;
    }
}
