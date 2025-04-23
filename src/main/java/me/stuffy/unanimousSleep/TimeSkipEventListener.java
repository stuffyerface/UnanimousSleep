package me.stuffy.unanimousSleep;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class TimeSkipEventListener implements Listener {
    private final UnanimousSleepPlugin plugin;

    public TimeSkipEventListener(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTimeSkipEvent(TimeSkipEvent event) {
        // Handle the time skip event here
        if(event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            event.setCancelled(true); // Cancel the time skip event
        }
    }
}
