package com.alchemi.alchemicpvp.meta;

import org.bukkit.plugin.Plugin;

import com.alchemi.al.objects.meta.BaseMeta;

public class VanishMeta extends BaseMeta {

	private boolean vanish;
	
	public static final String NAME = "vanish";
	
	public VanishMeta(Plugin owningPlugin, boolean vanish) {
		super(owningPlugin, vanish);
		this.vanish = vanish;
	}

	@Override
	public Object value() {

		return vanish;
	}
	
	@Override
	public void invalidate() {}
	
}
