package me.alchemi.alchemicpvp.listeners.cmds;

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

import me.alchemi.al.Library;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.meta.StatsMeta;
import me.alchemi.alchemicpvp.stats.YMLStats;

public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			Player pl = (Player) sender;
			
			YMLStats stats;
			boolean other = false;
			if (args.length > 1 && args[1].equals("clear") && PvP.getInstance().hasPermission(pl, "alchemicpvp.stats.clear")) {

				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File dataFile = new File(PvP.getInstance().playerData, player.getUniqueId().toString() + ".yml");
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

				PvP.getInstance().getMessenger().sendMessage(Messages.STATS_CLEARED.value().replace("$player$", args[0]), sender);
				return true;
				
			} else if (args.length > 0 && PvP.getInstance().hasPermission(pl, "alchemicpvp.stats.other")) {
				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File statsFile = new File(PvP.getInstance().playerData, player.getUniqueId().toString() + ".yml");
				if (!statsFile.exists()) {
					PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				if (!player.isOnline()) {
					stats = new YMLStats(YamlConfiguration.loadConfiguration(statsFile), statsFile);
					other = true;
				}
				else {
					stats = StatsMeta.getStats(Bukkit.getPlayer(args[0]));
					other = true;
				}
				
				
				
			} else if (args.length > 0){
				PvP.getInstance().getMessenger().sendMessage(Messages.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
				return true;
			} else {
				stats = StatsMeta.getStats(pl);
			}
			
			
			String msg = "";
			
			if (!other) msg = Messages.STATS_HEADER.value();
			else {
				msg = Messages.STATS_HEADEROTHER.value().replace("$player$", stats.getName());
			}
			
			msg = msg + "\n" + Messages.STATS_KILLS.value().replace("$amount$", String.valueOf(stats.getKills()));
			msg = msg + "\n" + Messages.STATS_DEATHS.value().replace("$amount$", String.valueOf(stats.getDeaths()));
			if (stats.getDeaths() > 0) msg = msg + "\n" + Messages.STATS_KDR.value().replace("$amount$", String.format("%.2f", stats.getKDR()));
			else msg = msg + "\n" + Messages.STATS_KDR.value().replace("$amount$", "N/A");
			msg = msg + "\n" + Messages.STATS_CKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getCurrentKillstreak()));
			msg = msg + "\n" + Messages.STATS_BKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getBestKillstreak()));
			
			if (!other) msg = msg + "\n" + Messages.STATS_FOOTER.value(); 
			else msg = msg + "\n" + Messages.STATS_FOOTEROTHER.value().replace("$player$", stats.getName());
			
			msg = Messenger.formatString(msg);
			
			sender.sendMessage(msg);
			
			return true;
			
		}
		
		if (args.length > 1) {
			if (args[1].equals("clear")) {

				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				File dataFile = new File(PvP.getInstance().playerData, player.getUniqueId().toString() + ".yml");
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

				PvP.getInstance().getMessenger().sendMessage(Messages.STATS_CLEARED.value().replace("$player$", args[0]), sender);
				
			}
		} else if (args.length > 0) {
			OfflinePlayer player = Library.getOfflinePlayer(args[0]);
			
			if (player == null) {
				PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
				return true;
			}
			File statsFile = new File(PvP.getInstance().playerData, player.getUniqueId() + ".yml");
			
			if (!statsFile.exists()) {
				PvP.getInstance().getMessenger().sendMessage(Messages.STATS_NOPLAYER.value().replace("$player$", args[0]), sender);
				return true;
			}
			YMLStats stats;	
			if (!player.isOnline()) {
				stats = new YMLStats(YamlConfiguration.loadConfiguration(statsFile), statsFile);
				
			}
			else {
				stats = StatsMeta.getStats(Bukkit.getPlayer(args[0]));
				
			}
			
			String msg = "";
			
			msg = Messages.STATS_HEADEROTHER.value().replace("$player$", stats.getName());
			
			msg = msg + "\n" + Messages.STATS_KILLS.value().replace("$amount$", String.valueOf(stats.getKills()));
			msg = msg + "\n" + Messages.STATS_DEATHS.value().replace("$amount$", String.valueOf(stats.getDeaths()));
			if (stats.getDeaths() > 0) msg = msg + "\n" + Messages.STATS_KDR.value().replace("$amount$", String.format("%.2f", stats.getKDR()));
			else msg = msg + "\n" + Messages.STATS_KDR.value().replace("$amount$", "N/A");
			msg = msg + "\n" + Messages.STATS_CKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getCurrentKillstreak()));
			msg = msg + "\n" + Messages.STATS_BKILLSTREAK.value().replace("$amount$", String.valueOf(stats.getBestKillstreak()));
			
			msg = msg + "\n" + Messages.STATS_FOOTEROTHER.value().replace("$player$", stats.getName());
			
			msg = Messenger.formatString(msg);
			
			sender.sendMessage(msg);
			
		}
		
		return true;
	}

}
