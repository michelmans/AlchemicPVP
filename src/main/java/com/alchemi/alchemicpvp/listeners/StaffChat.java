package com.alchemi.alchemicpvp.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alchemi.al.Messenger;
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
