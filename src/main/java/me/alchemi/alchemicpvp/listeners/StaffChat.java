package me.alchemi.alchemicpvp.listeners;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;

public class StaffChat implements Listener{

	private ArrayList<CommandSender> listeners = new ArrayList<CommandSender>();
	
	public void addListener(CommandSender sender) {
		listeners.add(sender);
		main.getInstance().getMessenger().sendMessage(Messages.STAFFCHAT_START.value(), sender);
	}
	
	public void removeListener(CommandSender sender) {
		listeners.remove(sender);
		main.getInstance().getMessenger().sendMessage(Messages.STAFFCHAT_STOP.value(), sender);
	}
	
	public boolean isListening(CommandSender sender) {
		return listeners.contains(sender);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().contains(Config.MESSAGE.MENTION_TAG.asString()) && Config.MESSAGE.MENTION_TAG.asString() != "") {
			Matcher m = Pattern.compile(Config.MESSAGE.MENTION_TAG.asString() + "\\w*").matcher(e.getMessage());
			
			while(m.find()) {
				Player player = Bukkit.getPlayer(m.group().replace(Config.MESSAGE.MENTION_TAG.asString(), ""));
				
				if (player != null) {
					if (e.getMessage().contains("&")) {
						
						Matcher m1 = Pattern.compile("&[1234567890abcdefklnor]").matcher(e.getMessage());
						if (m1.find() && m1.start() < m.end()) {
							
							e.setMessage(e.getMessage().replace(m.group(), Messenger.formatString(Config.MESSAGE.MENTION_COLOUR.asString() + "@" + ChatColor.stripColor(player.getDisplayName()) + "&r" + m1.group())));
						}
					} else e.setMessage(e.getMessage().replace(m.group(), Messenger.formatString(Config.MESSAGE.MENTION_COLOUR.asString() + "@" + ChatColor.stripColor(player.getDisplayName()) + "&r")));
					player.playSound(player.getLocation(), Config.MESSAGE.MENTION_SOUND.asSound(), 1.0F, 1.0F);
					
				}
			}
		}
		
		if (listeners.contains(e.getPlayer())) {
			e.setCancelled(true);
			send(e.getPlayer(), e.getMessage());
		}
	}
	
	public void send(CommandSender sender, String message) {
		String toSend;
		if (main.getInstance().hasPermission(sender, "alchemicpvp.staffchat")) {
			toSend = Messages.STAFFCHAT_STAFF.value();
			if (sender instanceof Player) toSend = toSend.replace("$player$", ((Player) sender).getDisplayName());
			else toSend = toSend.replace("$player$", "Console");
			toSend = toSend + message;
		} else {
			toSend = Messages.STAFFCHAT_NONSTAFF.value().replace("$player$", ((Player) sender).getDisplayName()) + message;
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (main.getInstance().hasPermission(player, "alchemicpvp.staffchat")) {
				main.getInstance().getMessenger().sendMessage(toSend, player);
			}
		}
	}
}
