package com.alchemi.alchemicpvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.alchemi.al.Library;
import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.VanishMeta;

public class CHECK implements Listener{

	private final Player player;
	private static Team checkers;
	private static ItemStack air = new ItemStack(Material.AIR);
	
	public CHECK(Player pl) {
		
		if (main.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers") != null) checkers = main.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers");
		else checkers = main.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("checkers");
		
		checkers.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		player = pl;
		pl.setAllowFlight(true);
		pl.setFoodLevel(40);
		
		checkers.addEntry(getPlayer());
		
		main.getInstance().getServer().getPluginManager().registerEvents(this, main.getInstance());
		if (!Library.hasMeta(pl, VanishMeta.class) || !Library.getMeta(pl, VanishMeta.class).asBoolean()) vanishToggle(pl);
	}
	
	public String getPlayer() {
		return player.getName();
	}
	
	public static void vanishToggle(Player player) {
		String prefix = main.getInstance().chat.getGroupPrefix(player.getWorld(), main.getInstance().chat.getPlayerGroups(player)[0]);
		boolean vanish = false;
		
		if (Library.hasMeta(player, VanishMeta.class)) vanish = Library.getMeta(player, VanishMeta.class).asBoolean();
		
		for (Player OnPl : main.getInstance().getServer().getOnlinePlayers()) {
			if (!main.getInstance().hasPermission(OnPl, "alchemicpvp.check.bypass")) {
				if (vanish) {
					OnPl.showPlayer(main.getInstance(), player);
				} else {
					OnPl.hidePlayer(main.getInstance(), player);
				}
			}
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "animatednames reload");
		if (vanish) {
			player.setPlayerListName(Messenger.cc(prefix + player.getName()));
			if (Config.STATS.POTION_EFFECT.asBoolean()) player.removePotionEffect(PotionEffectType.INVISIBILITY);
			vanish = false;
			main.getInstance().getMessenger().sendMessage(MESSAGES.UNVANISH.value(), player);
		}
		else {
			player.setPlayerListName(Messenger.cc(MESSAGES.CHECK_VANISHTAG.value() + prefix + player.getName()));
			if (Config.STATS.POTION_EFFECT.asBoolean()) player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 9, true, false), true);
			vanish = true;
			main.getInstance().getMessenger().sendMessage(MESSAGES.VANISH.value(), player);
		}
		
		player.removeMetadata(VanishMeta.class.getSimpleName(), main.getInstance());
		player.setMetadata(VanishMeta.class.getSimpleName(), new VanishMeta(main.getInstance(), vanish));
		
	}
	
	public void remove() {
		
		if (Library.hasMeta(player, VanishMeta.class) && Library.getMeta(player, VanishMeta.class).asBoolean()) vanishToggle(player);
		
		checkers.removeEntry(getPlayer());
				
		HandlerList.unregisterAll(this);
		main.getInstance().unRegisterCheck(getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if ((((VanishMeta)player.getMetadata("vanish").get(0)).asBoolean()) && !main.getInstance().hasPermission(e.getPlayer(), "alchemicpvp.check.bypass")) {
			e.getPlayer().hidePlayer(main.getInstance(), player);
		}
	}
	
	@EventHandler 
	public void onPlayerLeave(PlayerQuitEvent e){
		if (e.getPlayer().equals(player)) {
			e.getPlayer().performCommand("uncheck");
		} else if (!e.getPlayer().canSee(player)) {
			e.getPlayer().showPlayer(main.getInstance(), player);
		}
		
	}
	
	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && ((Player)e.getEntity()).equals(player)) {
			e.setCancelled(true);
			main.getInstance().getMessenger().sendMessage(MESSAGES.CHECK_NOHURTING.value(), player);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getPlayer().equals(player) && e.getItem() != null && !e.getItem().equals(air)) {
			
			main.getInstance().getServer().getScheduler().runTaskLater(main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					player.getInventory().setArmorContents(new ItemStack[] {air, air, air, air});
					
				}
			}, 1);
		}
	}
	
	@EventHandler
	public void onPlayerInventoryInteract(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		
		if (player.equals(this.player) && !e.getCurrentItem().equals(air)) {
			if (e.getSlotType() != SlotType.OUTSIDE) {
				main.getInstance().getServer().getScheduler().runTaskLater(main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						player.getInventory().setArmorContents(new ItemStack[] {air, air, air, air});
						
					}
				}, 1);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (!e.getEntityType().equals(EntityType.PLAYER)) return;
		
		Player player = (Player) e.getEntity();
		
		if (player.equals(this.player)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerByCheckDamage(EntityDamageByEntityEvent e) {
		if (!e.getEntityType().equals(EntityType.PLAYER)) return;
		
		if (e.getDamager() instanceof Player && ((Player) e.getDamager()).equals(this.player)) {
			e.setCancelled(true);
			main.getInstance().getMessenger().sendMessage(MESSAGES.CHECK_NOHURTING.value(), player);
		}
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && ((Player)e.getEntity()).equals(player)) { 
			e.setCancelled(true);
			e.setFoodLevel(40);
		}
	}
	
	@EventHandler
	public void onFire(EntityCombustEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && ((Player)e.getEntity()).equals(player)) {
			e.setCancelled(true); 
		}
	}
}
