package me.stuffy.unanimousSleep;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SleepManager {
    private final UnanimousSleepPlugin plugin;
    private final Map<UUID, Player> attemptingToSleep = new HashMap<>();
    private final Map<UUID, BukkitTask> skipNightTasks = new HashMap<>();


    public SleepManager(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    public void cancelSleepCommand(Player player, World world) {
        UUID worldId = world.getUID();
        if(!attemptingToSleep.containsKey(worldId)) {
            // No one is trying to sleep in this world
            player.sendMessage("There is no sleep to cancel right now.");
            return;
        }

        if(attemptingToSleep.get(worldId) == player) {
            player.sendMessage("You can't cancel your own sleep.");
            return;
        }

        cancelSleepTask(world);
        // Tell all players in the world who cancelled the sleep
        Component actionBar = Component.text()
                .append(Component.text("Sleep cancelled by "))
                .append(Component.text(player.getName()))
                .build();
        player.getWorld().getPlayers().forEach(playerInWorld -> {
            playerInWorld.sendActionBar(actionBar);
        });
        attemptingToSleep.remove(worldId);
    }

    public void tryToSleep(Player player, World world) {
        UUID worldId = world.getUID();
        if(attemptingToSleep.containsKey(worldId)) {
            // Someone is already trying to sleep in this world
            return;
        }
        Component actionBar = Component.text()
                .append(Component.text(player.getName()))
                .append(Component.text(" is sleeping through this night. "))
                .append(Component.text("/cancelsleep").color(NamedTextColor.RED))
                .append(Component.text(" to cancel."))
                .build();

        world.getPlayers().forEach(playerInWorld -> {
                    if (playerInWorld.equals(player)) {
                        playerInWorld.sendActionBar(Component.text("Sleeping through this night. "));
                        return;
                    }
                    playerInWorld.sendActionBar(actionBar);
                });

        attemptingToSleep.put(worldId, player);

        skipNightTasks.put(worldId, plugin.getServer().getScheduler().runTaskLater(plugin, () -> skipNight(world), 100L));
    }

    public void exitBed(Player player, World world) {
        UUID worldId = world.getUID();
        if(!attemptingToSleep.containsKey(worldId) || !attemptingToSleep.get(worldId).equals(player)) {
            return;
        }
        cancelSleepTask(world);
        Component actionBar = Component.text()
                .append(Component.text(player.getName()))
                .append(Component.text(" left their bed. Sleep cancelled. "))
                .build();

        world.getPlayers().forEach(playerInWorld -> {
            if (playerInWorld.equals(player)) {
                playerInWorld.sendActionBar(Component.text("You left your bed. Sleep cancelled."));
                return;
            }
            playerInWorld.sendActionBar(actionBar);
        });
    }

    private void skipNight(World world) {
        UUID worldId = world.getUID();
        attemptingToSleep.remove(worldId);
//        plugin.getLogger().info("Skipping night in world " + world.getName());
        world.setTime(0); // Set time to day

        // Clear weather if raining or thundering
        if(!world.isClearWeather()){
            world.setStorm(false);
            world.setThundering(false);
        }
    }

    private void cancelSleepTask(World world) {
        UUID worldId = world.getUID();
        attemptingToSleep.remove(worldId);
        BukkitTask task = skipNightTasks.get(worldId);
        if (task != null) {
            task.cancel();
            skipNightTasks.remove(worldId);
        } else {
            plugin.getLogger().warning("No sleep task found for world " + world.getName());
        }
    }

    public UnanimousSleepPlugin getPlugin() {
        return plugin;
    }
}
