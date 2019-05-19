package com.alchemi.alchemicpvp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.configurations.SexyConfiguration;
import com.alchemi.al.objects.base.ConfigBase;
import com.alchemi.al.objects.handling.SexyLocation;

public class Config extends ConfigBase{

	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(main.getInstance());
		
		immediateConsuming = ConfigEnum.CONFIG.getConfig().getBoolean("immediateConsuming", true);
		
		if (!ConfigEnum.CONFIG.getConfig().isSet("SPAWN")) SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation();
		else {
			try { SPAWN = new SexyLocation(ConfigEnum.CONFIG.getConfig().getConfigurationSection("SPAWN")).getLocation(); }
			catch (Exception e) { SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation(); }
		}
		
		deathMessages = ConfigEnum.MESSAGES.getConfig().getStringList("AlchemicPVP.DeathMessages");
	}

	public static enum ConfigEnum implements IConfigEnum {
		
		CONFIG(new File(main.getInstance().getDataFolder(), "config.yml"), 5),
		MESSAGES(new File(main.getInstance().getDataFolder(), "messages.yml"), 6);

		final File file;
		final int version;
		SexyConfiguration config;
		
		private ConfigEnum(File file, int version) {
			this.file = file;
			this.version = version;
			this.config = SexyConfiguration.loadConfiguration(file);
		}
		
		@Override
		public SexyConfiguration getConfig() {
			return config;
		}

		@Override
		public File getFile() {
			return file;
		}

		@Override
		public int getVersion() {
			return version;
		}
		
		
		
	}

	public static enum WANDS implements IConfig {
		MAGIC_ENABLED("wand.enabled"),
		MAGIC_NAME("wand.name"),
		MAGIC_MATERIAL("wand.material"),
		MAGIC_ENCHANTED("wand.enchanted"),
		MAGIC_COOLDOWN("wand.cooldown"),
		DRAGON_ENABLED("dragonstick.enabled"),
		DRAGON_NAME("dragonstick.name"),
		DRAGON_MATERIAL("dragonstick.material"),
		DRAGON_ENCHANTED("dragonstick.enchanted"),
		DRAGON_COOLDOWN("dragonstick.cooldown");

		Object value;
		String path;
		
		private WANDS(String path) {
			this.path = path;
		}
		
		@Override
		public Object value() {
			return this.value;
		}

		@Override
		public void get() {

			this.value = getConfig().get(this.path);
			
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
			return null;
		}

		@Override
		public List<String> asStringList() {
			return null;
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			return null;
		}

		@Override
		public Material asMaterial() {
			return Material.getMaterial(asString());
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public String key() {
			return path;
		}
		
	}
	
	public static enum STATS implements IConfig {
		
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
			value = getConfig().get(key);
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

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}
	}
	
	public static enum NICKNAME implements IConfig{
		
		ALLOW_FORMAT("Nickname.allowFormat"),
		CHARACTERLIMIT("Nickname.characterlimit");
		
		public final String key;
		private Object value;
		NICKNAME(String key){
			this.key = key;
			
		}

		@Override
		public void get() {
			value = getConfig().get(key);
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

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}
		
	}
	
	public static enum MESSAGE implements IConfig{
		
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
			value = getConfig().get(key);
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

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}
		
	}

	public static enum MESSAGES implements IMessage {
		
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
		SUDO_PLAYEROFFLINE("AlchemicPVP.Sudo.PlayerOffline"),
		SUDO_SUDO("AlchemicPVP.Sudo.sudo"),
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
			value = getConfig().getString(key, "PLACEHOLDER - STRING NOT FOUND");
			
		}
		
		public String value() {
			return value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.MESSAGES.getConfig();
		}
	}
	
	public static Location SPAWN;
	
	public static boolean immediateConsuming;
	
	public static List<String> deathMessages;
	
	@Override
	public void reload() {
		super.reload();
		deathMessages = ConfigEnum.MESSAGES.getConfig().getStringList("AlchemicPVP.DeathMessages");
		immediateConsuming = ConfigEnum.CONFIG.getConfig().getBoolean("immediateConsuming", true);
		
		try { SPAWN = new SexyLocation(ConfigEnum.CONFIG.getConfig().getConfigurationSection("SPAWN")).getLocation(); }
		catch (Exception e) { SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation(); }
	}
	
	@Override
	public void save() {
		
		ConfigEnum.CONFIG.getConfig().createSection("SPAWN", new SexyLocation(SPAWN).getSection().getValues(true));
		ConfigEnum.CONFIG.getConfig().set("immediateConsuming", immediateConsuming);
		ConfigEnum.MESSAGES.getConfig().set("AlchemicPVP.DeathMessages", deathMessages);
		try {
			ConfigEnum.CONFIG.getConfig().save();
			ConfigEnum.MESSAGES.getConfig().save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.save();
	}

	@Override
	protected IConfigEnum[] getConfigs() {
		return ConfigEnum.values();
	}

	@Override
	protected Set<IConfig> getEnums() {
		return new HashSet<IConfig>() {
			{
				addAll(Arrays.asList(WANDS.values()));
				addAll(Arrays.asList(STATS.values()));
				addAll(Arrays.asList(NICKNAME.values()));
				addAll(Arrays.asList(MESSAGE.values()));
			}
		};
	}

	@Override
	protected Set<IMessage> getMessages() {
		return new HashSet<IMessage>() {
			{
				addAll(Arrays.asList(MESSAGES.values()));
			}
		};
	}
	
}
