package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.EventMessage;
import com.alchemi.alchemicpvp.meta.StatsMeta;

public class ReplyCommand implements CommandExecutor {

	private Messenger msgnr = main.messenger;
	
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
				msgnr.sendMsg("Message.PlayerOffline", sender, new HashMap<String, Object>(){
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
				if (recipient instanceof Player && Config.MESSAGE.RECEIVE_SOUND.asSound() != null) ((Player)recipient).playSound(((Player) recipient).getLocation(), Config.MESSAGE.RECEIVE_SOUND.asSound(), 1.0F, 1.0F);
				msgnr.sendMsg("Message.TemplateSend", sender, new HashMap<String, Object>(){
					{
						put("$player$", nameR);
						put("$message$", e.getMessage());
					}
				});
				msgnr.sendMsg("Message.TemplateReceive", recipient, new HashMap<String, Object>(){
					{
						put("$player$", nameS);
						put("$message$", e.getMessage());
					}
				});
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
