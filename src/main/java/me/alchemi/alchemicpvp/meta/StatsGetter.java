package me.alchemi.alchemicpvp.meta;

import org.bukkit.OfflinePlayer;

import me.alchemi.alchemicpvp.Config.Storage;
import me.alchemi.alchemicpvp.stats.DataType;
import me.alchemi.alchemicpvp.stats.IStats;
import me.alchemi.alchemicpvp.stats.MySQLStats;
import me.alchemi.alchemicpvp.stats.YMLStats;

public class StatsGetter {

	public static IStats get(OfflinePlayer player) {
		switch(DataType.valueOf(Storage.TYPE.asString())) {
		case MYSQL:
			return new MySQLStats(player);
		case SQLITE:
			return new MySQLStats(player);
		case YML:
			return new YMLStats(player);
		}
		return null;
	}
	
}
