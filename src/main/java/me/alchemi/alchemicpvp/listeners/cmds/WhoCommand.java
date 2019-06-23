package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;

public class WhoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (main.getInstance().hasPermission(sender, "alchemicpvp.whois") && args.length > 0) {
			Player answer = whoIs(args[0]);
			if (answer == null) {
				main.getInstance().getMessenger().sendMessage(Messages.NICK_NOONE.value().replace("$nick$", args[0]), sender);
				return true;
			}
			main.getInstance().getMessenger().sendMessage(Messages.NICK_IS.value()
					.replace("$name$", answer.getDisplayName())
					.replace("$player$", answer.getName()), sender);
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
