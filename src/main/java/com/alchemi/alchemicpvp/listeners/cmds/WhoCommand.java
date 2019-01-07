package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;

public class WhoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (main.instance.hasPermission(sender, "alchemicpvp.whois") && args.length > 0) {
			Player answer = whoIs(args[0]);
			if (answer == null) {
				main.messenger.sendMessage("Nick.NoOne", sender, new HashMap<String, Object>(){
					{
						put("$nick$", args[0]);
					}
				});
				return true;
			}
			main.messenger.sendMessage("Nick.Is", sender, new HashMap<String, Object>(){
				{
					put("$name$", answer.getDisplayName());
					put("$player$", answer.getName());
				}
			});
		}
		
		return true;
	}
	
	public static Player whoIs(String displayName) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getDisplayName().equals(displayName)) {
				return player;
			}
		}
		return null;
	}

}
