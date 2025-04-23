package me.stuffy.unanimousSleep;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;


public class SleepListener implements Listener {
    private final UnanimousSleepPlugin plugin;

    public SleepListener(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player sleeper = event.getPlayer();
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)){
            plugin.getSleepManager().tryToSleep(sleeper, sleeper.getWorld());
        }
    }
}
