package com.alchemi.alchemicpvp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.alchemi.al.configurations.Messenger;
import com.alchemi.al.deprecated.FileManager;
import com.alchemi.alchemicpvp.listeners.CHECK;
import com.alchemi.alchemicpvp.listeners.Events;
import com.alchemi.alchemicpvp.listeners.SpyListener;
import com.alchemi.alchemicpvp.listeners.StaffChat;
import com.alchemi.alchemicpvp.listeners.cmds.CheckCommand;
import com.alchemi.alchemicpvp.listeners.cmds.MessageCommand;
import com.alchemi.alchemicpvp.listeners.cmds.NickCommand;
import com.alchemi.alchemicpvp.listeners.cmds.ReloadCommand;
import com.alchemi.alchemicpvp.listeners.cmds.ReplyCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SmiteCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SpawnCommand;
import com.alchemi.alchemicpvp.listeners.cmds.SpyCommand;
import com.alchemi.alchemicpvp.listeners.cmds.StaffChatCommand;
import com.alchemi.alchemicpvp.listeners.cmds.StatsCommand;
import com.alchemi.alchemicpvp.listeners.cmds.UncheckCommand;
import com.alchemi.alchemicpvp.listeners.cmds.VanishCommand;
import com.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import com.alchemi.alchemicpvp.listeners.tabcomplete.SpyTabComplete;
import com.alchemi.alchemicpvp.listeners.tabcomplete.StatsTabComplete;

import net.milkbowl.vault.chat.Chat;

public class main extends JavaPlugin {

	public static main instance;
	
	public Chat chat;
	public static boolean AnimatedNames = false;
	
	public static Messenger messenger;
	public static FileManager fileManager;
	
	public StaffChat staffChat;
	
	public static final int MESSAGES_FILE_VERSION = 5;
	public static final int CONFIG_FILE_VERSION = 3;
	
	public static File MESSAGES_FILE;
	public static File CONFIG_FILE;
	
	public File playerData;
	
	private HashMap<String, CHECK> checkedPlayers = new HashMap<String, CHECK>();
	
	public HashMap<String, SpyListener> spies = new HashMap<String, SpyListener>();
	
	@Override
	public void onEnable() {
		instance = this;
		
		MESSAGES_FILE = new File(getDataFolder(), "messages.yml");
		CONFIG_FILE = new File(getDataFolder(), "config.yml");
		
		messenger = new Messenger(this);
		
		try {
			Config.enable();
		} catch (IOException | InvalidConfigurationException e) {
			
			getServer().getPluginManager().disablePlugin(this);
			System.err.println("[AlchemicPVP]: Unable to load configurations! Disabling plugin.");
			e.printStackTrace();
			
		}
		
		AnimatedNames = getServer().getPluginManager().getPlugin("AnimatedName") != null;
		
		if (setupChat()) {
			messenger.print("Vault dependent chat installed! Using Vault api chat.");
		}
		
		staffChat = new StaffChat();
		registerCommands();
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(staffChat, this);
		
		
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
		getCommand("nick").setExecutor(new NickCommand());
		getCommand("whois").setExecutor(new WhoCommand());
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		
		getCommand("socialspy").setTabCompleter(new SpyTabComplete());
		getCommand("stats").setTabCompleter(new StatsTabComplete());
		
		messenger.print("Commands registered.");
		
	}

	public boolean hasPermission(Player player, String perm) {
		
		return player.isOp() || player.hasPermission(perm);
	}
	
	public boolean hasPermission(CommandSender sender, String perm) {
		return sender instanceof Player ? sender.isOp() || hasPermission((Player) sender, perm) : true;
	}
	
	private boolean setupChat() {
		if(getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if (rsp == null) return false;
		
		chat = rsp.getProvider();
		
		return chat != null;
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
