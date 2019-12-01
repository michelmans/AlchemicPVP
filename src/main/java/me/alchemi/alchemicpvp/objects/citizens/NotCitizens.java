package me.alchemi.alchemicpvp.objects.citizens;

import org.bukkit.entity.Entity;

import me.alchemi.alchemicpvp.objects.NPC;

public class NotCitizens implements NPC {

	@Override
	public boolean isNPC(Entity e) {
		return false;
	}

}
