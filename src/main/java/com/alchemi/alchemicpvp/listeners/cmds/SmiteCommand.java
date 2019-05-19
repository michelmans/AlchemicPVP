package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;

public class SmiteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (main.getInstance().hasPermission(sender, "alchemicpvp.smite")) {
			Location smite;
			if (args.length == 0) {
				if (sender instanceof Player) {
					smite = ((Player) sender).getLocation();
					main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_SMITTEN.value(), sender);
					main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_SMITESENT.value().replace("$player$", sender.getName()), sender);
				} else {
					main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_PROVIDE.value(), sender);
					return true;
				}
			} else {
				if (Bukkit.getPlayer(args[0]) == null || !Bukkit.getPlayer(args[0]).isOnline()) {
					main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_PLAYEROFFLINE.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				smite = Bukkit.getPlayer(args[0]).getLocation();
				
				main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_SMITTEN.value(), Bukkit.getPlayer(args[0]));
				main.getInstance().getMessenger().sendMessage(MESSAGES.SMITE_SMITESENT.value().replace("$player$", args[0]), sender);
				
			}
			
			smite.getWorld().strikeLightning(smite);
			
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
