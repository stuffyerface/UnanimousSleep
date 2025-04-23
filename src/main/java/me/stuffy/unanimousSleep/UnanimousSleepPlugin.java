package me.stuffy.unanimousSleep;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnanimousSleepPlugin extends JavaPlugin {

    private ProtocolManager protocolManager;
    private SleepManager sleepManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("UnanimousSleep has been enabled.");

        // Set game rule
        getServer().getWorlds().forEach(world -> {
            if (world.getEnvironment() == org.bukkit.World.Environment.NORMAL) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
                getLogger().info("Set vanilla player sleeping percentage in `" + world.getName() + "` to impossible.");
            }
        });

        sleepManager = new SleepManager(this);
        // Register Commands
        this.getCommand("cancelsleep").setExecutor(new CancelSleepCommand(this));

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("ProtocolLib has been loaded.");


        // Registering the event listeners
        getServer().getPluginManager().registerEvents(new SleepListener(this), this);
        getServer().getPluginManager().registerEvents(new TimeSkipEventListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveBedListener(this), this);

        // Hide action bar messages for players entering beds/night passing
        HideBedMessages hideBedMessages = new HideBedMessages(this);
        hideBedMessages.register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public SleepManager getSleepManager() {
        return sleepManager;
    }
}
