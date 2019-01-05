package com.alchemi.alchemicpvp;

import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;

public class SpyMeta extends MetadataValueAdapter {

	private String spying = "false";
	
	public SpyMeta(Plugin owningPlugin, boolean spying) {
		super(owningPlugin);
		this.spying = String.valueOf(spying);
	}

	@Override
	public Object value() {

		return spying;
	}
	
	@Override
	public boolean asBoolean() {
		
		return Boolean.getBoolean(spying);
	}

	@Override
	public void invalidate() {
		spying = null;
	}
}