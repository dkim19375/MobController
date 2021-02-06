package me.dkim19375.mobaicontroller.plugin.api;

import me.dkim19375.mobaicontroller.plugin.MobAIController;
import me.dkim19375.mobaicontroller.api.MobAIControllerAPI;

import java.util.Set;
import java.util.UUID;

public class MobAIControllerImpl implements MobAIControllerAPI {
    private final MobAIController plugin;

    public MobAIControllerImpl(MobAIController plugin) {
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
