package me.dkim19375.mobcontroller.plugin.api;

import me.dkim19375.mobcontroller.api.MobControllerAPI;
import me.dkim19375.mobcontroller.plugin.MobController;

import java.util.Set;
import java.util.UUID;

public class MobControllerImpl implements MobControllerAPI {
    private final MobController plugin;

    public MobControllerImpl(MobController plugin) {
        this.plugin = plugin;
    }

    @Override
    public Set<UUID> getControlledMobs() {
        plugin.getController().update();
        return plugin.getController().getMobs();
    }

    @Override
    public boolean removeMob(UUID mob) {
        plugin.getController().update();
        boolean alreadyRemoved = !plugin.getController().getMobs().contains(mob);
        plugin.getController().removeMob(mob);
        return !alreadyRemoved;
    }

    @Override
    public boolean addMob(UUID mob) {
        plugin.getController().update();
        boolean alreadyAdded = plugin.getController().getMobs().contains(mob);
        plugin.getController().addMob(mob);
        return !alreadyAdded;
    }
}
