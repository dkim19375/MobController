package me.dkim19375.mobcontroller.plugin.attributes;

import org.bukkit.entity.LivingEntity;

public abstract class AbstractAttributes {
    private static AbstractAttributes attributeInstance;

    public static AbstractAttributes getInstance() {
        if (attributeInstance != null) {
            return attributeInstance;
        }
        if (VersionParser.isLegacy()) {
            attributeInstance = new LegacyAttributes();
        } else {
            attributeInstance = new NewAttributes();
        }
        return attributeInstance;
    }

    public abstract void setMaxHealth(double health, LivingEntity entity);

    public abstract double getMaxHealth(LivingEntity entity);

    public abstract double getDefaultMaxHealth(LivingEntity entity);
}
