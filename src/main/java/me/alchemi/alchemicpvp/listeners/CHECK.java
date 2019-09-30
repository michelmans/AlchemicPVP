package me.alchemi.alchemicpvp.listeners;

import org.bukkit.Bukkit;
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

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.meta.VanishMeta;

public class CHECK implements Listener{

	private final Player player;
	private static Team checkers;
	private static ItemStack air = new ItemStack(MaterialWrapper.AIR.getMaterial());
	
	public CHECK(Player pl) {
		
		if (PvP.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers") != null) checkers = PvP.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeam("checkers");
		else checkers = PvP.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("checkers");
		
		checkers.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		player = pl;
		pl.setAllowFlight(true);
		pl.setFoodLevel(40);
		
		checkers.addEntry(getPlayer());
		
		PvP.getInstance().getServer().getPluginManager().registerEvents(this, PvP.getInstance());
		if (!PersistentMeta.hasMeta(pl, VanishMeta.class) || !PersistentMeta.getMeta(pl, VanishMeta.class).asBoolean()) vanishToggle(pl);
	}
	
	public String getPlayer() {
		return player.getName();
	}
	
	public static void vanishToggle(Player player) {
		String prefix = PvP.getInstance().chat.getGroupPrefix(player.getWorld(), PvP.getInstance().chat.getPlayerGroups(player)[0]);
		boolean vanish = false;
		
		if (PersistentMeta.hasMeta(player, VanishMeta.class)) vanish = PersistentMeta.getMeta(player, VanishMeta.class).asBoolean();
		
		for (Player OnPl : PvP.getInstance().getServer().getOnlinePlayers()) {
			if (!PvP.getInstance().hasPermission(OnPl, "alchemicpvp.check.bypass")) {
				if (vanish) {
					OnPl.showPlayer(PvP.getInstance(), player);
				} else {
					OnPl.hidePlayer(PvP.getInstance(), player);
				}
			}
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "animatednames reload");
		if (vanish) {
			player.setPlayerListName(Messenger.formatString(prefix + player.getName()));
			if (Config.Stats.POTION_EFFECT.asBoolean()) player.removePotionEffect(PotionEffectType.INVISIBILITY);
			vanish = false;
			PvP.getInstance().getMessenger().sendMessage(Messages.UNVANISH.value(), player);
		}
		else {
			player.setPlayerListName(Messenger.formatString(Messages.CHECK_VANISHTAG.value() + prefix + player.getName()));
			if (Config.Stats.POTION_EFFECT.asBoolean()) player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 9, true, false), true);
			vanish = true;
			PvP.getInstance().getMessenger().sendMessage(Messages.VANISH.value(), player);
		}
		
		player.setMetadata(VanishMeta.class.getName(), new VanishMeta(PvP.getInstance(), vanish));
		
	}
	
	public void remove() {
		
		if (PersistentMeta.hasMeta(player, VanishMeta.class) && PersistentMeta.getMeta(player, VanishMeta.class).asBoolean()) vanishToggle(player);
		
		checkers.removeEntry(getPlayer());
				
		HandlerList.unregisterAll(this);
		PvP.getInstance().unRegisterCheck(getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if ((((VanishMeta)player.getMetadata("vanish").get(0)).asBoolean()) && !PvP.getInstance().hasPermission(e.getPlayer(), "alchemicpvp.check.bypass")) {
			e.getPlayer().hidePlayer(PvP.getInstance(), player);
		}
	}
	
	@EventHandler 
	public void onPlayerLeave(PlayerQuitEvent e){
		if (e.getPlayer().equals(player)) {
			e.getPlayer().performCommand("uncheck");
		} else if (!e.getPlayer().canSee(player)) {
			e.getPlayer().showPlayer(PvP.getInstance(), player);
		}
		
	}
	
	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && ((Player)e.getEntity()).equals(player)) {
			e.setCancelled(true);
			PvP.getInstance().getMessenger().sendMessage(Messages.CHECK_NOHURTING.value(), player);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getPlayer().equals(player) && e.getItem() != null && !e.getItem().equals(air)) {
			
			PvP.getInstance().getServer().getScheduler().runTaskLater(PvP.getInstance(), new Runnable() {
				
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
				PvP.getInstance().getServer().getScheduler().runTaskLater(PvP.getInstance(), new Runnable() {
					
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
			PvP.getInstance().getMessenger().sendMessage(Messages.CHECK_NOHURTING.value(), player);
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
