package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.EventMessage;

public class MessageCommand implements CommandExecutor{

	private Messenger msgnr = main.instance.messenger;
	private static CommandSender consoleReply; 
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length > 1) {
			CommandSender recipient = null;
			
			if (args[0].equals("Console")) {
				recipient = Bukkit.getConsoleSender();
			} else {
				recipient = main.instance.getServer().getPlayer(args[0]);
				if (recipient == null) {
					msgnr.sendMessage("Message.PlayerOffline", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[0]);
						}
					});
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
				msgnr.sendMessage("Message.TemplateSend", sender, new HashMap<String, Object>(){
					{
						put("$player$", nameR);
						put("$message$", e.getMessage());
					}
				});
				msgnr.sendMessage("Message.TemplateReceive", recipient, new HashMap<String, Object>(){
					{
						put("$player$", nameS);
						put("$message$", e.getMessage());
					}
				});
				if (recipient == Bukkit.getConsoleSender()) {
					MessageCommand.setConsoleReply(sender);
					main.instance.getPlayer(sender.getName()).setReplyTo(recipient);
				} else if (sender == Bukkit.getConsoleSender()) {
					MessageCommand.setConsoleReply(recipient);
					main.instance.getPlayer(recipient.getName()).setReplyTo(sender);
				} else {
					main.instance.getPlayer(sender.getName()).setReplyTo(recipient);
					main.instance.getPlayer(recipient.getName()).setReplyTo(sender);
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
