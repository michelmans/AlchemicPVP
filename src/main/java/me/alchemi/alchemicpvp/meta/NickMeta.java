package me.alchemi.alchemicpvp.meta;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.alchemicpvp.main;

public class NickMeta extends BaseMeta {

	private final String nickname;
	
	public static final String NAME = "nickname";
	
	public NickMeta(String nick) {
		super(main.getInstance(), nick);
		nickname = nick;
		}

	@Override
	public Object value() {

		return nickname;
	}

	@Override
	public void invalidate() {}

}
