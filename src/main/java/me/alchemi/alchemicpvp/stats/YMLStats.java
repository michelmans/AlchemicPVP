package me.alchemi.alchemicpvp.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.objects.events.StatsChangeEvent;

public class YMLStats implements IStats {

	private FileConfiguration data;
	private File dataFile;
	private OfflinePlayer player;
	
	public YMLStats(OfflinePlayer player) {
		this.player = player;
		this.dataFile = new File(PvP.getInstance().playerData, player.getUniqueId().toString() + ".yml");
		
		if (!dataFile.exists()) {
			data.set("nickname", player.getName());
			data.set("kills", 0);
			data.set("deaths", 0);
			data.createSection("killstreak");
			data.set("killstreak.best", 0);
			data.set("killstreak.current", 0);
			try {
				data.save(dataFile);
			} catch (IOException e1) {}
		} else {
			data = YamlConfiguration.loadConfiguration(dataFile);
		}
	}
	
	public void reload() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	@Override
	public YMLStats copy() {
		return new YMLStats(player);
	}
	
	@Override
	public int getKills() {
		return data.getInt("kills");
	}
	
	@Override
	public int getDeaths() {
		return data.getInt("deaths");
	}
	
	@Override
	public int getBestKillstreak() {
		return data.getInt("killstreak.best");
	}
	
	@Override
	public int getCurrentKillstreak() {
		return data.getInt("killstreak.current");
	}
	
	@Override
	public double getKDR() {
		return (double) getKills()/getDeaths();
	}
	
	@Override
	public void setKills(int newKills) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KILLS, getKills(), newKills, player.getPlayer()));
		data.set("kills", newKills);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), player.getPlayer()));
	}
	
	@Override
	public void setDeaths(int newDeaths) {
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.DEATHS, getDeaths(), newDeaths, player.getPlayer()));
		data.set("deaths", newDeaths);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), player.getPlayer()));
		
	}
	
	@Override
	public void setBestKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.BKS, getBestKillstreak(), newKillstreak, player.getPlayer()));
		data.set("killstreak.best", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	@Override
	public void setCurrentKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.CKS, getCurrentKillstreak(), newKillstreak, player.getPlayer()));
		data.set("killstreak.current", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}

	@Override
	public String getNickname() {
		return data.getString("nickname");
	}

	@Override
	public void setNickname(String nickname) {
		data.set("nickname", nickname);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
	}

	@Override
	public OfflinePlayer getPlayer() {
		return player;
	}
	
}
