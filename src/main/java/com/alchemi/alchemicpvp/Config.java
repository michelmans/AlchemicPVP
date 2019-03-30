package com.alchemi.alchemicpvp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.configurations.SexyConfiguration;
import com.alchemi.al.objects.SexyLocation;

public class Config {

	public static SexyConfiguration config;
	public static SexyConfiguration messages;

	private interface ConfigInterface {
		
		Object value();
		
		void get();
		
		boolean asBoolean();
		
		String asString();
		
		Sound asSound();
		
		List<String> asStringList();
		
		int asInt();
		
		ItemStack asItemStack();
		
		Material asMaterial();
	}
	
	public static enum STATS implements ConfigInterface {
		
		POTION_EFFECT("Stats.potionEffect"),
		DEATHMESSAGES("Stats.deathMessages");
		
		private Object value;
		public final String key;
		
		STATS(String key){
			this.key = key;
			get();
		}
		
		
		
		@Override
		public void get() {
			value = config.get(key);
		}

		@Override
		public Object value() {
			return value;
		}

		@Override
		public boolean asBoolean() {
			return Boolean.parseBoolean(asString());
		}

		@Override
		public String asString() {
			return String.valueOf(value);
		}

		@Override
		public Sound asSound() {
			
			return Sound.valueOf(asString());
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> asStringList() {
			try {
				return (List<String>) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			try {
				return (ItemStack) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public Material asMaterial() {
			return Material.valueOf(asString());
		}
	}
	
	public static enum NICKNAME implements ConfigInterface{
		
		ALLOW_FORMAT("Nickname.allowFormat"),
		CHARACTERLIMIT("Nickname.characterlimit");
		
		public final String key;
		private Object value;
		NICKNAME(String key){
			this.key = key;
			
		}

		@Override
		public void get() {
			value = config.get(key);
		}

		@Override
		public Object value() {
			return value;
		}

		@Override
		public boolean asBoolean() {
			return Boolean.parseBoolean(asString());
		}

		@Override
		public String asString() {
			return String.valueOf(value);
		}

		@Override
		public Sound asSound() {
			
			return Sound.valueOf(asString());
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> asStringList() {
			try {
				return (List<String>) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			try {
				return (ItemStack) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public Material asMaterial() {
			return Material.valueOf(asString());
		}
		
	}
	
	public static enum MESSAGE implements ConfigInterface{
		
		MENTION_TAG("Message.mentionTag"),
		MENTION_COLOUR("Message.mentionColour"),
		RECEIVE_SOUND("Message.receiveSound"),
		MENTION_SOUND("Message.mentionSound");
		
		
		public final String key;
		private Object value;
		MESSAGE(String key){
			this.key = key;
			
		}

		@Override
		public void get() {
			value = config.get(key);
		}

		@Override
		public Object value() {
			return value;
		}

		@Override
		public boolean asBoolean() {
			return Boolean.parseBoolean(asString());
		}

		@Override
		public String asString() {
			return String.valueOf(value);
		}

		@Override
		public Sound asSound() {
			try {
				return Sound.valueOf(asString());
			} catch(IllegalArgumentException | NullPointerException ex) {
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> asStringList() {
			try {
				return (List<String>) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			try {
				return (ItemStack) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public Material asMaterial() {
			return Material.valueOf(asString());
		}
		
	}

	public static enum MESSAGES{
		
		NO_PERMISSION("AlchemicPVP.Commands.NoPermission"),
		COMMANDS_WRONG_FORMAT("AlchemicPVP.Commands.WrongFormat"),
		COMMANDS_UNKNOWN("AlchemicPVP.Commands.Unknown"),
		STATS_NOPLAYER("AlchemicPVP.Stats.NoPlayer"),
		STATS_CLEARED("AlchemicPVP.Stats.Cleared"),
		STATS_HEADEROTHER("AlchemicPVP.Stats.HeaderOther"),
		STATS_FOOTEROTHER("AlchemicPVP.Stats.FooterOther"),
		STATS_HEADER("AlchemicPVP.Stats.Header"),
		STATS_KILLS("AlchemicPVP.Stats.Kills"),
		STATS_DEATHS("AlchemicPVP.Stats.Deaths"),
		STATS_KDR("AlchemicPVP.Stats.KDR"),
		STATS_CKILLSTREAK("AlchemicPVP.Stats.cKillstreak"),
		STATS_BKILLSTREAK("AlchemicPVP.Stats.bKillstreak"),
		STATS_FOOTER("AlchemicPVP.Stats.Footer"),
		CHECK_PLAYEROFFLINE("AlchemicPVP.Check.PlayerOffline"),
		CHECK_TELEPORT("AlchemicPVP.Check.Teleport"),
		CHECK_CHECK("AlchemicPVP.Check.Check"),
		CHECK_UNCHECK("AlchemicPVP.Check.Uncheck"),
		CHECK_NOTCHECK("AlchemicPVP.Check.NotCheck"),
		CHECK_ALREADYCHECK("AlchemicPVP.Check.AlreadyCheck"),
		CHECK_NOHURTING("AlchemicPVP.Check.NoHurting"),
		CHECK_VANISHTAG("AlchemicPVP.Check.VanishTag"),
		CHECK_SPAWN("AlchemicPVP.Check.Spawn"),
		VANISH("AlchemicPVP.Vanish"),
		UNVANISH("AlchemicPVP.Unvanish"),
		MESSAGE_PLAYEROFFLINE("AlchemicPVP.PlayerOffline"),
		MESSAGE_TEMPLATESEND("AlchemicPVP.Message.TemplateSend"),
		MESSAGE_TEMPLATERECEIVE("AlchemicPVP.Message.TemplateReceive"),
		SMITE_PLAYEROFFLINE("AlchemicPVP.Smite.PlayerOffline"),
		SMITE_PROVIDE("AlchemicPVP.Smite.Provide"),
		SMITE_SMITTEN("AlchemicPVP.Smite.Smitten"),
		SMITE_SMITESENT("AlchemicPVP.Smite.SmiteSent"),
		SPY_PLAYEROFFLINE("AlchemicPVP.Spy.PlayerOffline"),
		SPY_MESSAGE("AlchemicPVP.Spy.Message"),
		SPY_START("AlchemicPVP.Spy.Start"),
		SPY_STOP("AlchemicPVP.Spy.Stop"),
		SPY_IGNORESTART("AlchemicPVP.Spy.IgnoreStart"),
		SPY_IGNORESTOP("AlchemicPVP.Spy.IgnoreStop"),
		NICK_NOONE("AlchemicPVP.Nick.NoOne"),
		NICK_IS("AlchemicPVP.Nick.Is"),
		NICK_TAKEN1("AlchemicPVP.Nick.Taken1"),
		NICK_TAKEN2("AlchemicPVP.Nick.Taken2"),
		NICK_NEW("AlchemicPVP.Nick.New"),
		NICK_TOOLONG("AlchemicPVP.Nick.TooLong"),
		NICK_NOFORMAT("AlchemicPVP.Nick.NoFormat"),
		STAFFCHAT_NONSTAFF("AlchemicPVP.StaffChat.NonStaff"),
		STAFFCHAT_STAFF("AlchemicPVP.StaffChat.Staff"),
		STAFFCHAT_START("AlchemicPVP.StaffChat.Start"),
		STAFFCHAT_STOP("AlchemicPVP.StaffChat.Stop");
		
		String value;
		String key;
		
		private MESSAGES(String key) {
			this.key = key;
		}
		
		public void get() { 
			value = messages.getString(key, "PLACEHOLDER - STRING NOT FOUND");
			
		}
		
		public String value() {
			return value;
		}
	}
	
	public static Location SPAWN;
	
	public static List<String> deathMessages;
	
	public static void enable() throws IOException, InvalidConfigurationException {
		
		config = SexyConfiguration.loadConfiguration(main.CONFIG_FILE);
		messages = SexyConfiguration.loadConfiguration(main.MESSAGES_FILE);
		
		if (!config.isSet("SPAWN")) SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation();
		else {
			try { SPAWN = new SexyLocation(config.getConfigurationSection("SPAWN")).getLocation(); }
			catch (Exception e) { SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation(); }
		}
		for (SexyConfiguration file : new SexyConfiguration[] {messages, config}) {
			int version;
			if (file.equals(config)) version = main.CONFIG_FILE_VERSION; 
			else if (file.equals(messages)) version = main.MESSAGES_FILE_VERSION; 
			else version = 0;
				  
			if(!file.getFile().exists()) main.instance.saveResource(file.getFile().getName(), false);
			  
			if(!file.isSet("File-Version-Do-Not-Edit") ||
					!file.get("File-Version-Do-Not-Edit").equals(version)) {
				
				main.messenger.print("Your $file$ is outdated! Updating...".replace("$file$", file.getFile().getName())); 
				file.load(new InputStreamReader(main.instance.getResource(file.getFile().getName())));
				file.update(SexyConfiguration.loadConfiguration(new InputStreamReader(main.instance.getResource(file.getFile().getName()))));
				file.set("File-Version-Do-Not-Edit", version);
				file.save();
				main.messenger.print("File successfully updated!");
			} 
		}
					 
		for (STATS value : STATS.values()) value.get();
		
		for (NICKNAME value : NICKNAME.values()) value.get();
		
		for (MESSAGE value : MESSAGE.values()) value.get();
		
		for (MESSAGES value : MESSAGES.values()) value.get();
		
		deathMessages = messages.getStringList("AlchemicPVP.DeathMessages");
	}
	
	public static void reload() {
		config = SexyConfiguration.loadConfiguration(main.CONFIG_FILE);
		messages = SexyConfiguration.loadConfiguration(main.MESSAGES_FILE);
		
		for (STATS value : STATS.values()) {
			value.get();
		}
		
		for (NICKNAME value : NICKNAME.values()) {
			value.get();
		}
		
		for (MESSAGE value : MESSAGE.values()) {
			value.get();
		}
		
		deathMessages = messages.getStringList("AlchemicPVP.DeathMessages");
	}
	
	public static void save() {
		for (STATS value : STATS.values()) {
			config.set(value.key, value.value);
		}
		
		for (NICKNAME value : NICKNAME.values()) {
			config.set(value.key, value.value);
		}
		
		for (MESSAGE value : MESSAGE.values()) {
			config.set(value.key, value.value);
		}
		config.set("SPAWN", new SexyLocation(SPAWN).getSection());
		main.fileManager.saveConfig("config.yml");
	}
	
}
