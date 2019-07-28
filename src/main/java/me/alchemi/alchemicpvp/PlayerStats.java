package me.alchemi.alchemicpvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.alchemi.alchemicpvp.objects.events.StatsChangeEvent;

public class PlayerStats {

	public static enum Stat {
		KILLS, DEATHS, KDR, CKS, BKS;
	}
	
	private FileConfiguration data;
	private File dataFile;
	private String name;
	
	public PlayerStats(FileConfiguration data, File dataFile) {
		this.data = data;
		name = data.getString("name");
		this.dataFile = dataFile; 
	}
	
	public void reload() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	public PlayerStats copy() {
		return new PlayerStats(data, dataFile);
	}
	
	public String getName() {
		return name;
	}
	
	public int getKills() {
		return data.getInt("kills");
	}
	
	public int getDeaths() {
		return data.getInt("deaths");
	}
	
	public int getBestKillstreak() {
		return data.getInt("killstreak.best");
	}
	
	public int getCurrentKillstreak() {
		return data.getInt("killstreak.current");
	}
	
	public double getKDR() {
		return (double) getKills()/getDeaths();
	}
	
	public void setKills(int newKills) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KILLS, getKills(), newKills, Bukkit.getPlayer(name)));
		data.set("kills", newKills);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), Bukkit.getPlayer(name)));
	}
	
	public void setDeaths(int newDeaths) {
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.DEATHS, getDeaths(), newDeaths, Bukkit.getPlayer(name)));
		data.set("deaths", newDeaths);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), Bukkit.getPlayer(name)));
		
	}
	
	public void setBestKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.BKS, getBestKillstreak(), newKillstreak, Bukkit.getPlayer(name)));
		data.set("killstreak.best", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	public void setCurrentKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.CKS, getCurrentKillstreak(), newKillstreak, Bukkit.getPlayer(name)));
		data.set("killstreak.current", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	public void updateKills(int update) {
		setKills(getKills() + update);
	}
	public void updateDeaths(int update) {
		setDeaths(getDeaths() + update);
	}
	public void updateKillstreaks(int update) {
		setCurrentKillstreak(getCurrentKillstreak() + update);
		
		if (getBestKillstreak() < getCurrentKillstreak()) {
			setBestKillstreak(getCurrentKillstreak());
		}
		
	}

	public String getNickname() {
		return data.getString("nickname");
	}

	public void setNickname(String nickname) {
		data.set("nickname", nickname);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
	}
	
}
