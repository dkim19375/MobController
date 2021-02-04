package me.dkim19375.mobcontroller.plugin;

import me.dkim19375.dkim19375core.ConfigFile;
import me.dkim19375.dkim19375core.CoreJavaPlugin;
import me.dkim19375.mobcontroller.api.MobControllerAPI;
import me.dkim19375.mobcontroller.plugin.api.MobControllerImpl;
import me.dkim19375.mobcontroller.plugin.commands.TabCompletionHandler;
import me.dkim19375.mobcontroller.plugin.commands.CommandHandler;
import me.dkim19375.mobcontroller.plugin.listeners.EntityDamageListener;
import me.dkim19375.mobcontroller.plugin.util.Controller;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import java.util.logging.Level;

public class MobController extends CoreJavaPlugin {
    private Controller controller;
    private final ConfigFile mobsFile = new ConfigFile(this, "mobs.yml");
    private static MobControllerAPI api;

    @Override
    public void onEnable() {
        api = new MobControllerImpl(this);
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
    public static MobControllerAPI getAPI() {
        return api;
    }
}
