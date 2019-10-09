package me.alchemi.alchemicpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.alchemicpvp.PvP;

public class PotionBlocking implements Listener {
	
	@EventHandler
	public void onPotionThrow(PlayerInteractEvent e) {
		if ( (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) 
				&& e.getItem() != null
				&& (MaterialWrapper.wrap(e.getItem()) == MaterialWrapper.SPLASH_POTION
					|| MaterialWrapper.wrap(e.getItem()) == MaterialWrapper.LINGERING_POTION
					|| MaterialWrapper.wrap(e.getItem()) == MaterialWrapper.POTION)
				&& !PvP.getInstance().getWorldGuard().canPvP(e.getPlayer())) {
			PvP.getInstance().getWorldGuard().sendPvPDeny(e.getPlayer());
			e.setCancelled(true);
		}
	}

}
