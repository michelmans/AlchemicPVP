package com.alchemi.alchemicpvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.alchemi.alchemicpvp.main;

public class ItemListeners implements Listener{

	private final Player player;
	
	public ItemListeners(Player player) {
		this.player = player;
		Bukkit.getServer().getPluginManager().registerEvents(this, main.instance);
	}
	
	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		
		if (e.getPlayer().equals(player)) {
			if (e.getMaterial().equals(Material.STICK) && e.getItem().hasItemMeta() && e.getItem().getItemMeta().getDisplayName().contains("Magic wand")) {
				Block target = e.getPlayer().getTargetBlockExact(10);
				if (target != null 
						&& (e.getAction().equals(Action.LEFT_CLICK_AIR)
								|| e.getAction().equals(Action.LEFT_CLICK_BLOCK))) target.getWorld().strikeLightning(target.getLocation());
				
			}
		}
	}
	
}
