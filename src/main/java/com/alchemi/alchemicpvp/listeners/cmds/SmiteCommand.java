package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;

public class SmiteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (main.instance.hasPermission(sender, "alchemicpvp.smite")) {
			Location smite;
			if (args.length == 0) {
				if (sender instanceof Player) {
					smite = ((Player) sender).getLocation();
					main.instance.messenger.sendMessage("Smite.Smitten", sender);
					main.instance.messenger.sendMessage("Smite.SmiteSent", sender, new HashMap<String, Object>(){
						{
							put("$player$", sender.getName());
						}
					});
				} else {
					main.instance.messenger.sendMessage("Smite.Provide", sender);
					return true;
				}
			} else {
				if (Bukkit.getPlayer(args[0]) == null || !Bukkit.getPlayer(args[0]).isOnline()) {
					main.instance.messenger.sendMessage("Smite.PlayerOffline", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[0]);
						}
					});
					return true;
				}
				
				smite = Bukkit.getPlayer(args[0]).getLocation();
				
				main.instance.messenger.sendMessage("Smite.Smitten", Bukkit.getPlayer(args[0]));
				main.instance.messenger.sendMessage("Smite.SmiteSent", sender, new HashMap<String, Object>(){
					{
						put("$player$", args[0]);
					}
				});
				
			}
			
			smite.getWorld().strikeLightning(smite);
			
			
		}
		
		return true;
	}

}
