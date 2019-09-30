package me.alchemi.alchemicpvp.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.alchemi.alchemicpvp.objects.events.StatsChangeEvent;

public class YMLStats implements IStats {

	private FileConfiguration data;
	private File dataFile;
	private String name;
	
	public YMLStats(FileConfiguration data, File dataFile) {
		this.data = data;
		name = data.getString("name");
		this.dataFile = dataFile; 
	}
	
	public void reload() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	@Override
	public YMLStats copy() {
		return new YMLStats(data, dataFile);
	}
	
	@Override
	public String getName() {
		return name;
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
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KILLS, getKills(), newKills, Bukkit.getPlayer(name)));
		data.set("kills", newKills);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), Bukkit.getPlayer(name)));
	}
	
	@Override
	public void setDeaths(int newDeaths) {
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.DEATHS, getDeaths(), newDeaths, Bukkit.getPlayer(name)));
		data.set("deaths", newDeaths);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.KDR, getKDR(), getKDR(), Bukkit.getPlayer(name)));
		
	}
	
	@Override
	public void setBestKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.BKS, getBestKillstreak(), newKillstreak, Bukkit.getPlayer(name)));
		data.set("killstreak.best", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	@Override
	public void setCurrentKillstreak(int newKillstreak) {
		
		Bukkit.getPluginManager().callEvent(new StatsChangeEvent(Stat.CKS, getCurrentKillstreak(), newKillstreak, Bukkit.getPlayer(name)));
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
	
}
