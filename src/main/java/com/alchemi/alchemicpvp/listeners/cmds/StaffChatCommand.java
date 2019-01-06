package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.StaffChat;

public class StaffChatCommand implements CommandExecutor {

	StaffChat staffChat = main.instance.staffChat;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0 && main.instance.hasPermission(sender, "alchemicpvp.staffchat")) {
			if (!staffChat.isListening(sender)) staffChat.addListener(sender);
			else staffChat.removeListener(sender);
		} else if (args.length > 0) {
			String toSend = "";
			for (String arg : args) {
				toSend += arg + " ";
			}
			staffChat.send(sender, toSend);
		}
		
		return true;
	}

}
