package me.dkim19375.mobcontroller.plugin.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CreatureTypeUtils {
    private static final BiMap<EntityType, String> names = HashBiMap.create();
    private static final Set<String> validTypes = new HashSet<>();

    private CreatureTypeUtils() { }

    static {
        addValidTypes();
        EntityType[] entityTypes = EntityType.values();
        for (EntityType type : entityTypes) {
            if (!validTypes.contains(type.name())) {
                continue;
            }
            //noinspection deprecation
            final String name = type.getName();
            if (name == null) {
                continue;
            }
            names.put(type, format(name));
        }
    }

    @NotNull
    public static BiMap<EntityType, String> getLowerCaseTypes() {
        BiMap<EntityType, String> map = HashBiMap.create();
        for (Map.Entry<EntityType, String> e : names.entrySet()) {
            map.put(e.getKey(), e.getValue().toLowerCase(Locale.ENGLISH));
        }
        return map;
    }
    @NotNull
    public static Map<EntityType, String> getNames() {
        return names;
    }

    @NotNull
    public static BiMap<EntityType, String> getNamesBiMap() {
        return names;
    }

    @NotNull
    public static Map<String, EntityType> getTypes() {
        return names.inverse();
    }

    @NotNull
    public static BiMap<String, EntityType> getTypesBiMap() {
        return names.inverse();
    }

    @Nullable
    public static String getName(final EntityType entityType) {
        return getNames().get(entityType);
    }

    @NotNull
    public static Set<String> getTypesSet() {
        return names.values();
    }

    @NotNull
    private static String format(String s) {
        StringBuilder formatted = new StringBuilder();
        for (String string : s.split("_")) {
            formatted.append(string.substring(0, 1).toUpperCase()).append(string.substring(1));
        }
        return formatted.toString();
    }

    private static void addValidTypes() {
        validTypes.add("BAT");
        validTypes.add("BEE");
        validTypes.add("BLAZE");
        validTypes.add("CAT");
        validTypes.add("CAVE_SPIDER");
        validTypes.add("CHICKEN");
        validTypes.add("COD");
        validTypes.add("COW");
        validTypes.add("CREEPER");
        validTypes.add("DOLPHIN");
        validTypes.add("DONKEY");
        validTypes.add("DROWNED");
        validTypes.add("ELDER_GUARDIAN");
        validTypes.add("ENDER_DRAGON");
        validTypes.add("ENDERMAN");
        validTypes.add("ENDERMITE");
        validTypes.add("EVOKER");
        validTypes.add("FOX");
        validTypes.add("GHAST");
        validTypes.add("GIANT");
        validTypes.add("GUARDIAN");
        validTypes.add("HOGLIN");
        validTypes.add("HORSE");
        validTypes.add("HUSK");
        validTypes.add("ILLUSIONER");
        validTypes.add("IRON_GOLEM");
        validTypes.add("LLAMA");
        validTypes.add("MAGMA_CUBE");
        validTypes.add("MULE");
        validTypes.add("MUSHROOM_COW");
        validTypes.add("OCELOT");
        validTypes.add("PANDA");
        validTypes.add("PARROT");
        validTypes.add("PHANTOM");
        validTypes.add("PIG");
        validTypes.add("PIGLIN");
        validTypes.add("PIGLIN_BRUTE");
        validTypes.add("PILLAGER");
        validTypes.add("POLAR_BEAR");
        validTypes.add("PUFFERFISH");
        validTypes.add("RABBIT");
        validTypes.add("RAVAGER");
        validTypes.add("SALMON");
        validTypes.add("SHEEP");
        validTypes.add("SHULKER");
        validTypes.add("SILVERFISH");
        validTypes.add("SKELETON");
        validTypes.add("SKELETON_HORSE");
        validTypes.add("SLIME");
        validTypes.add("SNOWMAN");
        validTypes.add("SPIDER");
        validTypes.add("SQUID");
        validTypes.add("STRAY");
        validTypes.add("STRIDER");
        validTypes.add("TRADER_LLAMA");
        validTypes.add("TROPICAL_FISH");
        validTypes.add("TURTLE");
        validTypes.add("VEX");
        validTypes.add("VILLAGER");
        validTypes.add("VINDICATOR");
        validTypes.add("WANDERING_TRADER");
        validTypes.add("WITCH");
        validTypes.add("WITHER");
        validTypes.add("WITHER_SKELETON");
        validTypes.add("WOLF");
        validTypes.add("ZOGLIN");
        validTypes.add("ZOMBIE");
        validTypes.add("ZOMBIE_HORSE");
        validTypes.add("ZOMBIE_VILLAGER");
        validTypes.add("ZOMBIFIED_PIGLIN");
    }
}



