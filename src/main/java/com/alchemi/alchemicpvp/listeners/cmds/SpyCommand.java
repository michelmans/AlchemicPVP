package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.SpyListener;

public class SpyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.spy")) {
			
			
			if (args.length > 1 && args[0].equalsIgnoreCase("ignore") && main.getInstance().spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					main.getInstance().getMessenger().sendMessage(MESSAGES.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
					return true;
				}
				main.getInstance().spies.get(sender.getName()).ignorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			} else if (args.length > 1 && args[0].equalsIgnoreCase("unignore") && main.getInstance().spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					main.getInstance().getMessenger().sendMessage(MESSAGES.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
					return true;
				}
				main.getInstance().spies.get(sender.getName()).unIgnorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			}
			
			
			if (main.getInstance().spies.containsKey(sender.getName())) {
				main.getInstance().spies.remove(sender.getName());
				main.getInstance().getMessenger().sendMessage(MESSAGES.SPY_STOP.value(), sender);
			} else {
				main.getInstance().spies.put(sender.getName(), new SpyListener((Player) sender));
				main.getInstance().getMessenger().sendMessage(MESSAGES.SPY_START.value(), sender);
			}
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		return true;
	}
	
}
