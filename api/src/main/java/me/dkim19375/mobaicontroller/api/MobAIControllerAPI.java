package me.dkim19375.mobaicontroller.api;

import java.util.Set;
import java.util.UUID;

public interface MobAIControllerAPI {
    Set<UUID> getControlledMobs();

    boolean removeMob(UUID mob);

    boolean addMob(UUID mob);
}
