package com.alchemi.alchemicpvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerStats {

	private FileConfiguration data;
	private File dataFile;
	private String name;
	
	private CommandSender replyTo;
	
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
		
		data.set("kills", newKills);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	public void setDeaths(int newDeaths) {
		
		data.set("deaths", newDeaths);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	public void setBestKillstreak(int newKillstreak) {
		
		data.set("killstreak.best", newKillstreak);
		try {
			data.save(dataFile);
		} catch (IOException e) {}
		
	}
	
	public void setCurrentKillstreak(int newKillstreak) {
		
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

	public CommandSender getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(CommandSender replyTo) {
		this.replyTo = replyTo;
	}
	
}
