package me.alchemi.alchemicpvp.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.PvP;

public class Tracker implements Listener{

	private final Player player;
	private BukkitRunnable tracker;
	private Player tracking;
	
	public Tracker(Player player) {
		this.player = player;
	}
	
	public void startTracking() {
		if (Config.trackingDistance < 0) track();
		else track(Config.trackingDistance);
	}
	
	private void track(double radius) {
		new BukkitRunnable() {
			@Override
			public void run() {
				double close = -1;
				Player closest = null;
				for (Entity e : player.getNearbyEntities(radius, radius, radius)) {
					if (e instanceof Player
							&& !PvP.getInstance().npc.isNPC(e)
							&& !e.equals(player)) {
						
						double dist = e.getLocation().distance(player.getLocation());
						if (dist < close
								|| close == -1) {
							close = dist;
							closest = (Player)e;
						}
					}
				}
				tracking = closest;
				setTarget();
			}
		}.runTaskAsynchronously(PvP.getInstance());
	}
	
	private void track() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				double close = -1;
				Player closest = null;
				for (Player p : player.getWorld().getEntitiesByClass(Player.class)) {
					if (!PvP.getInstance().npc.isNPC(p) && !p.equals(player)) {
						
						double dist = p.getLocation().distance(player.getLocation());
						if (dist < close
								|| close == -1) {
							close = dist;
							closest = p;
						}
					}
				}
				tracking = closest;
				setTarget();				
			}
		}.runTaskAsynchronously(PvP.getInstance());
	}
	
	private void setTarget() {
		if (tracker != null) tracker.cancel();
		tracker = new BukkitRunnable() {
			@Override
			public void run() {
				if (tracking != null 
						&& tracking.isOnline()) {
					Messenger.printStatic("Set target to", tracking.getName());
					player.setCompassTarget(tracking.getLocation());
				} else {
					tracking = null;
				}
			}
		};
		tracker.runTaskTimerAsynchronously(PvP.getInstance(), 0, 20);
	}
	
}
