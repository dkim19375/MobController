package me.dkim19375.mobcontroller.plugin.listeners;

import me.dkim19375.mobcontroller.plugin.MobController;
import me.dkim19375.mobcontroller.plugin.attributes.AbstractAttributes;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class EntityDamageListener implements Listener {
    private final MobController plugin;


    public EntityDamageListener(MobController plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        plugin.getController().update();
        final UUID uuid = e.getEntity().getUniqueId();
        if (!plugin.getController().getMobs().contains(uuid)) {
            return;
        }
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        e.setDamage(0);
        LivingEntity entity = (LivingEntity) e.getEntity();
        AbstractAttributes abstractAttributes = AbstractAttributes.getInstance();
        if (!plugin.getConfig().getBoolean("no-damage")) {
            abstractAttributes.setMaxHealth(abstractAttributes.getDefaultMaxHealth(entity), entity);
            entity.setHealth(abstractAttributes.getDefaultMaxHealth(entity));
            return;
        }
        abstractAttributes.setMaxHealth(2048, entity);
        entity.setHealth(2048);
    }
}
