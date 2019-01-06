package com.alchemi.alchemicpvp.listeners;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.Library;
import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.PlayerStats;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import com.alchemi.alchemicpvp.meta.NickMeta;
import com.alchemi.alchemicpvp.meta.VanishMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Events implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().getInventory().clear();
		
		if (main.instance.hasPermission(e.getPlayer(), "alchemicpvp.spy")) e.getPlayer().performCommand("socialspy");
		
		File dataFile = new File(main.instance.playerData, e.getPlayer().getUniqueId().toString() + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		if (!dataFile.exists()) {
			fc.set("name", e.getPlayer().getName());
			fc.set("nickname", e.getPlayer().getDisplayName());
			fc.set("kills", 0);
			fc.set("deaths", 0);
			fc.createSection("killstreak");
			fc.set("killstreak.best", 0);
			fc.set("killstreak.current", 0);
			try {
				fc.save(dataFile);
			} catch (IOException e1) {}
		} else {
			fc = YamlConfiguration.loadConfiguration(dataFile);
		}
		
		main.instance.registerPlayer(new PlayerStats(fc, dataFile));
		
		if (!Library.hasMeta(e.getPlayer(), VanishMeta.NAME, VanishMeta.class)) e.getPlayer().setMetadata(VanishMeta.NAME, new VanishMeta(main.instance, false));
		if (!Library.hasMeta(e.getPlayer(), NickMeta.NAME, NickMeta.class)) e.getPlayer().setMetadata(NickMeta.NAME, new NickMeta(fc.getString("nickname")));
		
		if (WhoCommand.whoIs(fc.getString("nickname")) != null 
				&& !WhoCommand.whoIs(fc.getString("nickname")).equals(e.getPlayer())) {
			
			TextComponent mainComponent = new TextComponent(Messenger.cc(Messenger.parseVars(main.instance.messenger.getMessage("Nick.Taken1"), new HashMap<String, Object>(){
				{
					put("$name$", Library.getMeta(e.getPlayer(), NickMeta.NAME, NickMeta.class).asString());
				}
			}))) ;
			TextComponent clickComponent = new TextComponent("\n" + Messenger.cc(main.instance.messenger.getMessage("Nick.Taken2")));
			clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
			clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
			mainComponent.addExtra(clickComponent);
			e.getPlayer().spigot().sendMessage(mainComponent);
			
		} else e.getPlayer().setDisplayName(Library.getMeta(e.getPlayer(), NickMeta.NAME, NickMeta.class).asString());
		
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() == null) return;
		
		PlayerStats victim = main.instance.getPlayer(e.getEntity().getName());
		victim.setCurrentKillstreak(0);
		victim.updateDeaths(1);
		
		PlayerStats killer = main.instance.getPlayer(e.getEntity().getKiller().getName());
		killer.updateKills(1);
		killer.updateKillstreaks(1);
		
		e.getEntity().getKiller().setHealth(e.getEntity().getKiller().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		
		
		//Death Messages
		if (!main.instance.config.getBoolean("Stats.deathMessages")) return;
		
		ItemStack killItem = e.getEntity().getKiller().getInventory().getItemInMainHand();
		String item = killItem.hasItemMeta() ? killItem.getItemMeta().hasDisplayName() ? killItem.getItemMeta().getDisplayName() : killItem.getType().toString().replaceAll("_", " ").toLowerCase() : killItem.getType().toString().replaceAll("_", " ").toLowerCase(); 
		
		String itemKill;
		if (Arrays.asList(new String[] {"a", "i", "e", "o", "u", "y"}).contains(String.valueOf(item.charAt(0)).toLowerCase())) {
			itemKill = "an " + item;
		} else {
			itemKill = "a " + item;
		}
		
		Random rand = new Random();
		List<String> deathMessages = main.instance.fileManager.getConfig("messages.yml").getStringList("AlchemicPVP.DeathMessages");
		
		if (deathMessages.size() > 1) main.instance.messenger.broadcast(deathMessages.get(rand.nextInt(deathMessages.size())), new HashMap<String, Object>(){
			{
				put("$victim$", ((Player) e.getEntity()).getDisplayName());
				put("$killer$", e.getEntity().getKiller().getDisplayName());
				put("$item$", itemKill);
			}
		}, false);
		else main.instance.messenger.print(main.instance.fileManager.getConfig("messages.yml").getStringList("AlchemicPVP.DeathMessages"));
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		
		for (SpyListener sl : main.instance.spies.values()) {
			if (sl.isIgnoring(e.getPlayer())) sl.unIgnorePlayer(e.getPlayer());
		}
		
		main.instance.unregisterPlayer(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onStrike(BlockIgniteEvent e) {
		if (e.getCause().equals(IgniteCause.LIGHTNING)) {
			if (!e.getBlock().getWorld().hasStorm()) {
				e.setCancelled(true);
			}
		}
	}
}
