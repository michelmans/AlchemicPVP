package me.alchemi.alchemicpvp.listeners.cmds;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.listeners.EventMessage;
import me.alchemi.alchemicpvp.meta.StatsMeta;

public class MessageCommand implements CommandExecutor{

	private Messenger msgnr = main.getInstance().getMessenger();
	private static CommandSender consoleReply; 
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length > 1) {
			CommandSender recipient = null;
			
			if (args[0].equals("Console")) {
				recipient = Bukkit.getConsoleSender();
			} else {
				recipient = main.getInstance().getServer().getPlayer(args[0]);
				if (recipient == null) {
					msgnr.sendMessage(Messages.MESSAGE_PLAYEROFFLINE.value().replace("$player$", args[0]), sender);
					return true;
				}
			}
			
			String msg = "";
			for (String arg : Arrays.copyOfRange(args, 1, args.length)) {
				msg += " " + arg;
			}
			String message = msg;
			
			String nameR;
			if (recipient instanceof Player) {
				nameR = ((Player)recipient).getDisplayName();
			} else {
				nameR = "Console";
			}

			String nameS;
			if (sender instanceof Player) {
				nameS = ((Player)sender).getDisplayName();
			} else {
				nameS = "Console";
			}
			
			EventMessage e = new EventMessage(message, recipient, sender);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if (!e.isCancelled()) {
				if (recipient instanceof Player && Config.MESSAGE.RECEIVE_SOUND.asSound() != null) ((Player)recipient).playSound(((Player) recipient).getLocation(), Config.MESSAGE.RECEIVE_SOUND.asSound(), 1.0F, 1.0F);
				msgnr.sendMessage(Messages.MESSAGE_TEMPLATESEND.value()
						.replace("$player$", nameR)
						.replace("$message$", e.getMessage()), sender);
				
				msgnr.sendMessage(Messages.MESSAGE_TEMPLATERECEIVE.value()
						.replace("$player$", nameS)
						.replace("$message$", e.getMessage()), recipient);
				
				if (recipient == Bukkit.getConsoleSender()) {
					MessageCommand.setConsoleReply(sender);
					StatsMeta.getMeta(sender).setReplyTo(recipient);
					
				} else if (sender == Bukkit.getConsoleSender()) {
					MessageCommand.setConsoleReply(recipient);
					StatsMeta.getMeta(recipient).setReplyTo(sender);
				} else {
					StatsMeta.getMeta(sender).setReplyTo(recipient);
					StatsMeta.getMeta(recipient).setReplyTo(sender);
				}
			}
		}
		
		return true;
	}

	public static CommandSender getConsoleReply() {
		return consoleReply;
	}

	public static void setConsoleReply(CommandSender consoleReply) {
		MessageCommand.consoleReply = consoleReply;
	}

	
	
}
