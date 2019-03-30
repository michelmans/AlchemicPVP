package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.StatsMeta;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.reload")) {
			
			for (String file : main.fileManager.getFiles().keySet()) {
				main.fileManager.reloadConfig(file);
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				StatsMeta.getStats(player).reload();
			}
			Config.reload();
			main.messenger.sendMessage("&9Configs reloaded.", sender);
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
