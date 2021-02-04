package me.dkim19375.mobcontroller.plugin.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import me.dkim19375.mobcontroller.plugin.MobController;
import me.dkim19375.mobcontroller.plugin.util.CreatureTypeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TabCompletionHandler implements TabCompleter {
    private final MobController plugin;

    private final HashMultimap<String, String> completesListMap;

    public TabCompletionHandler(MobController plugin) {
        this.plugin = plugin;
        completesListMap = HashMultimap.create();
        add("core", "help", "spawn", "list", "reload", "remove", "info");
        add("mobTypes", combine(CreatureTypeUtils.getTypesSet(), "random"));
    }

    private Set<String> combine(Set<String> set, String... varargs) {
        Set<String> newSet = new HashSet<>();
        newSet.addAll(set);
        newSet.addAll(Arrays.asList(varargs));
        return newSet;
    }

    public Set<String> getUUIDS() {
        plugin.getController().update();
        final Set<UUID> uuidSet = new HashSet<>(plugin.getController().getMobs());
        final Set<String> stringSet = new HashSet<>();
        for (UUID uuid : uuidSet) {
            stringSet.add(uuid.toString());
        }
        return stringSet;
    }

    private void add(@SuppressWarnings("SameParameterValue") String key, String... args) { completesListMap.putAll(key, Arrays.asList(args)); }

    private void add(@SuppressWarnings("SameParameterValue") String key, Collection<String> args) { completesListMap.putAll(key, args); }

    private List<String> getPartial(String token, Iterable<String> collection) {
        return StringUtil.copyPartialMatches(token, collection, new ArrayList<>());
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        switch (args.length) {
            case 0:
                return Lists.newArrayList(completesListMap.get("core"));
            case 1: return getPartial(args[0], completesListMap.get("core"));
            case 2:
                if (args[0].equalsIgnoreCase("spawn")) {
                    return getPartial(args[1], completesListMap.get("mobTypes"));
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    return getPartial(args[1], combine(getUUIDS(), "nearest", "farthest", "random", "all"));
                }
                if (args[0].equalsIgnoreCase("info")) {
                    return getPartial(args[1], getUUIDS());
                }
            default: return ImmutableList.of();
        }
    }
}