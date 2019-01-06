package com.alchemi.alchemicpvp.meta;

import org.bukkit.metadata.MetadataValueAdapter;

import com.alchemi.alchemicpvp.main;

public class NickMeta extends MetadataValueAdapter {

	private final String nickname;
	
	public static final String NAME = "nickname";
	
	public NickMeta(String nick) {
		super(main.instance);
		nickname = nick;
		}

	@Override
	public Object value() {

		return nickname;
	}

	@Override
	public void invalidate() {}

}
