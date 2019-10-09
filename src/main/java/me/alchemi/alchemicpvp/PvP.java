package me.alchemi.alchemicpvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;
import me.alchemi.alchemicpvp.Config.Storage;
import me.alchemi.alchemicpvp.listeners.CHECK;
import me.alchemi.alchemicpvp.listeners.Events;
import me.alchemi.alchemicpvp.listeners.ItemListeners;
import me.alchemi.alchemicpvp.listeners.PotionBlocking;
import me.alchemi.alchemicpvp.listeners.PunishListener;
import me.alchemi.alchemicpvp.listeners.SpyListener;
import me.alchemi.alchemicpvp.listeners.cmds.CheckCommand;
import me.alchemi.alchemicpvp.listeners.cmds.FlyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.GameModeCommand;
import me.alchemi.alchemicpvp.listeners.cmds.GiveWandCommand;
import me.alchemi.alchemicpvp.listeners.cmds.MessageCommand;
import me.alchemi.alchemicpvp.listeners.cmds.NickCommand;
import me.alchemi.alchemicpvp.listeners.cmds.ReloadCommand;
import me.alchemi.alchemicpvp.listeners.cmds.ReplyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SpawnCommand;
import me.alchemi.alchemicpvp.listeners.cmds.SpyCommand;
import me.alchemi.alchemicpvp.listeners.cmds.StatsCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpoCommand;
import me.alchemi.alchemicpvp.listeners.cmds.TpoHereCommand;
import me.alchemi.alchemicpvp.listeners.cmds.UncheckCommand;
import me.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import me.alchemi.alchemicpvp.listeners.tabcomplete.GameModeTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.GiveWandComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.SpyTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.StatsTabComplete;
import me.alchemi.alchemicpvp.listeners.tabcomplete.TpTabComplete;
import me.alchemi.alchemicpvp.objects.StatsScoreboard;
import me.alchemi.alchemicpvp.objects.wands.DragonStick;
import me.alchemi.alchemicpvp.objects.wands.MagicWand;
import me.alchemi.alchemicpvp.objects.worldguard.WorldGuard;
import me.alchemi.alchemicpvp.stats.DataType;
import me.alchemi.alchemicpvp.stats.MySQLStats;
import net.milkbowl.vault.chat.Chat;

public class PvP extends PluginBase {

	private static PvP instance;
	
	public Chat chat;
	public static boolean AnimatedNames = false;
	public static boolean worldGuard;
	
	public DragonStick dragon;
	public MagicWand magic;
	
	private WorldGuard wg;
	
	private StatsScoreboard ssb;
	
	public static Config config;
	
	public File playerData;
	
	private HashMap<String, CHECK> checkedPlayers = new HashMap<String, CHECK>();
	
	public HashMap<String, SpyListener> spies = new HashMap<String, SpyListener>();
	
	private List<UUID> toKill = new ArrayList<UUID>();
	
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
		AnimatedNames = getServer().getPluginManager().getPlugin("AnimatedNames") != null;
		
		if (setupChat()) {
			messenger.print("Vault dependent chat installed! Using Vault api chat.");
		}
		
		registerCommands();
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		if (DataType.valueOf(Storage.TYPE.asString()) == DataType.YML) {
			playerData = new File(getDataFolder(), "playerdata");
			if (!playerData.exists()) playerData.mkdir();
		} else {
			MySQLStats.init();
		}
		
		if (worldGuard) {
			wg.onEnable();
			getServer().getPluginManager().registerEvents(new PotionBlocking(), this);
		}
		
		if (Bukkit.getPluginManager().getPlugin("CombatLogX") != null) new PunishListener();
		
		ssb = new StatsScoreboard();
		
		dragon = new DragonStick();
		magic = new MagicWand();		
		
		Bukkit.getOnlinePlayers().forEach(new Consumer<Player>() {
			
			@Override
			public void accept(Player t) {
				new Events().onPlayerJoin(new PlayerJoinEvent(t, ""));
			}
		});
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
		getCommand("socialspy").setExecutor(new SpyCommand());
		getCommand("nick").setExecutor(new NickCommand());
		getCommand("whois").setExecutor(new WhoCommand());
		getCommand("givewand").setExecutor(new GiveWandCommand());
		getCommand("tpo").setExecutor(new TpoCommand());
		getCommand("tpohere").setExecutor(new TpoHereCommand());
		getCommand("tp").setExecutor(new TpCommand());
		getCommand("gamemode").setExecutor(new GameModeCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		
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
	
	public static PvP getInstance() {
		return instance;
	}
	
	public WorldGuard getWorldGuard() {
		return wg;
	}

	/**
	 * @return the ssb
	 */
	public final StatsScoreboard getSsb() {
		return ssb;
	}

	/**
	 * @return the toKill
	 */
	public List<UUID> getToKill() {
		return toKill;
	}

	/**
	 * @param toKill the toKill to set
	 */
	public void addToKill(OfflinePlayer toKill) {
		this.toKill.add(toKill.getUniqueId());
	}
	
	public void addToKill(UUID uuid) {
		this.toKill.add(uuid);
	}
	
	/**
	 * @param killed the killed to remove
	 */
	public void removeToKill(OfflinePlayer killed) {
		this.toKill.remove(killed.getUniqueId());
	}
	
	public void removeToKill(UUID uuid) {
		this.toKill.remove(uuid);
	}
	
	public boolean toKill(Player toKill) {
		return this.toKill.contains(toKill.getUniqueId());
	}
	
}
