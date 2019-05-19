package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.StatsMeta;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.reload")) {
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				StatsMeta.getStats(player).reload();
			}
			main.config.reload();
			main.getInstance().getMessenger().sendMessage("&9Configs reloaded.", sender);
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
