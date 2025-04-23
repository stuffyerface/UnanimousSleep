package me.stuffy.unanimousSleep;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelSleepCommand implements CommandExecutor {
    private final UnanimousSleepPlugin plugin;
    public CancelSleepCommand(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (org.bukkit.entity.Player) sender;
            World world = player.getWorld();
            plugin.getSleepManager().cancelSleepCommand(player, world);
            return true;
        }
        return false;
    }
}
