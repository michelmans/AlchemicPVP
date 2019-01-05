package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.PlayerStats;
import com.alchemi.alchemicpvp.main;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.reload")) {
			
			for (String file : main.instance.fileManager.getFiles().keySet()) {
				main.instance.fileManager.reloadConfig(file);
			}
			for (PlayerStats stats : main.instance.getAllStats()) {
				stats.reload();
			}
			main.instance.config = main.instance.fileManager.getConfig("config.yml");
			main.instance.messenger.sendMessage("Reload", sender);
			
		} else if (sender instanceof Player) {
			main.instance.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/statsreload");
				}
			});
		}
		
		return true;
	}

}
