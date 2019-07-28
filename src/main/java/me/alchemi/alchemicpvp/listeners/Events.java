package me.alchemi.alchemicpvp.listeners;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.PlayerStats;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.listeners.cmds.WhoCommand;
import me.alchemi.alchemicpvp.meta.CooldownMeta;
import me.alchemi.alchemicpvp.meta.FireExtMeta;
import me.alchemi.alchemicpvp.meta.NickMeta;
import me.alchemi.alchemicpvp.meta.SecondCooldownMeta;
import me.alchemi.alchemicpvp.meta.StatsMeta;
import me.alchemi.alchemicpvp.meta.VanishMeta;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Events implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (Config.clearInventory) e.getPlayer().getInventory().clear();
		
		if (main.getInstance().hasPermission(e.getPlayer(), "alchemicpvp.spy") && !main.getInstance().spies.containsKey(e.getPlayer().getName())) e.getPlayer().performCommand("socialspy");
		
		if (!PersistentMeta.hasMeta(e.getPlayer(), VanishMeta.class)) e.getPlayer().setMetadata(VanishMeta.class.getName(), new VanishMeta(main.getInstance(), false));
		e.getPlayer().setMetadata(StatsMeta.class.getName(), new StatsMeta(e.getPlayer()));
		if (!PersistentMeta.hasMeta(e.getPlayer(), NickMeta.class)) e.getPlayer().setMetadata(NickMeta.class.getName(), new NickMeta(StatsMeta.getStats(e.getPlayer()).getNickname()));
		
		if (WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()) != null 
				&& !WhoCommand.whoIs(StatsMeta.getStats(e.getPlayer()).getNickname()).equals(e.getPlayer())) {
			
			TextComponent mainComponent = new TextComponent(Messenger.formatString(Messages.NICK_TAKEN1.value().replace("$name$", PersistentMeta.getMeta(e.getPlayer(), NickMeta.class).asString())));
			TextComponent clickComponent = new TextComponent("\n" + Messenger.formatString(Messages.NICK_TAKEN2.value()));
			clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
			clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
			mainComponent.addExtra(clickComponent);
			e.getPlayer().spigot().sendMessage(mainComponent);
			
		} else e.getPlayer().setDisplayName(PersistentMeta.getMeta(e.getPlayer(), NickMeta.class).asString());
		
		main.getInstance().getSsb().addPlayer(e.getPlayer());
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
		
		e.getEntity().getInventory().clear();
		e.getEntity().updateInventory();
		e.getEntity().removeMetadata(CooldownMeta.class.getName(), main.getInstance());
		e.getEntity().removeMetadata(SecondCooldownMeta.class.getName(), main.getInstance());
		
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
		if (!Config.Stats.DEATHMESSAGES.asBoolean()) return;
		
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
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getHitEntity() != null && e.getHitEntity() instanceof LivingEntity && e.getEntity() instanceof Arrow && e.getEntity().getCustomName().equals("flame")) {
			((LivingEntity)e.getHitEntity()).setNoDamageTicks(0);
		}
		
		if (e.getHitBlock() != null && e.getEntity().getFireTicks() > 0 
				&& !(e.getEntityType().equals(EntityType.SMALL_FIREBALL)
						|| e.getEntityType().equals(EntityType.FIREBALL)
						|| e.getEntityType().equals(EntityType.DRAGON_FIREBALL))) {
			Location loc = e.getHitBlock().getLocation();
			
			if (main.worldGuard && !main.getInstance().getWorldGuard().isPvPDenied(loc)) {
				return;
			}
			
			switch(e.getHitBlockFace()) {
			case DOWN:
				break;
			case EAST:
				loc.add(1, 0, 0);
				break;
			case EAST_NORTH_EAST:
				loc.add(1, 0, 0);
				break;
			case EAST_SOUTH_EAST:
				loc.add(1, 0, 0);
				break;
			case NORTH:
				loc.add(0, 0, -1);
				break;
			case NORTH_EAST:
				loc.add(0, 0, -1);
				break;
			case NORTH_NORTH_EAST:
				loc.add(0, 0, -1);
				break;
			case NORTH_NORTH_WEST:
				loc.add(0, 0, -1);
				break;
			case NORTH_WEST:
				loc.add(0, 0, -1);
				break;
			case SELF:
				break;
			case SOUTH:
				loc.add(0, 0, 1);
				break;
			case SOUTH_EAST:
				loc.add(0, 0, 1);
				break;
			case SOUTH_SOUTH_EAST:
				loc.add(0, 0, 1);
				break;
			case SOUTH_SOUTH_WEST:
				loc.add(0, 0, 1);
				break;
			case SOUTH_WEST:
				loc.add(0, 0, 1);
				break;
			case UP:
				loc.add(0, 1, 0);
				break;
			case WEST:
				loc.add(-1, 0, 0);
				break;
			case WEST_NORTH_WEST:
				loc.add(-1, 0, 0);
				break;
			case WEST_SOUTH_WEST:
				loc.add(-1, 0, 0);
				break;
			default:
				loc.add(0, 1, 0);
				break;
			}
			
			if (loc.getWorld().getBlockAt(loc).isEmpty()) {
				
				Block fire = loc.getWorld().getBlockAt(loc);
				fire.setType(MaterialWrapper.FIRE.getMaterial());
				Fire fireData = (Fire) Bukkit.createBlockData(MaterialWrapper.FIRE.getMaterial());
				try {
					fireData.setFace(e.getHitBlockFace().getOppositeFace(), true);
				} catch (IllegalArgumentException ex) {}
				fire.setBlockData(fireData);
				fire.setMetadata(FireExtMeta.class.getName(), new FireExtMeta(fire));
				
			}
		}
	}
		
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		if (e.getEntity().getCustomName().equals("firebawl")) {
			e.setCancelled(true);
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
