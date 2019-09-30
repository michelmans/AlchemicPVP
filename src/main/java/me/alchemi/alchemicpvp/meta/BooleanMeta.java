package me.alchemi.alchemicpvp.meta;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.alchemicpvp.PvP;

public class BooleanMeta extends BaseMeta {

	public BooleanMeta(boolean bool) {
		super(PvP.getInstance(), bool);
	}
	
}
