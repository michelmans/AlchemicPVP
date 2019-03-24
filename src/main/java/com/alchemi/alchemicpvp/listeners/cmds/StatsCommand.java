package com.alchemi.alchemicpvp.listeners.cmds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.alchemi.al.Library;
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
					main.messenger.sendMsg("Stats.NoPlayer", sender);
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

				main.messenger.sendMsg("Stats.Cleared", sender, new HashMap<String, Object>(){
					{
						put("$player$", args[0]);
					}
				});
				return true;
				
			} else if (args.length > 0 && main.instance.hasPermission(pl, "alchemicpvp.stats.other")) {
				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					main.messenger.sendMsg("Stats.NoPlayer", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[0]);
						}
					});
					return true;
				}
				
				File statsFile = new File(main.instance.playerData, player.getUniqueId().toString() + ".yml");
				if (!statsFile.exists()) {
					main.messenger.sendMsg("Stats.NoPlayer", sender, new HashMap<String, Object>(){
						{
							put("$player$", args[0]);
						}
					});
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
				main.messenger.sendMsg("NoPermission", pl, new HashMap<String, Object>(){
					{
						put("$command$", "/stats " + args[0]);
					}
				});
				return true;
			} else {
				stats = StatsMeta.getStats(pl);
			}
			
			
			if (!other) main.messenger.sendMsg("Stats.Header", pl);
			else {
				main.messenger.sendMsg("Stats.HeaderOther", pl, new HashMap<String, Object>(){
					{
						put("$player$", stats.getName());
					}
				});
			}
			
			main.messenger.sendMsg("Stats.Kills", pl, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getKills());
				}
			});
			main.messenger.sendMsg("Stats.Deaths", pl, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getDeaths());
				}
			});
			main.messenger.sendMsg("Stats.KDR", pl, new HashMap<String, Object>(){
				{
					if (stats.getDeaths() > 0) put("$amount$", stats.getKDR());
					else put("$amount$", "N/A");
				}
			});
			main.messenger.sendMsg("Stats.cKillstreak", pl, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getCurrentKillstreak());
				}
			});
			main.messenger.sendMsg("Stats.bKillstreak", pl, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getBestKillstreak());
				}
			});
			
			if (!other) main.messenger.sendMsg("Stats.Footer", pl);
			else {
				main.messenger.sendMsg("Stats.FooterOther", pl, new HashMap<String, Object>(){
					{
						put("$player$", stats.getName());
					}
				});
			}
			return true;
			
		}
		
		if (args.length > 1) {
			if (args[1].equals("clear")) {

				OfflinePlayer player = Library.getOfflinePlayer(args[0]);
				
				if (player == null) {
					main.messenger.sendMsg("Stats.NoPlayer", sender);
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

				main.messenger.sendMsg("Stats.Cleared", sender, new HashMap<String, Object>(){
					{
						put("$player$", args[0]);
					}
				});
				
			}
		} else if (args.length > 0) {
			OfflinePlayer player = Library.getOfflinePlayer(args[0]);
			
			if (player == null) {
				main.messenger.sendMsg("Stats.NoPlayer", sender);
				return true;
			}
			File statsFile = new File(main.instance.playerData, player.getUniqueId() + ".yml");
			System.out.println(statsFile);
			
			if (!statsFile.exists()) {
				main.messenger.sendMsg("Stats.NoPlayer", sender, new HashMap<String, Object>(){
					{
						put("$player$", args[0]);
					}
				});
				return true;
			}
			PlayerStats stats;	
			if (!player.isOnline()) {
				stats = new PlayerStats(YamlConfiguration.loadConfiguration(statsFile), statsFile);
				
			}
			else {
				stats = StatsMeta.getStats(Bukkit.getPlayer(args[0]));
				
			}
			
			main.messenger.sendMsg("Stats.HeaderOther", sender, new HashMap<String, Object>(){
				{
					put("$player$", stats.getName());
				}
			});
			
			main.messenger.sendMsg("Stats.Kills", sender, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getKills());
				}
			});
			main.messenger.sendMsg("Stats.Deaths", sender, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getDeaths());
				}
			});
			main.messenger.sendMsg("Stats.KDR", sender, new HashMap<String, Object>(){
				{
					if (stats.getDeaths() > 0) put("$amount$", stats.getKDR());
					else put("$amount$", "N/A");
				}
			});
			main.messenger.sendMsg("Stats.cKillstreak", sender, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getCurrentKillstreak());
				}
			});
			main.messenger.sendMsg("Stats.bKillstreak", sender, new HashMap<String, Object>(){
				{
					put("$amount$", stats.getBestKillstreak());
				}
			});
			main.messenger.sendMsg("Stats.FooterOther", sender, new HashMap<String, Object>(){
				{
					put("$player$", stats.getName());
				}
			});
			
		}
		
		return true;
	}

}
