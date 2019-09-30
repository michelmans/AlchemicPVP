package me.alchemi.alchemicpvp.objects.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.alchemi.alchemicpvp.stats.Stat;

public class StatsChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private final Stat type;
	private final double oldValue;
	private final double newValue;
	private final Player player;
	
	public StatsChangeEvent(Stat type, double oldValue, double newValue, Player player) {
		this.type = type;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the type
	 */
	public final Stat getType() {
		return type;
	}

	/**
	 * @return the oldValue
	 */
	public final double getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public final double getNewValue() {
		return newValue;
	}

	/**
	 * @return the player
	 */
	public final Player getPlayer() {
		return player;
	}

	
	
}
