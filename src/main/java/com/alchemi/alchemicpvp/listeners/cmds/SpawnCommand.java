package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;

public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		
		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.spawn")) {
			
			Player player = (Player) sender;
			Config.SPAWN = player.getLocation();
			System.out.println(Config.SPAWN.serialize());
			Config.save();
			main.messenger.sendMessage(MESSAGES.CHECK_SPAWN.value()
					.replace("$x$", String.valueOf(player.getLocation().getBlockX()))
					.replace("$y$", String.valueOf(player.getLocation().getBlockY()))
					.replace("$z$", String.valueOf(player.getLocation().getBlockZ())), player);
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
