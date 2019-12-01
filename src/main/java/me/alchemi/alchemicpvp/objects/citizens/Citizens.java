package me.alchemi.alchemicpvp.objects.citizens;

import org.bukkit.entity.Entity;

import me.alchemi.alchemicpvp.objects.NPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Citizens implements NPC {

	@Override
	public boolean isNPC(Entity e) {
		
		for (NPCRegistry reg : CitizensAPI.getNPCRegistries()) {
			if (reg.isNPC(e)) return true;
		}
		return false;
		
	}

}
