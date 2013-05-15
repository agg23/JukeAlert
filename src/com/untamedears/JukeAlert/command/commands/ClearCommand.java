package com.untamedears.JukeAlert.command.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.untamedears.JukeAlert.command.PlayerCommand;
import com.untamedears.JukeAlert.model.Snitch;
import org.bukkit.Bukkit;

public class ClearCommand extends PlayerCommand {

    public ClearCommand() {
        super("Clear");
        setDescription("Clears snitch logs");
        setUsage("/jaclear");
        setArgumentRange(0, 0);
        setIdentifier("jaclear");
    }

    @Override
    public boolean execute(final CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Set<Snitch> snitches = plugin.getSnitchManager().findSnitches(player.getWorld(), player.getLocation());
            for (final Snitch snitch : snitches) {
                //Get only first snitch in cuboid
                if (snitch.getGroup().isMember(player.getName()) || snitch.getGroup().isFounder(player.getName()) || snitch.getGroup().isModerator(player.getName())) {
                   Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                       @Override
                       public void run() {
                           deleteLog(sender, snitch);
                       }
                   });
                   break;
                }
            }

        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
        return false;
    }

    private void deleteLog(CommandSender sender, Snitch snitch) {
        Player player = (Player) sender;
        Boolean completed = plugin.getJaLogger().deleteSnitchInfo(snitch.getId());

        if (completed) {
            player.sendMessage(ChatColor.AQUA + "Snitch Cleared");
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Snitch Clear Failed");
        }
    }
}
