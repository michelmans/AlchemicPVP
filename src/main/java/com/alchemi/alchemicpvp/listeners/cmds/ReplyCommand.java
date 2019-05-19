package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.EventMessage;
import com.alchemi.alchemicpvp.meta.StatsMeta;

public class ReplyCommand implements CommandExecutor {

	private Messenger msgnr = main.getInstance().getMessenger();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			CommandSender recipient;
			
			if (sender instanceof Player) {
				recipient = StatsMeta.getMeta(sender).getReplyTo();
			} else {
				recipient = MessageCommand.getConsoleReply();	
			}
			
			if (recipient instanceof Player && !((Player) recipient).isOnline()) {
				msgnr.sendMessage(MESSAGES.MESSAGE_PLAYEROFFLINE.value().replace("$player$", recipient.getName()), sender);
				return true;
			}
			
			String msg = "";
			for (String arg : args) {
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
				msgnr.sendMessage(MESSAGES.MESSAGE_TEMPLATESEND.value()
						.replace("$player$", nameR)
						.replace("$message$", e.getMessage()), sender);
				
				msgnr.sendMessage(MESSAGES.MESSAGE_TEMPLATERECEIVE.value()
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

}
