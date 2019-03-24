package com.alchemi.alchemicpvp.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.alchemi.alchemicpvp.main;

public class SpyListener implements Listener{

	private final Player spy;
	
	private ArrayList<Player> ignored = new ArrayList<Player>();
	
	public SpyListener(Player spy) {
		this.spy = spy;
		Bukkit.getServer().getPluginManager().registerEvents(this, main.instance);
		main.instance.spies.put(spy.getName(), this);
	}
	
	public void ignorePlayer(Player player) {
		ignored.add(player);
		main.messenger.sendMsg("Spy.IgnoreStart", spy, new HashMap<String, Object>(){
			{
				put("$player$", player.getDisplayName());
			}
		});
	}
	
	public boolean unIgnorePlayer(Player player) {
		main.messenger.sendMsg("Spy.IgnoreStop", spy, new HashMap<String, Object>(){
			{
				put("$player$", player.getDisplayName());
			}
		});
		return ignored.remove(player);
	}
	
	public boolean isIgnoring(Player player) {
		return ignored.contains(player);
	}
	
	public void stopSpying() {
		HandlerList.unregisterAll(this);
		main.instance.spies.remove(spy.getName());
	}
	
	@EventHandler
	public void onMessage(EventMessage e) {
		if (e.getSender() instanceof Player && !ignored.contains((Player) e.getSender()) && !main.instance.hasPermission(e.getSender(), "alchemicpvp.spy.hideFromBigBrother")&&
				e.getSender() != spy && e.getRecipient() != spy) {
			main.messenger.sendMsg("Spy.Message", spy, new HashMap<String, Object>(){
				{
					put("$sender$", ((Player)e.getSender()).getDisplayName());
					if (e.getRecipient() instanceof Player) put("$recipient$", ((Player)e.getRecipient()).getDisplayName());
					else put("$recipient$", "Console");
					put("$message$", e.getMessage());
				}
			});
		}
	}
	
}
