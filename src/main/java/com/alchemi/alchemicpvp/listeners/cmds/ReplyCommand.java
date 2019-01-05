package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.EventMessage;

public class ReplyCommand implements CommandExecutor {

	private Messenger msgnr = main.instance.messenger;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			CommandSender recipient;
			
			if (sender instanceof Player) {
				recipient = main.instance.getPlayer(sender.getName()).getReplyTo();
			} else {
				recipient = MessageCommand.getConsoleReply();	
			}
			
			if (recipient instanceof Player && !((Player) recipient).isOnline()) {
				msgnr.sendMessage("Message.PlayerOffline", sender, new HashMap<String, Object>(){
					{
						put("$player$", recipient.getName());
					}
				});
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

}