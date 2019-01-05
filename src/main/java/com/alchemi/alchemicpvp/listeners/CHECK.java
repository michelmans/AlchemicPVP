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
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.VanishMeta;
import com.alchemi.alchemicpvp.main;

public class CHECK implements Listener{

	private final Player player;
	private static Team checkers;
	private static ItemStack air = new ItemStack(Material.AIR);
	
	public CHECK(Player pl) {
		
		if (main.instance.getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers") != null) checkers = main.instance.getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers");
		else checkers = main.instance.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("checkers");
		
		checkers.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		player = pl;
		pl.setAllowFlight(true);
		pl.setFoodLevel(40);
		
		checkers.addEntry(getPlayer());
		
		player.getInventory().setArmorContents(new ItemStack[] {air, air, air, air});
		
		main.instance.getServer().getPluginManager().registerEvents(this, main.instance);
		if (!((VanishMeta)player.getMetadata("vanish").get(0)).asBoolean()) vanishToggle(pl);
	}
	
	public String getPlayer() {
		return player.getName();
	}
	
	public static void vanishToggle(Player player) {
		String prefix = main.instance.chat.getGroupPrefix(player.getWorld(), main.instance.chat.getPlayerGroups(player)[0]);
		boolean vanish = false;
		
		if (player.getMetadata("vanish").size() > 0) {
			for (MetadataValue meta : player.getMetadata("vanish")) {
				if (meta instanceof VanishMeta) {
					vanish = (boolean) meta.value();
					System.out.println(vanish);
					break;
				}
			}
		}
		
		for (Player OnPl : main.instance.getServer().getOnlinePlayers()) {
			if (!main.instance.hasPermission(OnPl, "alchemicpvp.check.bypass")) {
				if (vanish) {
					OnPl.showPlayer(main.instance, player);
				} else {
					OnPl.hidePlayer(main.instance, player);
				}
			}
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "animatednames reload");
		if (vanish) {
			player.setPlayerListName(Messenger.cc(prefix + player.getDisplayName()));
			if (main.instance.config.getBoolean("Stats.potionEffect")) player.removePotionEffect(PotionEffectType.INVISIBILITY);
			vanish = false;
			main.instance.messenger.sendMessage("Unvanish", player);
		}
		else {
			player.setPlayerListName(Messenger.cc(main.instance.messenger.getMessage("Check.VanishTag") + prefix + player.getDisplayName()));
			if (main.instance.config.getBoolean("Stats.potionEffect")) player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 9, true, false), true);
			vanish = true;
			main.instance.messenger.sendMessage("Vanish", player);
		}
		
		player.removeMetadata("vanish", main.instance);
		player.setMetadata("vanish", new VanishMeta(main.instance, vanish));
		
	}
	
	public void remove() {
		
		if (((VanishMeta)player.getMetadata("vanish").get(0)).asBoolean()) vanishToggle(player);
		
		checkers.removeEntry(getPlayer());
				
		HandlerList.unregisterAll(this);
		main.instance.unRegisterCheck(getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!main.instance.hasPermission(e.getPlayer(), "alchemicpvp.check.bypass")) {
			e.getPlayer().hidePlayer(main.instance, player);
		}
	}
	
	@EventHandler 
	public void onPlayerLeave(PlayerQuitEvent e){
		if (e.getPlayer().equals(player)) {
			e.getPlayer().performCommand("uncheck");
		} else if (!e.getPlayer().canSee(player)) {
			e.getPlayer().showPlayer(main.instance, player);
		}
		
	}
	
	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && ((Player)e.getEntity()).equals(player)) {
			e.setCancelled(true);
			main.instance.messenger.sendMessage("Check.NoHurting", player);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getPlayer().equals(player) && e.getItem() != null && !e.getItem().equals(air)) {
			
			main.instance.getServer().getScheduler().runTaskLater(main.instance, new Runnable() {
				
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
				main.instance.getServer().getScheduler().runTaskLater(main.instance, new Runnable() {
					
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
			main.instance.messenger.sendMessage("Check.NoHurting", player);
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
