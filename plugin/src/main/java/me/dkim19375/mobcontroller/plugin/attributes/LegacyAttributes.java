package me.dkim19375.mobcontroller.plugin.attributes;

import org.bukkit.entity.LivingEntity;

@SuppressWarnings("deprecation")
public class LegacyAttributes extends AbstractAttributes {

    @Override
    public void setMaxHealth(double health, LivingEntity entity) {
        entity.setMaxHealth(health);
        entity.setHealth(health);
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    @Override
    public double getDefaultMaxHealth(LivingEntity entity) {
        final double beforeMaxHealth = entity.getMaxHealth();
        final double beforeHealth = entity.getHealth();
        entity.resetMaxHealth();
        final double afterMaxHealth = entity.getMaxHealth();
        entity.setMaxHealth(beforeMaxHealth);
        entity.setHealth(beforeHealth);
        return afterMaxHealth;
    }
}
