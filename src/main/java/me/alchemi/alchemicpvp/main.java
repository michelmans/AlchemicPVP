package me.alchemi.alchemicpvp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;
import me.alchemi.alchemicpvp.listeners.CHECK;
import me.alchemi.alchemicpvp.listeners.Events;
import me.alchemi.alchemicpvp.listeners.ItemListeners;
import me.alchemi.alchemicpvp.listeners.SpyListener;
import me.alchemi.alchemicpvp.listeners.StaffChat;
import me.alchemi.alchemicpvp.listeners.cmds.CheckCommand;
import me.alchemi.alchemicpvp.listeners.cmds.FlyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.GameModeCommand;
import me.alchemi.alchemicpvp.listeners.cmds.GiveWandCommand;
import me.alchemi.alchemicpvp.listeners.cmds.InvseeCommand;
import me.alchemi.alchemicpvp.listeners.cmds.MessageCommand;
import me.alchemi.alchemicpvp.listeners.cmds.NickCommand;
import me.alchemi.alchemicpvp.listeners.cmds.ReloadCommand;
import me.alchemi.alchemicpvp.listeners.cmds.ReplyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SmiteCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SpawnCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SpyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.StaffChatCommand;
import me.alchemi.alchemicpvp.listeners.cmds.StatsCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SudoCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpoCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpoHereCommand;
import me.alchemi.alchemicpvp.listeners.cmds.UncheckCommand;
import me.alchemi.alchemicpvp.listeners.cmds.VanishCommand;
import me.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import me.alchemi.alchemicpvp.listeners.tabcomplete.GameModeTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.GiveWandComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.SpyTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.StatsTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.TpTabComplete;
import me.alchemi.alchemicpvp.objects.wands.DragonStick;
import me.alchemi.alchemicpvp.objects.wands.MagicWand;
import me.alchemi.alchemicpvp.objects.worldguard.WorldGuard;
import net.milkbowl.vault.chat.Chat;

public class main extends PluginBase {

	private static main instance;
	
	public Chat chat;
	public static boolean AnimatedNames = false;
	public static boolean worldGuard;
	
	public DragonStick dragon;
	public MagicWand magic;
	
	public StaffChat staffChat;
	
	private WorldGuard wg;
	
	public static Config config;
	
	public File playerData;
	
	private HashMap<String, CHECK> checkedPlayers = new HashMap<String, CHECK>();
	
	public HashMap<String, SpyListener> spies = new HashMap<String, SpyListener>();
	
	@Override
	public void onLoad() {
		worldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
		if (worldGuard) {
			wg = new WorldGuard();
			wg.onLoad();
		}
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		setMessenger(new Messenger(this));
		
		try {
			config = new Config();
		} catch (IOException | InvalidConfigurationException e) {
			
			getServer().getPluginManager().disablePlugin(this);
			System.err.println("[AlchemicPVP]: Unable to load configurations! Disabling plugin.");
			e.printStackTrace();
			
		}
		new ItemListeners();
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
		
		if (worldGuard) wg.onEnable();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, "YOmOmma"));
		}
		
		dragon = new DragonStick();
		magic = new MagicWand();
		
		messenger.print("&1The die is &8cast...");
	}
	
	@Override
	public void onDisable() {
		if (worldGuard) wg.clearBorders();
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
		getCommand("sudo").setExecutor(new SudoCommand());
		getCommand("givewand").setExecutor(new GiveWandCommand());
		getCommand("tpo").setExecutor(new TpoCommand());
		getCommand("tpohere").setExecutor(new TpoHereCommand());
		getCommand("tp").setExecutor(new TpCommand());
		getCommand("gamemode").setExecutor(new GameModeCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		
		getCommand("socialspy").setTabCompleter(new SpyTabComplete());
		getCommand("stats").setTabCompleter(new StatsTabComplete());
		getCommand("givewand").setTabCompleter(new GiveWandComplete());
		getCommand("tp").setTabCompleter(new TpTabComplete());
		getCommand("gamemode").setTabCompleter(new GameModeTabComplete());		
		
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
	
	public static main getInstance() {
		return instance;
	}
	
	public WorldGuard getWorldGuard() {
		return wg;
	}
}