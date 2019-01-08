package com.alchemi.alchemicpvp;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.sexyconfs.SexyConfiguration;

public class Config {

	public static SexyConfiguration config;

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

	public static Location SPAWN;
	
	public static void enable() {
		config = main.fileManager.getConfig("config.yml");
		if (!config.isSet("SPAWN")) SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation();
		SPAWN = (Location) config.get("SPAWN");
	}
	
	public static void reload() {
		config = main.fileManager.getConfig("config.yml");
		
		for (STATS value : STATS.values()) {
			value.get();
		}
		
		for (NICKNAME value : NICKNAME.values()) {
			value.get();
		}
		
		for (MESSAGE value : MESSAGE.values()) {
			value.get();
		}
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
		config.set("SPAWN", SPAWN);
		main.fileManager.saveConfig("config.yml");
	}
	
}
