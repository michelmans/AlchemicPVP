package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;

public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		
		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.spawn")) {
			
			Player player = (Player) sender;
			main.instance.config.set("SPAWN", player.getLocation());
			main.instance.fileManager.saveConfig("config.yml");
			main.instance.messenger.sendMessage("Check.Spawn", player, new HashMap<String, Object>(){
				{
					put("$x$", player.getLocation().getBlockX());
					put("$y$", player.getLocation().getBlockY());
					put("$z$", player.getLocation().getBlockZ());
				}
			});
			
		} else if (sender instanceof Player) {
			main.instance.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/setcheckspawn");
				}
			});
		}
		
		return true;
	}

}
