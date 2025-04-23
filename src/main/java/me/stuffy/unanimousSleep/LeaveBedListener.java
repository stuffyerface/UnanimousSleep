package me.stuffy.unanimousSleep;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class LeaveBedListener implements Listener {
    private final UnanimousSleepPlugin plugin;

    public LeaveBedListener(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        Player sleeper = event.getPlayer();
        World world = sleeper.getWorld();
        plugin.getSleepManager().exitBed(sleeper, world);
    }
}
