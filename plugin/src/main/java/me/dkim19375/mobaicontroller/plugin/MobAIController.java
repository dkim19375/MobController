package me.dkim19375.mobaicontroller.plugin;

import me.dkim19375.dkim19375core.ConfigFile;
import me.dkim19375.dkim19375core.CoreJavaPlugin;
import me.dkim19375.mobaicontroller.plugin.commands.CommandHandler;
import me.dkim19375.mobaicontroller.plugin.util.Controller;
import me.dkim19375.mobaicontroller.api.MobAIControllerAPI;
import me.dkim19375.mobaicontroller.plugin.api.MobAIControllerImpl;
import me.dkim19375.mobaicontroller.plugin.commands.TabCompletionHandler;
import me.dkim19375.mobaicontroller.plugin.listeners.EntityDamageListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import java.util.logging.Level;

public class MobAIController extends CoreJavaPlugin {
    private Controller controller;
    private final ConfigFile mobsFile = new ConfigFile(this, "mobs.yml");
    private static MobAIControllerAPI api;

    @Override
    public void onEnable() {
        api = new MobAIControllerImpl(this);
        controller = new Controller(this);
        saveDefaultConfig();
        mobsFile.createConfig();
        PluginCommand command = getCommand("mobaicontroller");
        if (command == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not get command!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        command.setExecutor(new CommandHandler(this));
        command.setTabCompleter(new TabCompletionHandler(this));
    }

    @Override
    public void onDisable() {

    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        mobsFile.createConfig();
        mobsFile.reload();
    }

    public ConfigFile getMobsFile() {
        return mobsFile;
    }

    @SuppressWarnings("unused")
    public static MobAIControllerAPI getAPI() {
        return api;
    }
}
