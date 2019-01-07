package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.SpyListener;

public class SpyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.spy")) {
			
			
			if (args.length > 1 && args[0].equalsIgnoreCase("ignore") && main.instance.spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					main.messenger.sendMessage("Message.PlayerOffline", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[1]);
						}
					});
					return true;
				}
				main.instance.spies.get(sender.getName()).ignorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			} else if (args.length > 1 && args[0].equalsIgnoreCase("unignore") && main.instance.spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					main.messenger.sendMessage("Message.PlayerOffline", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[1]);
						}
					});
					return true;
				}
				main.instance.spies.get(sender.getName()).unIgnorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			}
			
			
			if (main.instance.spies.containsKey(sender.getName())) {
				main.instance.spies.remove(sender.getName());
				main.messenger.sendMessage("Spy.Stop", sender);
			} else {
				main.instance.spies.put(sender.getName(), new SpyListener((Player) sender));
				main.messenger.sendMessage("Spy.Start", sender);
			}
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/socialspy");
				}
			});
		}
		return true;
	}
	
}
