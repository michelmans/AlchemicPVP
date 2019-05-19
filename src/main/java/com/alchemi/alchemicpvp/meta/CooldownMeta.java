package com.alchemi.alchemicpvp.meta;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.alchemi.al.objects.meta.BaseMeta;
import com.alchemi.alchemicpvp.main;

public class CooldownMeta extends BaseMeta {

	private int remaining;
	private int remainingTicks;
	private boolean value;
	private BukkitTask task;
	public Player player;
	
	public CooldownMeta(int cooldown, Player player) {
		super(main.getInstance(), false);
		value = false;
		remainingTicks = 20;
		remaining = cooldown;
		this.player = player;
		player.setLevel(cooldown);
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				remainingTicks --;
				
				if (remainingTicks == 0) {
					remaining --;
					
					player.setExp(1.0F);
					player.setLevel(remaining);
					
					remainingTicks = 20;
				}
				
				else player.setExp(remainingTicks/20.0F);
				
				if (remaining == 0) {
					value = true;
					player.setExp(0.0F);
					cancelTask();
				}
				
				
				
			}
		}, 0, 1);
	}
	
	public void cancelTask() {
		task.cancel();
	}
	
	@Override
	public boolean asBoolean() {
		
		return value;
	}
	
	@Override
	public int asInt() {
		
		return remaining;
	}

}
