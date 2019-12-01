package me.alchemi.alchemicpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.alchemicpvp.objects.Tracker;

public class ClickCompassEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onClick(PlayerInteractEvent e) {
		if (e.getItem() != null && MaterialWrapper.wrap(e.getItem()) == MaterialWrapper.COMPASS) {
			new Tracker(e.getPlayer()).startTracking();
			e.setCancelled(true);
		}
	}
	
}
