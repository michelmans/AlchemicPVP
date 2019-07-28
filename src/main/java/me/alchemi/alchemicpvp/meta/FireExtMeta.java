package me.alchemi.alchemicpvp.meta;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.alchemicpvp.main;

public class FireExtMeta extends BaseMeta {
	
	private static List<FireExtMeta> registered = new ArrayList<FireExtMeta>();
	
	private Block block;

	public FireExtMeta(Block block) {
		super(main.getInstance(), block);
		
		this.block = block;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(getOwningPlugin(), new Runnable() {
			
			@Override
			public void run() {
				
				update();
				remove();
				
			}
		}, 100);
		registered.add(this);
	}
	
	public static void removeAll() {
		for (FireExtMeta meta : registered) {
			meta.update();
			meta.remove();
		}
	}
	
	private void update() {
		block.setType(MaterialWrapper.AIR.getMaterial());
	}
	
	private void remove() {
		registered.remove(this);
		block.removeMetadata(this.getClass().getName(), main.getInstance());
	}

}
