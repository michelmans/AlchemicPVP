package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.listeners.StaffChat;

public class StaffChatCommand implements CommandExecutor {

	StaffChat staffChat = main.getInstance().staffChat;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0 && main.getInstance().hasPermission(sender, "alchemicpvp.staffchat")) {
			if (!staffChat.isListening(sender)) staffChat.addListener(sender);
			else staffChat.removeListener(sender);
		} else if (args.length > 0 && (main.getInstance().hasPermission(sender, "alchemicpvp.staffchat") || label.equals("ho") || label.equals("helpop"))) {
			String toSend = "";
			for (String arg : args) {
				toSend += arg + " ";
			}
			staffChat.send(sender, toSend);
		}
		
		return true;
	}

}