package com.alchemi.alchemicpvp.listeners;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.attribute.Attribute;
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
import com.alchemi.alchemicpvp.Config.MESSAGES;
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
		
		if (main.getInstance().hasPermission(e.getPlayer(), "alchemicpvp.spy") && !main.getInstance().spies.containsKey(e.getPlayer().getName())) e.getPlayer().performCommand("socialspy");
		
		if (!Library.hasMeta(e.getPlayer(), VanishMeta.class)) e.getPlayer().setMetadata(VanishMeta.class.getSimpleName(), new VanishMeta(main.getInstance(), false));
		e.getPlayer().setMetadata(StatsMeta.class.getSimpleName(), new StatsMeta(e.getPlayer()));
		if (!Library.hasMeta(e.getPlayer(), NickMeta.class)) e.getPlayer().setMetadata(NickMeta.class.getSimpleName(), new NickMeta(StatsMeta.getStats(e.getPlayer()).getNickname()));
		
		if (WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()) != null 
				&& !WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()).equals(e.getPlayer())) {
			
			TextComponent mainComponent = new TextComponent(Messenger.cc(MESSAGES.NICK_TAKEN1.value().replace("$name$", Library.getMeta(e.getPlayer(), NickMeta.class).asString())));
			TextComponent clickComponent = new TextComponent("\n" + Messenger.cc(MESSAGES.NICK_TAKEN2.value()));
			clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
			clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
			mainComponent.addExtra(clickComponent);
			e.getPlayer().spigot().sendMessage(mainComponent);
			
		} else e.getPlayer().setDisplayName(Library.getMeta(e.getPlayer(), NickMeta.class).asString());
		
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.getEntity().getInventory().clear();
		e.getEntity().updateInventory();
		
		e.setKeepInventory(true);
		
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
		
		if (Config.deathMessages.size() > 1) main.getInstance().getMessenger().broadcast(Config.deathMessages.get(rand.nextInt(Config.deathMessages.size()))
				.replace("$victim$", e.getEntity().getDisplayName())
				.replace("$killer$", e.getEntity().getKiller().getDisplayName())
				.replace("$item$", itemKill), false);
		else main.getInstance().getMessenger().print(Config.deathMessages);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (main.getInstance().hasPermission(e.getPlayer(), "alchemicpvp.spy") && main.getInstance().spies.containsKey(e.getPlayer().getName())) e.getPlayer().performCommand("socialspy");
		for (SpyListener sl : main.getInstance().spies.values()) {
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
