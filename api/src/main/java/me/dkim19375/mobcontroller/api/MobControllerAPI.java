package me.dkim19375.mobcontroller.api;

import java.util.Set;
import java.util.UUID;

public interface MobControllerAPI {
    Set<UUID> getControlledMobs();

    boolean removeMob(UUID mob);

    boolean addMob(UUID mob);
}
