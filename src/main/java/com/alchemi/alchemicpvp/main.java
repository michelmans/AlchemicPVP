package com.alchemi.alchemicpvp;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.alchemi.al.FileManager;
import com.alchemi.al.Library;
import com.alchemi.al.Messenger;
import com.alchemi.al.sexyconfs.SexyConfiguration;
import com.alchemi.alchemicpvp.listeners.CHECK;
import com.alchemi.alchemicpvp.listeners.Events;
import com.alchemi.alchemicpvp.listeners.SpyListener;
import com.alchemi.alchemicpvp.listeners.cmds.CheckCommand;
import com.alchemi.alchemicpvp.listeners.cmds.MessageCommand;
import com.alchemi.alchemicpvp.listeners.cmds.ReloadCommand;
import com.alchemi.alchemicpvp.listeners.cmds.ReplyCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SmiteCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SpawnCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SpyCommand;
import com.alchemi.alchemicpvp.listeners.cmds.StatsCommand;
import com.alchemi.alchemicpvp.listeners.cmds.UncheckCommand;
import com.alchemi.alchemicpvp.listeners.cmds.VanishCommand;
import com.alchemi.alchemicpvp.listeners.tabcomplete.SpyTabComplete;
import com.alchemi.alchemicpvp.listeners.tabcomplete.StatsTabComplete;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class main extends JavaPlugin {

	public static main instance;
	
	private Permission perms;
	public Chat chat;
	private boolean VaultPerms = false;
	public static boolean AnimatedNames = false;
	
	public Messenger messenger;
	public FileManager fileManager;
	public SexyConfiguration config;
	
	private final int MESSAGES_FILE_VERSION = 4;
	private final int CONFIG_FILE_VERSION = 1;
	
	public File playerData;
	
	private HashMap<String, PlayerStats> playerStats = new HashMap<String, PlayerStats>();
	private HashMap<String, CHECK> checkedPlayers = new HashMap<String, CHECK>();
	
	public HashMap<String, SpyListener> spies = new HashMap<String, SpyListener>();
	
	@Override
	public void onEnable() {
		instance = this;
		
		fileManager = new FileManager(this, new String[] {"messages.yml", "config.yml"}, null, null);
		
		fileManager.saveDefaultYML("messages.yml");
		fileManager.saveDefaultYML("config.yml");
		
		messenger = new Messenger(fileManager);
		
		if(!fileManager.getConfig("messages.yml").isSet("File-Version-Do-Not-Edit") || !fileManager.getConfig("messages.yml").get("File-Version-Do-Not-Edit").equals(MESSAGES_FILE_VERSION)) {
			messenger.print("Your messages file is outdated! Updating...");
			fileManager.reloadConfig("messages.yml");
			fileManager.updateConfig("messages.yml");
			fileManager.getConfig("messages.yml").set("File-Version-Do-Not-Edit", MESSAGES_FILE_VERSION);
			fileManager.saveConfig("messages.yml");
			messenger.print("File successfully updated!");
		}
		if(!getConfig().isSet("File-Version-Do-Not-Edit") || !getConfig().get("File-Version-Do-Not-Edit").equals(CONFIG_FILE_VERSION)) {
			messenger.print("Your config file is outdated! Updating...");
			fileManager.reloadConfig("config.yml");
			fileManager.updateConfig("config.yml");
			config = fileManager.getConfig("config.yml");
			config.setComment("Stats.potionEffect", "# Whether a vanished player should get the invisiblity potion effect or not.\n# Note: this does not affect visibility for players without the alchemicpvp.check.bypass permission node.\n# Default: false");
			config.setComment("Stats.deathMessages", "# Should death messages be displayed\n# Default: true");
			config.set("SPAWN", getServer().getWorlds().get(0).getSpawnLocation());
			config.set("File-Version-Do-Not-Edit", CONFIG_FILE_VERSION);
			fileManager.save(config);
			messenger.print("File successfully updated!");
		}
		
		config = fileManager.getConfig("config.yml");
		
		VaultPerms = setupPermission();
		AnimatedNames = getServer().getPluginManager().getPlugin("AnimatedName") != null;
		if (VaultPerms) messenger.print("Vault installed! Using Vault permissions.");
		if (setupChat()) {
			
			messenger.print("Vault installed! Using vault chat.");
			
		}
		registerCommands();
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		
		playerData = new File(getDataFolder(), "playerdata");
		
		if (!playerData.exists()) playerData.mkdir();
		
		messenger.print("&1The die is &8cast...");
	}
	
	private void registerCommands() {
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("check").setExecutor(new CheckCommand());
		getCommand("uncheck").setExecutor(new UncheckCommand());
		getCommand("setcheckspawn").setExecutor(new SpawnCommand());
		getCommand("statsreload").setExecutor(new ReloadCommand());
		getCommand("reply").setExecutor(new ReplyCommand());
		getCommand("message").setExecutor(new MessageCommand());
		getCommand("smite").setExecutor(new SmiteCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("socialspy").setExecutor(new SpyCommand());
		
		getCommand("socialspy").setTabCompleter(new SpyTabComplete());
		getCommand("stats").setTabCompleter(new StatsTabComplete());
		
	}

	public boolean hasPermission(Player player, String perm) {
		
		return VaultPerms ? perms.has(player, perm) || player.isOp() : player.hasPermission(perm) || player.isOp();
	}
	
	public boolean hasPermission(CommandSender sender, String perm) {
		return sender instanceof Player ? sender.isOp() || hasPermission((Player) sender, perm) : true;
	}
	
	private boolean setupPermission() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) return false;
		
		perms = rsp.getProvider();
		return perms != null;
	}
	
	private boolean setupChat() {
		if(getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if (rsp == null) return false;
		
		chat = rsp.getProvider();
		
		return chat != null;
	}
	
	public PlayerStats getPlayer(String name) {
		if (!playerStats.containsKey(name)) return null;
		return playerStats.get(name);
	}
	
	public Collection<PlayerStats> getAllStats(){
		return playerStats.values();
	}
	
	public void registerPlayer(PlayerStats stats) {
		playerStats.put(stats.getName(), stats);
	}
	
	public void unregisterPlayer(String name) {
		if (playerStats.containsKey(name)) playerStats.remove(name);
	}
	
	public CHECK getCheckPlayer(String name) {
		if (!checkedPlayers.containsKey(name)) return null;
		return checkedPlayers.get(name);
	}
	
	public void registerCheck(CHECK toReg) {
		checkedPlayers.put(toReg.getPlayer(), toReg);
	}
	
	public void unRegisterCheck(String name) {
		if (checkedPlayers.containsKey(name)) checkedPlayers.remove(name);
	}
	
}
