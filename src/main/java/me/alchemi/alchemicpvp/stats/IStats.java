package me.alchemi.alchemicpvp.stats;

import org.bukkit.OfflinePlayer;

public interface IStats {
	
	IStats copy();
	
	String getNickname();
	
	int getKills();
	
	int getDeaths();
	
	int getBestKillstreak();
	
	int getCurrentKillstreak();
	
	double getKDR();
	
	OfflinePlayer getPlayer();
	
	void setKills(int newKills);
	
	void setDeaths(int newDeaths);
	
	void setBestKillstreak(int newKillstreak);
	
	void setCurrentKillstreak(int newKillstreak);
	
	void setNickname(String nickname);
	
	default void updateKills(int update) {
		setKills(getKills() + update);
	}
	
	default void updateDeaths(int update) {
		setDeaths(getDeaths() + update);
	}
	
	default void updateKillstreaks(int update) {
		setCurrentKillstreak(getCurrentKillstreak() + update);
		
		if (getBestKillstreak() < getCurrentKillstreak()) {
			setBestKillstreak(getCurrentKillstreak());
		}
	}

}
