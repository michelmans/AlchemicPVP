package com.alchemi.alchemicpvp;

import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;

public class VanishMeta extends MetadataValueAdapter {

	private boolean vanish;
	
	public VanishMeta(Plugin owningPlugin, boolean vanish) {
		super(owningPlugin);
		this.vanish = vanish;
	}

	@Override
	public Object value() {

		return vanish;
	}
	
	@Override
	public void invalidate() {}
}
