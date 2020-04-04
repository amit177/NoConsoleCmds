package systems.amit.spigot.noconsolecmds;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class NoConsoleCmds extends JavaPlugin implements Listener {

    private ArrayList<String> blockedCmds = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!loadConfig()) {
            getLogger().severe("Could not load config, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("The plugin has been enabled (" + blockedCmds.size() + " blocked commands)");
    }

    @Override
    public void onDisable() {
        blockedCmds.clear();
        getLogger().info("The plugin has been disabled");
    }

    private boolean loadConfig() {
        saveDefaultConfig();
        if (!getConfig().contains("commands-blocked")) {
            getLogger().severe("The config is missing the 'commands-blocked' section");
            return false;
        }
        blockedCmds.addAll(getConfig().getStringList("commands-blocked"));
        if (blockedCmds.size() == 0) {
            getLogger().severe("No commands listed in config.");
            return false;
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(ServerCommandEvent e) {
        if (blockedCmds.contains(e.getCommand())) {
            e.setCancelled(true);
        }
    }
}
