package com.alchemi.alchemicpvp.meta;

import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;

public class VanishMeta extends MetadataValueAdapter {

	private boolean vanish;
	
	public static final String NAME = "vanish";
	
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
