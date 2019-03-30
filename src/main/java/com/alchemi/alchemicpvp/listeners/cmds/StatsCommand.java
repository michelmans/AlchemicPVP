package com.alchemi.alchemicpvp.listeners.cmds;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.alchemi.al.Library;
import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.PlayerStats;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.StatsMeta;

public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			Player pl = (Player) sender;
			
			PlayerStats stats;
			boolean other = false;
			if (args.length > 1 && args[1].equals("clear") && main.instance.hasPermission(pl, "alchemicpvp.stats.clear")) {

				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File dataFile = new File(main.instance.playerData, player.getUniqueId().toString() + ".yml");
				FileConfiguration fc = new YamlConfiguration();
				fc.set("name", player.getName());
				fc.set("kills", 0);
				fc.set("deaths", 0);
				fc.createSection("killstreak");
				fc.set("killstreak.best", 0);
				fc.set("killstreak.current", 0);
				try {
					fc.save(dataFile);
				} catch (IOException e1) {}

				main.messenger.sendMessage(MESSAGES.STATS_CLEARED.value().replace("$player$", args[0]), sender);
				return true;
				
			} else if (args.length > 0 && main.instance.hasPermission(pl, "alchemicpvp.stats.other")) {
				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File statsFile = new File(main.instance.playerData, player.getUniqueId().toString() + ".yml");
				if (!statsFile.exists()) {
					main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				if (!player.isOnline()) {
					stats = new PlayerStats(YamlConfiguration.loadConfiguration(statsFile), statsFile);
					other = true;
				}
				else {
					stats = StatsMeta.getStats(Bukkit.getPlayer(args[0]));
					other = true;
				}
				
				
				
			} else if (args.length > 0){
				main.messenger.sendMessage(MESSAGES.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
				return true;
			} else {
				stats = StatsMeta.getStats(pl);
			}
			
			
			String msg = "";
			
			if (!other) msg = MESSAGES.STATS_HEADER.value();
			else {
				msg = MESSAGES.STATS_HEADEROTHER.value().replace("$player$", stats.getName());
			}
			
			msg = msg + "\n" + MESSAGES.STATS_KILLS.value().replace("$amount$", String.valueOf(stats.getKills()));
			msg = msg + "\n" + MESSAGES.STATS_DEATHS.value().replace("$amount$", String.valueOf(stats.getDeaths()));
			if (stats.getDeaths() > 0) msg = msg + "\n" + MESSAGES.STATS_KDR.value().replace("$amount$", String.format("%.2f", stats.getKDR()));
			else msg = msg + "\n" + MESSAGES.STATS_KDR.value().replace("$amount$", "N/A");
			msg = msg + "\n" + MESSAGES.STATS_CKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getCurrentKillstreak()));
			msg = msg + "\n" + MESSAGES.STATS_BKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getBestKillstreak()));
			
			if (!other) msg = msg + "\n" + MESSAGES.STATS_FOOTER.value(); 
			else msg = msg + "\n" + MESSAGES.STATS_FOOTEROTHER.value().replace("$player$", stats.getName());
			
			msg = Messenger.cc(msg);
			
			sender.sendMessage(msg);
			
			return true;
			
		}
		
		if (args.length > 1) {
			if (args[1].equals("clear")) {

				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File dataFile = new File(main.instance.playerData, player.getUniqueId().toString() + ".yml");
				FileConfiguration fc = new YamlConfiguration();
				fc.set("name", player.getName());
				fc.set("kills", 0);
				fc.set("deaths", 0);
				fc.createSection("killstreak");
				fc.set("killstreak.best", 0);
				fc.set("killstreak.current", 0);
				try {
					fc.save(dataFile);
				} catch (IOException e1) {}

				main.messenger.sendMessage(MESSAGES.STATS_CLEARED.value().replace("$player$", args[0]), sender);
				
			}
		} else if (args.length > 0) {
			OfflinePlayer player = Library.getOfflinePlayer(args[0]);
			
			if (player == null) {
				main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
				return true;
			}
			File statsFile = new File(main.instance.playerData, player.getUniqueId() + ".yml");
			System.out.println(statsFile);
			
			if (!statsFile.exists()) {
				main.messenger.sendMessage(MESSAGES.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
				return true;
			}
			PlayerStats stats;	
			if (!player.isOnline()) {
				stats = new PlayerStats(YamlConfiguration.loadConfiguration(statsFile), statsFile);
				
			}
			else {
				stats = StatsMeta.getStats(Bukkit.getPlayer(args[0]));
				
			}
			
			String msg = "";
			
			msg = MESSAGES.STATS_HEADEROTHER.value().replace("$player$", stats.getName());
			
			msg = msg + "\n" + MESSAGES.STATS_KILLS.value().replace("$amount$", String.valueOf(stats.getKills()));
			msg = msg + "\n" + MESSAGES.STATS_DEATHS.value().replace("$amount$", String.valueOf(stats.getDeaths()));
			if (stats.getDeaths() > 0) msg = msg + "\n" + MESSAGES.STATS_KDR.value().replace("$amount$", String.format("%.2f", stats.getKDR()));
			else msg = msg + "\n" + MESSAGES.STATS_KDR.value().replace("$amount$", "N/A");
			msg = msg + "\n" + MESSAGES.STATS_CKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getCurrentKillstreak()));
			msg = msg + "\n" + MESSAGES.STATS_BKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getBestKillstreak()));
			
			msg = msg + "\n" + MESSAGES.STATS_FOOTEROTHER.value().replace("$player$", stats.getName());
			
			msg = Messenger.cc(msg);
			
			sender.sendMessage(msg);
			
		}
		
		return true;
	}

}
