package me.alchemi.alchemicpvp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.ConfigBase;
import me.alchemi.al.objects.handling.SexyLocation;

public class Config extends ConfigBase{

	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(PvP.getInstance());
		
		immediateConsuming = ConfigEnum.CONFIG.getConfig().getBoolean("immediateConsuming", true);
		clearInventory = ConfigEnum.CONFIG.getConfig().getBoolean("clearInventory", true);
		
		if (!ConfigEnum.CONFIG.getConfig().isSet("SPAWN")) SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation();
		else {
			try { SPAWN = new SexyLocation(ConfigEnum.CONFIG.getConfig().getConfigurationSection("SPAWN")).getLocation(); }
			catch (Exception e) { SPAWN = Bukkit.getWorlds().get(0).getSpawnLocation(); }
		}
		
		deathMessages = ConfigEnum.MESSAGES.getConfig().getStringList("AlchemicPVP.DeathMessages");
	}

	public static enum ConfigEnum implements IConfigEnum {
		
		CONFIG(new File(PvP.getInstance().getDataFolder(), "config.yml"), 11),
		MESSAGES(new File(PvP.getInstance().getDataFolder(), "messages.yml"), 10);

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

	public static enum Wands implements IConfig {
		MAGIC_ENABLED("wand.enabled"),
		MAGIC_NAME("wand.name"),
		MAGIC_MATERIAL("wand.material"),
		MAGIC_ENCHANTED("wand.enchanted"),
		MAGIC_COOLDOWN("wand.cooldown"),
		MAGIC_SECONDARYCOOLDOWN("wand.secondaryCooldown"),
		MAGIC_SECONDARYDAMAGE("wand.secondaryDamage"),
		MAGIC_LORE("wand.lore"),
		DRAGON_ENABLED("dragonstick.enabled"),
		DRAGON_NAME("dragonstick.name"),
		DRAGON_MATERIAL("dragonstick.material"),
		DRAGON_ENCHANTED("dragonstick.enchanted"),
		DRAGON_COOLDOWN("dragonstick.cooldown"),
		DRAGON_SECONDARYCOOLDOWN("dragonstick.secondaryCooldown"),
		DRAGON_SECONDARYDAMAGE("dragonstick.secondaryDamage"),
		DRAGON_LORE("dragonstick.lore");

		Object value;
		String path;
		
		private Wands(String path) {
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
			
			return getConfig().getStringList(path).stream()
					.map(STRING -> STRING = Messenger.formatString(STRING))
					.collect(Collectors.toList());
			
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}
		
		public double asDouble() {
			return Double.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			return null;
		}

		@Override
		public Material asMaterial() {
			return MaterialWrapper.getWrapper(asString());
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public String key() {
			return path;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static enum Stats implements IConfig {
		
		POTION_EFFECT("Stats.potionEffect"),
		DEATHMESSAGES("Stats.deathMessages"),
		SCOREBOARD("Stats.scoreboard");
		
		private Object value;
		public final String key;
		
		Stats(String key){
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
			return MaterialWrapper.getWrapper(asString());
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public double asDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static enum Worldguard implements IConfig {
		DISABLE_WAND_PVP("WorldGuard.disableWandPvP"),
		VISIBLE_BORDER_LEEWAY("WorldGuard.visibleBorderLeeway"),
		VISIBLE_BORDER_BLOCK("WorldGuard.visibleBorderBlock");

		Object value;
		String key;
		
		private Worldguard(String key) {
			this.key = key;
			get();
		}
		
		@Override
		public Object value() {
			return value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public void get() {
			
			value = getConfig().get(key);
			
		}

		@Override
		public boolean asBoolean() {
			return Boolean.valueOf(asString());
		}

		@Override
		public String asString() {
			return String.valueOf(value);
		}

		@Override
		public Sound asSound() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> asStringList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Material asMaterial() {
			return MaterialWrapper.getWrapper(asString());
		}

		@Override
		public double asDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static enum Nickname implements IConfig{
		
		ALLOW_FORMAT("Nickname.allowFormat"),
		CHARACTERLIMIT("Nickname.characterlimit");
		
		public final String key;
		private Object value;
		Nickname(String key){
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
			return MaterialWrapper.getWrapper(asString());
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public double asDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static enum Message implements IConfig{
		
		MENTION_TAG("Message.mentionTag"),
		MENTION_COLOUR("Message.mentionColour"),
		RECEIVE_SOUND("Message.receiveSound"),
		MENTION_SOUND("Message.mentionSound");
		
		
		public final String key;
		private Object value;
		Message(String key){
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
			return MaterialWrapper.getWrapper(asString());
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public double asDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static enum Scoreboard implements IConfig {
		LINESAMOUNT("Scoreboard.linesAmount"),
		KILLS("Scoreboard.kills"),
		DEATHS("Scoreboard.deaths"),
		KDR("Scoreboard.kdr"),
		CKILLSTREAK("Scoreboard.ckillstreak"),
		BKILLSTREAK("Scoreboard.bkillstreak");

		public final String key;
		private Object value;
		
		private Scoreboard(String key) {
			this.key = key;
			get();
		}
		
		@Override
		public Object value() {
			return value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public void get() {
			this.value = getConfig().get(key);
			
		}

		@Override
		public boolean asBoolean() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String asString() {
			return String.valueOf(value);
		}

		@Override
		public Sound asSound() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> asStringList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}

		@Override
		public ItemStack asItemStack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Material asMaterial() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double asDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Float> asFloatList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Integer> asIntList() {
			return getConfig().getIntegerList(key);
		}
		
	}

	public static enum Messages implements IMessage {
		COMMANDS_NOPERMISSION("AlchemicPVP.Commands.NoPermission"),
		COMMANDS_RELOAD("AlchemicPVP.Commands.Reload"),
		COMMANDS_WRONGFORMAT("AlchemicPVP.Commands.WrongFormat"),
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
		STATS_SCOREBOARD_TITLE("AlchemicPVP.Stats.Scoreboard.Title"),
		STATS_SCOREBOARD_KILLS("AlchemicPVP.Stats.Scoreboard.Kills"),
		STATS_SCOREBOARD_DEATHS("AlchemicPVP.Stats.Scoreboard.Deaths"),
		STATS_SCOREBOARD_KDR("AlchemicPVP.Stats.Scoreboard.KDR"),
		STATS_SCOREBOARD_CKILLSTREAK("AlchemicPVP.Stats.Scoreboard.cKillstreak"),
		STATS_SCOREBOARD_BKILLSTREAK("AlchemicPVP.Stats.Scoreboard.bKillstreak"),
		STATS_SCOREBOARD_FOOTER("AlchemicPVP.Stats.Scoreboard.Footer"),
		CHECK_PLAYEROFFLINE("AlchemicPVP.Check.PlayerOffline"),
		CHECK_TELEPORT("AlchemicPVP.Check.Teleport"),
		CHECK_ENABLE("AlchemicPVP.Check.Enable"),
		CHECK_DISABLE("AlchemicPVP.Check.Disable"),
		CHECK_NOTCHECK("AlchemicPVP.Check.NotCheck"),
		CHECK_ALREADYCHECK("AlchemicPVP.Check.AlreadyCheck"),
		CHECK_NOHURTING("AlchemicPVP.Check.NoHurting"),
		CHECK_VANISHTAG("AlchemicPVP.Check.VanishTag"),
		CHECK_SPAWN("AlchemicPVP.Check.Spawn"),
		VANISH("AlchemicPVP.Vanish"),
		UNVANISH("AlchemicPVP.Unvanish"),
		TP_PLAYEROFFLINE("AlchemicPVP.TP.PlayerOffline"),
		TP_NOLOCATION("AlchemicPVP.TP.NoLocation"),
		TP_TELEPORT("AlchemicPVP.TP.Teleport"),
		GAMEMODE_PLAYEROFFLINE("AlchemicPVP.Gamemode.PlayerOffline"),
		GAMEMODE_SETOTHER("AlchemicPVP.Gamemode.SetOther"),
		GAMEMODE_SETOWN("AlchemicPVP.Gamemode.SetOwn"),
		INVSEE_PLAYEROFFLINE("AlchemicPVP.Invsee.PlayerOffline"),
		FLY_ENABLE("AlchemicPVP.Fly.Enable"),
		FLY_DISABLE("AlchemicPVP.Fly.Disable"),
		MESSAGE_PLAYEROFFLINE("AlchemicPVP.Message.PlayerOffline"),
		MESSAGE_TEMPLATESEND("AlchemicPVP.Message.TemplateSend"),
		MESSAGE_TEMPLATERECEIVE("AlchemicPVP.Message.TemplateReceive"),
		SMITE_PLAYEROFFLINE("AlchemicPVP.Smite.PlayerOFfline"),
		SMITE_PROVIDE("AlchemicPVP.Smite.Provide"),
		SMITE_SMITTEN("AlchemicPVP.Smite.Smitten"),
		SMITE_SENT("AlchemicPVP.Smite.Sent"),
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
		STAFFCHAT_STOP("AlchemicPVP.StaffChat.Stop"),
		SUDO_PLAYEROFFLINE("AlchemicPVP.Sudo.PlayerOffline"),
		SUDO_RUN("AlchemicPVP.Sudo.Run"),
		DEATHMESSAGES("AlchemicPVP.DeathMessages");
		
		String value;
		String key;
		
		private Messages(String key) {
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
	
	public static boolean clearInventory;
	
	public static List<String> deathMessages;
	
	@Override
	public void reload() {
		super.reload();
		deathMessages = ConfigEnum.MESSAGES.getConfig().getStringList("AlchemicPVP.DeathMessages");
		clearInventory = ConfigEnum.CONFIG.getConfig().getBoolean("clearInventory", true);
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
				addAll(Arrays.asList(Wands.values()));
				addAll(Arrays.asList(Stats.values()));
				addAll(Arrays.asList(Nickname.values()));
				addAll(Arrays.asList(Message.values()));
			}
		};
	}

	@Override
	protected Set<IMessage> getMessages() {
		return new HashSet<IMessage>() {
			{
				addAll(Arrays.asList(Messages.values()));
			}
		};
	}
	
}
