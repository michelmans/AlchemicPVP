package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;

public class SudoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (main.getInstance().hasPermission(sender, "alchemicpvp.sudo")) {
			
			String msg = "";
			if (args.length < 2) {
				msg = MESSAGES.COMMANDS_WRONG_FORMAT.value() + main.getInstance().getCommand("sudo").getUsage();
				main.getInstance().getMessenger().sendMessage(msg, sender);
				return true;
			} else {
				
				Player user = Bukkit.getPlayer(args[0]);
				if (user == null) {
					msg = MESSAGES.SUDO_PLAYEROFFLINE.value().replace("$player$", args[0]);
					main.getInstance().getMessenger().sendMessage(msg, sender);
				}
				
				String cmd = "";
				for (String arg : args) {
					if (arg.equals(args[0])) continue;
					else if (arg.equals(args[1])) cmd = arg;
					else cmd += " " + arg;
				}
				
				user.performCommand(cmd);
				msg = MESSAGES.SUDO_SUDO.value().replace("$player$", user.getDisplayName()).replace("$command$", cmd);
				main.getInstance().getMessenger().sendMessage(msg, sender);
			}
			
		}
		return true;
	}

}
