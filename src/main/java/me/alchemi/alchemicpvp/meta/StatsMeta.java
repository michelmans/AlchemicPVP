package me.alchemi.alchemicpvp.meta;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.stats.IStats;

public class StatsMeta extends BaseMeta {

	private IStats stats;
	
	private CommandSender replyTo;
	
	public StatsMeta(Player player) {
		super(PvP.getInstance(), null);
		
		stats = StatsGetter.get(player);
	}
	
	@Override
	public void invalidate() {}

	@Override
	public Object value() {
		return stats;
	}
	
	public IStats stats() {
		return stats;
	}
	
	public static StatsMeta getMeta(Player player) {
		return (StatsMeta) PersistentMeta.getMeta(player, StatsMeta.class);
	}
	
	public static StatsMeta getMeta(CommandSender sender) {
		return sender instanceof Player ? getMeta((Player) sender) : null;
	}
	
	public static IStats getStats(Player player) {
		return getMeta(player).stats();
	}

	public CommandSender getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(CommandSender replyTo) {
		this.replyTo = replyTo;
	}

}
