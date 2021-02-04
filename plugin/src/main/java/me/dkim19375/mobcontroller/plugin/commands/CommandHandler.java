package me.dkim19375.mobcontroller.plugin.commands;

import me.dkim19375.dkim19375core.NumberUtils;
import me.dkim19375.mobcontroller.plugin.MobController;
import me.dkim19375.mobcontroller.plugin.util.Controller;
import me.dkim19375.mobcontroller.plugin.util.CreatureTypeUtils;
import me.dkim19375.mobcontroller.plugin.util.MobFileUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor {
    private static final String NO_PERMISSION = ChatColor.RED + "You do not have permission to run this command!";
    private static final String TOO_MANY_ARGS = ChatColor.RED + "Too many arguments!";
    private static final String LITTLE_ARGS = ChatColor.RED + "Not enough arguments!";
    private final MobController plugin;
    private final Random random = new Random();

    public CommandHandler(MobController plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("MobAIController")) {
            return false;
        }
        plugin.getController().update();
        if (args.length > 2) {
            sender.sendMessage(TOO_MANY_ARGS);
            showHelp(sender, label);
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(LITTLE_ARGS);
            showHelp(sender, label);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                showHelp(sender, label);
                return true;
            case "spawn":
                if (!sender.hasPermission("mobaicontroller.spawn.random")
                        && !sender.hasPermission("mobaicontroller.spawn.mobtype")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(LITTLE_ARGS);
                    showHelp(sender, label);
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "You must be a player!");
                    showHelp(sender, label);
                    return true;
                }
                final Player p = (Player) sender;
                if (args[1].equalsIgnoreCase("random")) {
                    if (!sender.hasPermission("mobaicontroller.spawn.random")) {
                        sender.sendMessage(NO_PERMISSION);
                        return true;
                    }
                    LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), getRandomEntityType());
                    plugin.getController().getMobs().add(entity.getUniqueId());
                    MobFileUtils.addMobToFile(plugin, entity.getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + "Successfully spawned a random mob!");
                    return true;
                }
                if (!sender.hasPermission("mobaicontroller.spawn.mobtype")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (!CreatureTypeUtils.getLowerCaseTypes().containsValue(args[1].toLowerCase(Locale.ENGLISH))) {
                    sender.sendMessage(ChatColor.RED + "Invalid mob!");
                    showHelp(sender, label);
                    return true;
                }
                final EntityType creatureType = CreatureTypeUtils.getLowerCaseTypes().inverse().get(args[1].toLowerCase(Locale.ENGLISH));
                LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), creatureType);
                plugin.getController().getMobs().add(entity.getUniqueId());
                MobFileUtils.addMobToFile(plugin, entity.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Successfully spawned a mob!");
                return true;
            case "list":
                if (!sender.hasPermission("mobaicontroller.list")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (args.length > 1) {
                    sender.sendMessage(TOO_MANY_ARGS);
                    showHelp(sender, label);
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Mobs:");
                if (sender instanceof Player) {
                    if (NumberUtils.percentChance(80)) {
                        sender.sendMessage(ChatColor.GREEN + "Tip: You can click one of the UUIDs to copy it! (The uuid will show in the chat)");
                    }
                }
                final Set<Entity> mobs = new HashSet<>();
                for (UUID uuid : plugin.getController().getMobs()) {
                    if (Controller.getEntity(uuid) == null) {
                        plugin.getController().removeMob(uuid);
                        continue;
                    }
                    mobs.add(Controller.getEntity(uuid));
                }
                for (Entity entity1 : mobs) {
                    final TextComponent message = new TextComponent(ChatColor.AQUA + "UUID: " + ChatColor.GREEN + entity1.getUniqueId().toString());
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, entity1.getUniqueId().toString()));
                    if (sender instanceof Player) {
                        ((Player) sender).spigot().sendMessage(message);
                        return true;
                    }
                    sender.sendMessage(ChatColor.AQUA + "UUID: " + ChatColor.GREEN + entity1.getUniqueId().toString());
                }
                return true;
            case "info":
                if (!sender.hasPermission("mobaicontroller.info")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(LITTLE_ARGS);
                    showHelp(sender, label);
                    return true;
                }
                plugin.getController().update();
                if (!plugin.getController().getMobs().contains(UUID.fromString(args[1]))) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid UUID!");
                    showHelp(sender, label);
                    return true;
                }
                Entity entity2 = Controller.getEntity(UUID.fromString(args[1]));
                if (entity2 == null) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid UUID!");
                    showHelp(sender, label);
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "UUID: " + entity2.getUniqueId().toString() + "\nLocation: "
                        + entity2.getWorld().getName() + ", "
                        + entity2.getLocation().getX() + ", "
                        + entity2.getLocation().getY() + ", "
                        + entity2.getLocation().getZ());
                return true;
            case "remove":
                if (!sender.hasPermission("mobaicontroller.remove.nearest")
                        && !sender.hasPermission("mobaicontroller.remove.farthest")
                        && !sender.hasPermission("mobaicontroller.remove.random")
                        && !sender.hasPermission("mobaicontroller.remove.all")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(LITTLE_ARGS);
                    showHelp(sender, label);
                    return true;
                }
                plugin.getController().update();
                switch (args[1].toLowerCase(Locale.ENGLISH)) {
                    case "nearest":
                        if (!sender.hasPermission("mobaicontroller.remove.nearest")) {
                            sender.sendMessage(NO_PERMISSION);
                            return true;
                        }
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player!");
                            showHelp(sender, label);
                            return true;
                        }
                        final Player pl = (Player) sender;
                        Set<Entity> entities = plugin.getController().getMobs().stream().map(Controller::getEntity).collect(Collectors.toSet());
                        Entity closestEntity = null;
                        for (Entity entity1 : entities) {
                            if (closestEntity == null) {
                                closestEntity = entity1;
                                continue;
                            }
                            if (pl.getLocation().distance(entity1.getLocation())
                                    < pl.getLocation().distance(closestEntity.getLocation())) {
                                closestEntity = entity1;
                            }
                        }
                        if (closestEntity == null) {
                            sender.sendMessage(ChatColor.RED + "Could not find any mobs!");
                            return true;
                        }
                        plugin.getController().removeMob(closestEntity.getUniqueId());
                        MobFileUtils.removeMobFromFile(plugin, closestEntity.getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed the nearest mob!");
                        return true;
                    case "farthest":
                        if (!sender.hasPermission("mobaicontroller.remove.farthest")) {
                            sender.sendMessage(NO_PERMISSION);
                            return true;
                        }
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player!");
                            showHelp(sender, label);
                            return true;
                        }
                        final Player play = (Player) sender;
                        Set<Entity> entitySet = plugin.getController().getMobs().stream().map(Controller::getEntity).collect(Collectors.toSet());
                        Entity farthestEntity = null;
                        for (Entity entity1 : entitySet) {
                            if (farthestEntity == null) {
                                farthestEntity = entity1;
                                continue;
                            }
                            if (play.getLocation().distance(entity1.getLocation())
                                    > play.getLocation().distance(farthestEntity.getLocation())) {
                                farthestEntity = entity1;
                            }
                        }
                        if (farthestEntity == null) {
                            sender.sendMessage(ChatColor.RED + "Could not find any mobs!");
                            return true;
                        }
                        plugin.getController().removeMob(farthestEntity.getUniqueId());
                        MobFileUtils.removeMobFromFile(plugin, farthestEntity.getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed the farthest mob!");
                        return true;
                    case "random":
                        if (!sender.hasPermission("mobaicontroller.remove.random")) {
                            sender.sendMessage(NO_PERMISSION);
                            return true;
                        }
                        Entity[] entities1 = plugin.getController().getMobs().stream().map(Controller::getEntity).distinct().toArray(Entity[]::new);
                        Entity entity1 = entities1[random.nextInt(entities1.length)];
                        plugin.getController().removeMob(entity1.getUniqueId());
                        MobFileUtils.removeMobFromFile(plugin, entity1.getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed a random mob!");
                        return true;
                    case "all":
                        if (!sender.hasPermission("mobaicontroller.remove.all")) {
                            sender.sendMessage(NO_PERMISSION);
                            return true;
                        }
                        if (plugin.getController().getMobs().size() < 1) {
                            sender.sendMessage(ChatColor.RED + "There are no mobs!");
                            return true;
                        }
                        int count = 0;
                        Set<UUID> uuidSet = new HashSet<>(plugin.getController().getMobs());
                        for (UUID uuid : uuidSet) {
                            plugin.getController().removeMob(uuid);
                            MobFileUtils.removeMobFromFile(plugin, uuid);
                            count++;
                        }
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed all mobs! (" + count + " mobs)");
                        return true;
                }
                if (!sender.hasPermission("mobaicontroller.remove.uuid")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (!plugin.getController().getMobs().contains(UUID.fromString(args[1]))) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid UUID!");
                    showHelp(sender, label);
                    return true;
                }
                plugin.getController().removeMob(UUID.fromString(args[1]));
                MobFileUtils.removeMobFromFile(plugin, UUID.fromString(args[1]));
                sender.sendMessage(ChatColor.GREEN + "Successfully removed the mob!");
                return true;
            case "reload":
                if (!sender.hasPermission("mobaicontroller.reload")) {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                if (args.length > 1) {
                    sender.sendMessage(TOO_MANY_ARGS);
                    showHelp(sender, label);
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Successfully reloaded the config!");
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid argument!");
                showHelp(sender, label);
                return true;
        }
    }

    private void showHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.AQUA + "MobAIController Commands");
        sender.sendMessage(ChatColor.AQUA + "/" + label + " help - Show help menu");
        sender.sendMessage(ChatColor.AQUA + "/" + label + " spawn <<mob type>/random> - Spawn a mob");
        sender.sendMessage(ChatColor.AQUA + "/" + label + " list - List all mobs");
        sender.sendMessage(ChatColor.AQUA + "/" + label + " info <uuid> - Get more information about a mob");
        sender.sendMessage(ChatColor.AQUA + "/" + label + " remove <<uuid>/nearest/farthest/random> - Remove a mob");
    }


    private EntityType getRandomEntityType() {
        EntityType[] creatureTypes = CreatureTypeUtils.getNames().keySet().toArray(new EntityType[0]);
        int random = this.random.nextInt(creatureTypes.length);
        return creatureTypes[random];
    }
}
