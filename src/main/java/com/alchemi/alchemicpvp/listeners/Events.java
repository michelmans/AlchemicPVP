package com.alchemi.alchemicpvp.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.attribute.Attribute;
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
import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.PlayerStats;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import com.alchemi.alchemicpvp.meta.NickMeta;
import com.alchemi.alchemicpvp.meta.StatsMeta;
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
		
		if (main.instance.hasPermission(e.getPlayer(), "alchemicpvp.spy") && !main.instance.spies.containsKey(e.getPlayer().getName())) e.getPlayer().performCommand("socialspy");
		
		if (!Library.hasMeta(e.getPlayer(), VanishMeta.class)) e.getPlayer().setMetadata(VanishMeta.NAME, new VanishMeta(main.instance, false));
		e.getPlayer().setMetadata(StatsMeta.NAME, new StatsMeta(e.getPlayer()));
		if (!Library.hasMeta(e.getPlayer(), NickMeta.class)) e.getPlayer().setMetadata(NickMeta.NAME, new NickMeta(StatsMeta.getStats(e.getPlayer()).getNickname()));
		
		if (WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()) != null 
				&& !WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()).equals(e.getPlayer())) {
			
			TextComponent mainComponent = new TextComponent(Messenger.cc(Messenger.parseVars(main.messenger.getMessage("Nick.Taken1"), new HashMap<String, Object>(){
				{
					put("$name$", Library.getMeta(e.getPlayer(), NickMeta.class).asString());
				}
			}))) ;
			TextComponent clickComponent = new TextComponent("\n" + Messenger.cc(main.messenger.getMessage("Nick.Taken2")));
			clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
			clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
			mainComponent.addExtra(clickComponent);
			e.getPlayer().spigot().sendMessage(mainComponent);
			
		} else e.getPlayer().setDisplayName(Library.getMeta(e.getPlayer(), NickMeta.class).asString());
		
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() == null) return;
		
		PlayerStats victim = StatsMeta.getStats(e.getEntity());
		victim.setCurrentKillstreak(0);
		victim.updateDeaths(1);
		
		PlayerStats killer = StatsMeta.getStats(e.getEntity().getKiller());
		killer.updateKills(1);
		killer.updateKillstreaks(1);
		
		e.getEntity().getKiller().setHealth(e.getEntity().getKiller().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		
		
		//Death Messages
		if (!Config.STATS.DEATHMESSAGES.asBoolean()) return;
		
		ItemStack killItem = e.getEntity().getKiller().getInventory().getItemInMainHand();
		String item = killItem.hasItemMeta() ? killItem.getItemMeta().hasDisplayName() ? killItem.getItemMeta().getDisplayName() : killItem.getType().toString().replaceAll("_", " ").toLowerCase() : killItem.getType().toString().replaceAll("_", " ").toLowerCase(); 
		
		String itemKill;
		if (Arrays.asList(new String[] {"a", "i", "e", "o", "u", "y"}).contains(String.valueOf(item.charAt(0)).toLowerCase())) {
			itemKill = "an " + item;
		} else {
			itemKill = "a " + item;
		}
		
		Random rand = new Random();
		List<String> deathMessages = main.fileManager.getConfig("messages.yml").getStringList("AlchemicPVP.DeathMessages");
		
		if (deathMessages.size() > 1) main.messenger.broadcast(deathMessages.get(rand.nextInt(deathMessages.size())), new HashMap<String, Object>(){
			{
				put("$victim$", ((Player) e.getEntity()).getDisplayName());
				put("$killer$", e.getEntity().getKiller().getDisplayName());
				put("$item$", itemKill);
			}
		}, false);
		else main.messenger.print(main.fileManager.getConfig("messages.yml").getStringList("AlchemicPVP.DeathMessages"));
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (main.instance.hasPermission(e.getPlayer(), "alchemicpvp.spy") && main.instance.spies.containsKey(e.getPlayer().getName())) e.getPlayer().performCommand("socialspy");
		for (SpyListener sl : main.instance.spies.values()) {
			if (sl.isIgnoring(e.getPlayer())) sl.unIgnorePlayer(e.getPlayer());
		}
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
