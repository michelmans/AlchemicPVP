package com.alchemi.alchemicpvp.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.main;

public class StaffChat implements Listener{

	private ArrayList<CommandSender> listeners = new ArrayList<CommandSender>();
	
	public void addListener(CommandSender sender) {
		listeners.add(sender);
		main.messenger.sendMessage("StaffChat.Start", sender);
	}
	
	public void removeListener(CommandSender sender) {
		listeners.remove(sender);
		main.messenger.sendMessage("StaffChat.Stop", sender);
	}
	
	public boolean isListening(CommandSender sender) {
		return listeners.contains(sender);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().contains(Config.MESSAGE.MENTION_TAG.asString()) && Config.MESSAGE.MENTION_TAG.asString() != "") {
			Matcher m = Pattern.compile("@\\w*").matcher(e.getMessage());
			
			while(m.find()) {
				Player player = Bukkit.getPlayer(m.group().replace("@", ""));
				if (player != null) {
					player.playSound(player.getLocation(), Config.MESSAGE.MENTION_SOUND.asSound(), 1.0F, 1.0F);
					if (e.getMessage().contains("&")) {
						
						Matcher m1 = Pattern.compile("&[1234567890klnor]").matcher(e.getMessage());
						if (m1.find() && m1.start() < m.end()) {
							
							e.setMessage(e.getMessage().replace(m.group(), Messenger.cc(Config.MESSAGE.MENTION_COLOUR.asString() + m.group() + "&r" + m1.group())));
						}
					} else e.setMessage(e.getMessage().replace(m.group(), Messenger.cc(Config.MESSAGE.MENTION_COLOUR.asString() + m.group() + "&r")));
					
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
		if (main.instance.hasPermission(sender, "alchemicpvp.staffchat")) {
			toSend = Messenger.parseVars(main.messenger.getMessage("StaffChat.Staff"), new HashMap<String, Object>(){
				{
					if (sender instanceof Player) put("$player$", ((Player) sender).getDisplayName());
					else put("$player$", "Console");
				}
			}) + message;
		} else {
			toSend = Messenger.parseVars(main.messenger.getMessage("StaffChat.NonStaff"), new HashMap<String, Object>(){
				{
					put("$player$", ((Player) sender).getDisplayName());
				}
			}) + message;
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (main.instance.hasPermission(player, "alchemicpvp.staffchat")) {
				Messenger.sendMsg(toSend, player);
			}
		}
	}
}
