package me.alchemi.alchemicpvp.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.PlayerStats;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.meta.StatsMeta;
import me.alchemi.alchemicpvp.objects.events.StatsChangeEvent;

public class StatsScoreboard implements Listener {
	
	private final ScoreboardManager manager;
	
	private static final String kills = "kills";
	private static final String deaths = "deaths";
	private static final String kdr = "kdr";
	private static final String cks = "cks";
	private static final String bks = "bks";
	
	public StatsScoreboard() {
		manager = Bukkit.getScoreboardManager();
		Bukkit.getPluginManager().registerEvents(this, main.getInstance());
	}
	
	public void addPlayer(Player player) {
		
		Scoreboard scoreboard = manager.getNewScoreboard();
		Objective obj = scoreboard.registerNewObjective("Stats", "none", Messenger.formatString(Messages.STATS_SCOREBOARD_TITLE.value()));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		PlayerStats stats = StatsMeta.getStats(player);
		
		Team[] teams = new Team[Config.Scoreboard.LINESAMOUNT.asInt()];
		
		Team killsTeam = scoreboard.registerNewTeam(kills);
		killsTeam.addEntry(Messenger.formatString("&%&r".replace("%", Config.Scoreboard.KILLS.asString())));
		killsTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_KILLS.value() + stats.getKills()));
		teams[Config.Scoreboard.KILLS.asInt()] = killsTeam;
		
		Team deathsTeam = scoreboard.registerNewTeam(deaths);
		deathsTeam.addEntry(Messenger.formatString("&%&r".replace("%", Config.Scoreboard.DEATHS.asString())));
		deathsTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_DEATHS.value() + stats.getDeaths()));
		teams[Config.Scoreboard.DEATHS.asInt()] = deathsTeam;
		
		Team kdrTeam = scoreboard.registerNewTeam(kdr);
		kdrTeam.addEntry(Messenger.formatString("&%&r".replace("%", Config.Scoreboard.KDR.asString())));
		kdrTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_KDR.value() + String.format("%.2f", stats.getKDR())));
		teams[Config.Scoreboard.KDR.asInt()] = kdrTeam;
		
		Team cksTeam = scoreboard.registerNewTeam(cks);
		cksTeam.addEntry(Messenger.formatString("&%&r".replace("%", Config.Scoreboard.CKILLSTREAK.asString())));
		cksTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_CKILLSTREAK.value() + stats.getCurrentKillstreak()));
		teams[Config.Scoreboard.CKILLSTREAK.asInt()] = cksTeam;
		
		Team bksTeam = scoreboard.registerNewTeam(bks);
		bksTeam.addEntry(Messenger.formatString("&%&r".replace("%", Config.Scoreboard.BKILLSTREAK.asString())));
		bksTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_BKILLSTREAK.value() + stats.getBestKillstreak()));
		teams[Config.Scoreboard.BKILLSTREAK.asInt()] = bksTeam;
		
		Team footerTeam = scoreboard.registerNewTeam("footer");
		footerTeam.addEntry(Messenger.formatString("&0&r"));
		footerTeam.setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_FOOTER.value()));
		teams[0] = footerTeam;
		
		
		for (int i = 0; i < Config.Scoreboard.LINESAMOUNT.asInt(); i++) {
			if (teams[i] == null) {
				Team t = scoreboard.registerNewTeam("emtpy" + i);
				t.addEntry(Messenger.formatString("&%&r".replace("%", String.valueOf(i))));
				t.setPrefix("");
			}
			obj.getScore(Messenger.formatString("&%&r".replace("%", String.valueOf(i)))).setScore(i);
		}
		
		player.setScoreboard(scoreboard);
	}
	
	@EventHandler
	public void onStatChange(StatsChangeEvent e) {
		
		Scoreboard scoreboard = e.getPlayer().getScoreboard();
		
		switch(e.getType()) {
		case BKS:
			scoreboard.getTeam(bks).setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_BKILLSTREAK.value() + Math.round(e.getNewValue())));
			break;
		case CKS:
			scoreboard.getTeam(cks).setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_CKILLSTREAK.value() + Math.round(e.getNewValue())));
			break;
		case DEATHS:
			scoreboard.getTeam(deaths).setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_DEATHS.value() + Math.round(e.getNewValue())));
			break;
		case KDR:
			scoreboard.getTeam(kdr).setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_KDR.value() + String.format("%.2f", e.getNewValue())));
			break;
		case KILLS:
			scoreboard.getTeam(kills).setPrefix(Messenger.formatString(Messages.STATS_SCOREBOARD_KILLS.value() + Math.round(e.getNewValue())));
			break;
		}
		
	}
}
