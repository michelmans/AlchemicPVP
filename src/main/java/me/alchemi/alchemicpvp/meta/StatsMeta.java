package me.alchemi.alchemicpvp.meta;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.PlayerStats;
import me.alchemi.alchemicpvp.main;

public class StatsMeta extends BaseMeta {

	private PlayerStats stats;
	
	private CommandSender replyTo;
	
	public static final String NAME = "stats";
	
	public StatsMeta(Player player) {
		super(main.getInstance(), null);
		
		File dataFile = new File(main.getInstance().playerData, player.getUniqueId().toString() + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		if (!dataFile.exists()) {
			fc.set("name", player.getName());
			fc.set("nickname", player.getDisplayName());
			fc.set("kills", 0);
			fc.set("deaths", 0);
			fc.createSection("killstreak");
			fc.set("killstreak.best", 0);
			fc.set("killstreak.current", 0);
			try {
				fc.save(dataFile);
			} catch (IOException e1) {}
		} else {
			fc = YamlConfiguration.loadConfiguration(dataFile);
		}
		
		stats = new PlayerStats(fc, dataFile);
		
	}
	
	@Override
	public void invalidate() {}

	@Override
	public Object value() {
		return stats;
	}
	
	public PlayerStats stats() {
		return stats;
	}
	
	public static StatsMeta getMeta(Player player) {
		return (StatsMeta) PersistentMeta.getMeta(player, StatsMeta.class);
	}
	
	public static StatsMeta getMeta(CommandSender sender) {
		return sender instanceof Player ? getMeta((Player) sender) : null;
	}
	
	public static PlayerStats getStats(Player player) {
		return getMeta(player).stats();
	}

	public CommandSender getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(CommandSender replyTo) {
		this.replyTo = replyTo;
	}

}
