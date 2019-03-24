package com.alchemi.alchemicpvp.meta;

import com.alchemi.al.objects.meta.BaseMeta;
import com.alchemi.alchemicpvp.main;

public class NickMeta extends BaseMeta {

	private final String nickname;
	
	public static final String NAME = "nickname";
	
	public NickMeta(String nick) {
		super(main.instance, nick);
		nickname = nick;
		}

	@Override
	public Object value() {

		return nickname;
	}

	@Override
	public void invalidate() {}

	@Override
	public String name() {
		return NAME;
	}

}
