package me.stuffy.unanimousSleep;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SleepManager {
    private final UnanimousSleepPlugin plugin;
    private final Map<World, Player> attemptingToSleep = new HashMap<>();
    private final Map<World, BukkitTask> skipNightTasks = new HashMap<>();


    public SleepManager(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    public void cancelSleepCommand(Player player, World world) {
        if(!attemptingToSleep.containsKey(world)) {
            // No one is trying to sleep in this world
            player.sendMessage("There is no sleep to cancel right now.");
            return;
        }

        if(attemptingToSleep.get(world) == player) {
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
        attemptingToSleep.remove(world);
    }

    public void tryToSleep(Player player, World world) {
        if(attemptingToSleep.containsKey(world)) {
            // Someone is already trying to sleep in this world
            return;
        }
        Component chatNotification = Component.text()
                .append(Component.text(player.getName()).color(NamedTextColor.GRAY))
                .append(Component.text(" is sleeping through this night. ").color(NamedTextColor.GRAY))
                .append(Component.text("Click").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD).clickEvent(
                        ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cancelsleep")
                ).hoverEvent(
                        HoverEvent.showText(
                                Component.text()
                                        .append(Component.text("Click here to cancel sleep.\n").color(NamedTextColor.YELLOW))
                                        .append(Component.text("Runs /cancelsleep.").color(NamedTextColor.GRAY))
                        )
                ))
                .append(Component.text(" to cancel.").color(NamedTextColor.GRAY))
                .build();

        world.getPlayers().forEach(playerInWorld -> {
                    if (playerInWorld.equals(player)) {
                        playerInWorld.sendActionBar(Component.text("Sleeping through this night. "));
                        return;
                    }
                    playerInWorld.sendMessage(chatNotification);
                });

        attemptingToSleep.put(world, player);

        skipNightTasks.put(world, plugin.getServer().getScheduler().runTaskLater(plugin, () -> skipNight(world), 100L));
    }

    public void exitBed(Player player, World world) {
        if(!attemptingToSleep.containsKey(world) || !attemptingToSleep.get(world).equals(player)) {
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
        attemptingToSleep.remove(world);
//        plugin.getLogger().info("Skipping night in world " + world.getName());
        world.setTime(0); // Set time to day
    }

    private void cancelSleepTask(World world) {
        attemptingToSleep.remove(world);
        BukkitTask task = skipNightTasks.get(world);
        if (task != null) {
            task.cancel();
            skipNightTasks.remove(world);
        } else {
            plugin.getLogger().warning("No sleep task found for world " + world.getName());
        }
    }

    public UnanimousSleepPlugin getPlugin() {
        return plugin;
    }
}
