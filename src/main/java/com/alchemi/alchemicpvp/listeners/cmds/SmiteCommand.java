package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.Library;
import com.alchemi.al.objects.ItemFactory;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.ItemListeners;

public class SmiteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (main.instance.hasPermission(sender, "alchemicpvp.smite")) {
			Location smite;
			if (args.length == 0) {
				if (sender instanceof Player) {
					Library.giveItemStack(new ItemFactory(Material.STICK).setName("&5Magic wand"), ((Player)sender));
					new ItemListeners((Player) sender);
					smite = ((Player) sender).getLocation();
					main.messenger.sendMessage(MESSAGES.SMITE_SMITTEN.value(), sender);
					main.messenger.sendMessage(MESSAGES.SMITE_SMITESENT.value().replace("$player$", sender.getName()), sender);
				} else {
					main.messenger.sendMessage(MESSAGES.SMITE_PROVIDE.value(), sender);
					return true;
				}
			} else {
				if (Bukkit.getPlayer(args[0]) == null || !Bukkit.getPlayer(args[0]).isOnline()) {
					main.messenger.sendMessage(MESSAGES.SMITE_PLAYEROFFLINE.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				smite = Bukkit.getPlayer(args[0]).getLocation();
				
				main.messenger.sendMessage(MESSAGES.SMITE_SMITTEN.value(), Bukkit.getPlayer(args[0]));
				main.messenger.sendMessage(MESSAGES.SMITE_SMITESENT.value().replace("$player$", args[0]), sender);
				
			}
			
			smite.getWorld().strikeLightning(smite);
			
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
