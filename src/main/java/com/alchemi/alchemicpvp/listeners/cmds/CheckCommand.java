package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.CHECK;

public class CheckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.check")) {
			
			Player player = (Player) sender;
			
			if (main.instance.getCheckPlayer(player.getName()) != null) {
				main.messenger.sendMsg("Check.AlreadyCheck", player);
				return true;
			}
			
			if (args.length > 0) {
				if (Bukkit.getPlayer(args[0]) == null) main.messenger.sendMsg("Check.PlayerOffline", player, new HashMap<String, Object>(){
					{
						put("$player$", args[0]);
					}
				});
				else {
					player.teleport(Bukkit.getPlayer(args[0]));
					main.messenger.sendMsg("Check.Teleport", player, new HashMap<String, Object>(){
						{
							put("$player$", args[0]);
						}
					});
				}
			}
			
			main.instance.registerCheck(new CHECK(player));
			main.messenger.sendMsg("Check.Check", player);
			
		}  else if (sender instanceof Player) {
			main.messenger.sendMsg("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/check");
				}
			});
		}
		
		return true;
	}

}
