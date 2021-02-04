package me.dkim19375.mobcontroller.plugin.attributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class NewAttributes extends AbstractAttributes {

    @Override
    public void setMaxHealth(double health, LivingEntity entity) {
        final AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance == null) {
            return;
        }
        instance.setBaseValue(health);
        entity.setHealth(health);
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        final AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance == null) {
            return 0;
        }
        return instance.getBaseValue();
    }

    @Override
    public double getDefaultMaxHealth(LivingEntity entity) {
        final AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance == null) {
            return 0;
        }
        return instance.getDefaultValue();
    }
}
