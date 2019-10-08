package me.alchemi.alchemicpvp.stats;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;

import me.alchemi.al.database.Column;
import me.alchemi.al.database.ColumnModifier;
import me.alchemi.al.database.DataType;
import me.alchemi.al.database.Table;
import me.alchemi.al.database.mysql.MySQLDatabase;
import me.alchemi.alchemicpvp.Config.Storage;
import me.alchemi.alchemicpvp.PvP;

public class MySQLStats implements IStats {

	private static MySQLDatabase database;
	
	private static Table table;
	private static Column uuid;
	private static Column nickname;
	private static Column kills;
	private static Column deaths;
	private static Column bks;
	private static Column cks;
	
	private OfflinePlayer player;
	private String nicknameVar;
	private short killsVar;
	private short deathsVar;
	private short bksVar;
	private short cksVar;
	
	public static void init() {
		uuid = new Column("uuid", DataType.TINYTEXT, ColumnModifier.NOT_NULL, ColumnModifier.UNIQUE);
		nickname = new Column("nickname", DataType.TINYTEXT, ColumnModifier.NOT_NULL);
		kills = new Column("kills", DataType.SMALLINT, ColumnModifier.DEFAULT);
		kills.setDefValue(0);
		deaths = new Column("deaths", DataType.SMALLINT, ColumnModifier.DEFAULT);
		deaths.setDefValue(0);
		bks = new Column("bks", DataType.SMALLINT, ColumnModifier.DEFAULT);
		bks.setDefValue(0);
		cks = new Column("cks", DataType.SMALLINT, ColumnModifier.DEFAULT);
		cks.setDefValue(0);
		
		table = new Table("alchemicpvp_playerstats", uuid, nickname, kills, deaths, bks, cks);
		
		try {
			database = MySQLDatabase.newConnection(PvP.getInstance(), Storage.HOST.asString(), Storage.DATABASE.asString(), Storage.USER.asString(), Storage.PASSWORD.asString());
			database.createTable(table);
		} catch (SQLException e) {
			database = null;
			e.printStackTrace();
		}
	}
	
	public MySQLStats(OfflinePlayer player) {
		this.player = player;
		
		database.insertValuesIgnore(table, new HashMap<Column, Object>(){
			{
				put(uuid, player.getUniqueId().toString());
				put(nickname, player.getName());
			}
		});
		
		if (Storage.KEEPINMEMORY.asBoolean()) {
			nicknameVar = getNickname();
			killsVar = (short)getKills();
			deathsVar = (short)getDeaths();
			bksVar = (short)getBestKillstreak();
			cksVar = (short)getCurrentKillstreak();
		} else {
			nicknameVar = null;
			killsVar = 0;
			deathsVar = 0;
			bksVar = 0;
			cksVar = 0;
		}
	}
	
	@Override
	public IStats copy() {
		return new MySQLStats(player);
	}

	@Override
	public String getNickname() {
		try {
			return Storage.KEEPINMEMORY.asBoolean() ? nicknameVar : database.getValue(table, nickname, uuid, player.getUniqueId().toString()).getString(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return player.getName();
		}
	}

	@Override
	public int getKills() {
		try {
			return Storage.KEEPINMEMORY.asBoolean() ? killsVar : database.getValue(table, kills, uuid, player.getUniqueId().toString()).getInt(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getDeaths() {
		try {
			return Storage.KEEPINMEMORY.asBoolean() ? deathsVar : database.getValue(table, deaths, uuid, player.getUniqueId().toString()).getInt(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getBestKillstreak() {
		try {
			return Storage.KEEPINMEMORY.asBoolean() ? bksVar : database.getValue(table, bks, uuid, player.getUniqueId().toString()).getShort(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getCurrentKillstreak() {
		try {
			return Storage.KEEPINMEMORY.asBoolean() ? cksVar : database.getValue(table, cks, uuid, player.getUniqueId().toString()).getShort(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double getKDR() {
		return Math.round(Double.valueOf(getKills())/Double.valueOf(getDeaths()) * 100D)/100D;
	}

	@Override
	public void setKills(int newKills) {
		database.updateValue(table, kills, (short)newKills, getConditional());
		
		if (Storage.KEEPINMEMORY.asBoolean()) killsVar = (short) newKills;
	}

	@Override
	public void setDeaths(int newDeaths) {
		
		database.updateValue(table, deaths, (short)newDeaths, getConditional());
		
		if (Storage.KEEPINMEMORY.asBoolean()) deathsVar = (short) newDeaths;

	}

	@Override
	public void setBestKillstreak(int newKillstreak) {
		
		database.updateValue(table, bks, (short)newKillstreak, getConditional());
		
		if (Storage.KEEPINMEMORY.asBoolean()) bksVar = (short) newKillstreak;

	}

	@Override
	public void setCurrentKillstreak(int newKillstreak) {
		
		database.updateValue(table, cks, (short)newKillstreak, getConditional());
		
		if (Storage.KEEPINMEMORY.asBoolean()) cksVar = (short) newKillstreak;

	}

	@Override
	public void setNickname(String newNickname) {
		
		database.updateValue(table, nickname, newNickname, getConditional());
		
		if (Storage.KEEPINMEMORY.asBoolean()) nicknameVar = newNickname;

	}

	private Map<Column, Object> getConditional(){
		return new HashMap<Column, Object>(){
			{
				put(uuid, player.getUniqueId().toString());
			}
		};
	}

	@Override
	public OfflinePlayer getPlayer() {
		return player;
	}
	
}
