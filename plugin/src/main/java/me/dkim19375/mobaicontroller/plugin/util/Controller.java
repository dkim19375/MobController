package me.dkim19375.mobaicontroller.plugin.util;

import me.dkim19375.mobaicontroller.plugin.MobAIController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Controller {
    private final Set<UUID> uuids = new HashSet<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final MobAIController plugin;
    private final Map<UUID, Location> location = new HashMap<>();
    private final Consumer<Boolean> task;

    public Controller(final MobAIController plugin) {
        this.plugin = plugin;
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
        if (!location.containsKey(entity.getUniqueId())) {
            location.put(entity.getUniqueId(), entity.getLocation());
            return;
        }
        final Location movedLocation = entity.getLocation();
        final Location loc = location.get(entity.getUniqueId());
        if (!movedLocation.equals(loc)) {
            final Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    movedLocation.getYaw(), movedLocation.getPitch());
            entity.teleport(newLoc);
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
        final Set<UUID> list = plugin.getMobsFile().getConfig().getStringList("mobs").stream().map(UUID::fromString).collect(Collectors.toSet());
        for (final UUID uuid : list) {
            final Entity entity = getEntity(uuid);
            if (entity == null) {
                list.remove(uuid);
                continue;
            }
            if (!CreatureTypeUtils.getNames().containsKey(entity.getType())) {
                continue;
            }
            ((LivingEntity) entity).setCanPickupItems(true);
        }
        uuids.clear();
        uuids.addAll(list);
    }

    public Map<UUID, Location> getLocations() {
        return location;
    }
}
