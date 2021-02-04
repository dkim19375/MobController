package me.dkim19375.mobcontroller.plugin.util;

import me.dkim19375.mobcontroller.plugin.MobController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class Controller {
    private final Set<UUID> uuids = new HashSet<>();
    private final MobController plugin;
    private final Consumer<Boolean> task;

    public Controller(final MobController plugin) {
        this.plugin = plugin;
        update();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (MobFileUtils.isModified()) {
                plugin.getMobsFile().save();
                MobFileUtils.setModified(false);
            }
        }, 1L, 1L);
        task = (async) -> {
            if (plugin.getConfig().getBoolean("async")) {
                if (!async) {
                    return;
                }
            } else {
                if (async) {
                    return;
                }
            }
            for (UUID uuid : uuids) {
                final Entity entity = getEntity(uuid);
                if (entity == null) {
                    continue;
                }
                if (!(entity instanceof LivingEntity)) {
                    continue;
                }
                final LivingEntity livingEntity = (LivingEntity) entity;
                if (!CreatureTypeUtils.getNames().containsKey(livingEntity.getType())) {
                    continue;
                }
                if (async) {
                    Bukkit.getScheduler().runTask(plugin, () -> useLocation(livingEntity));
                } else {
                    useLocation(livingEntity);
                }
            }
        };
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> task.accept(true), 1, 1);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(false), 1, 1);
    }

    public static Entity getEntity(UUID uuid) {
        try {
            return Bukkit.getEntity(uuid);
        } catch (NoSuchMethodError ignored) {
        }
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getUniqueId().equals(uuid)) {
                    return entity;
                }
            }
        }
        return null;
    }

    private void useLocation(final LivingEntity entity) {
        update();
        final Location movedLocation = entity.getLocation();
        Location loc = MobFileUtils.getLocation(plugin, entity.getUniqueId());
        if (loc == null) {
            MobFileUtils.setLocation(plugin, entity.getUniqueId(), entity.getLocation());
            loc = MobFileUtils.getLocation(plugin, entity.getUniqueId());
        }
        if (!movedLocation.equals(loc)) {
            entity.teleport(loc);
        }
    }

    @NotNull
    public Set<UUID> getMobs() {
        return uuids;
    }

    public void addMob(final UUID uuid) {
        uuids.add(uuid);
    }

    public void removeMob(final UUID uuid) {
        final Entity e = getEntity(uuid);
        if (e != null) {
            boolean enabled = Bukkit.getServer().spigot().getConfig().getBoolean("settings.log-villager-deaths");
            Bukkit.getServer().spigot().getConfig().set("settings.log-villager-deaths", false);
            e.remove();
            if (enabled) {
                Bukkit.getServer().spigot().getConfig().set("settings.log-villager-deaths", true);
            }
        }
        uuids.remove(uuid);
    }

    public void clearAndAddMobs(final Collection<? extends UUID> uuids) {
        this.uuids.clear();
        this.uuids.addAll(uuids);
    }

    public void update() {
        final Set<UUID> list = convertStringToUUID(plugin.getMobsFile().getConfig().getKeys(false));
        for (final UUID uuid : new HashSet<>(list)) {
            final Entity entity = getEntity(uuid);
            if (entity == null) {
                list.remove(uuid);
                continue;
            }
            if (!CreatureTypeUtils.getNames().containsKey(entity.getType())) {
                continue;
            }
            ((LivingEntity) entity).setCanPickupItems(true);
            if (MobFileUtils.getLocation(plugin, uuid) == null) {
                MobFileUtils.setLocation(plugin, uuid, entity.getLocation());
            }
        }
        for (UUID uuid : uuids) {
            MobFileUtils.getConfigurationSection(plugin, uuid);
        }
        clearAndAddMobs(list);
    }

    private Set<UUID> convertStringToUUID(Iterable<String> strings) {
        Set<UUID> uuidSet = new HashSet<>();
        for (String str : strings) {
            try {
                //noinspection ResultOfMethodCallIgnored
                UUID.fromString(str);
            } catch (IllegalArgumentException ignored) {
                continue;
            }
            uuidSet.add(UUID.fromString(str));
        }
        return uuidSet;
    }
}
