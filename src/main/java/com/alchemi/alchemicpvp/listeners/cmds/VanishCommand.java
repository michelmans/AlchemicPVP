package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.CHECK;

public class VanishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.vanish")) {
			
			CHECK.vanishToggle((Player) sender);
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/vanish");
				}
			});
		}
		
		return true;
	}

}
