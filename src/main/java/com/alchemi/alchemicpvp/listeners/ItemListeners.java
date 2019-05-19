package com.alchemi.alchemicpvp.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.alchemi.al.Library;
import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.WANDS;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.CooldownMeta;

public class ItemListeners implements Listener{

	public ItemListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(this, main.getInstance());
	}
	
	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		
		if (e.getMaterial().equals(WANDS.MAGIC_MATERIAL.asMaterial()) && WANDS.MAGIC_ENABLED.asBoolean()
				&& isName(e.getItem(), WANDS.MAGIC_NAME.asString()) 
				&& (!Library.hasMeta(e.getPlayer(), CooldownMeta.class) || Library.getMeta(e.getPlayer(), CooldownMeta.class).asBoolean())) {
			
			Location target = e.getPlayer().getTargetBlockExact(10) != null ? e.getPlayer().getTargetBlockExact(10).getLocation() : null;
			if (e.getAction().equals(Action.LEFT_CLICK_AIR)
							|| e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				
				if (target == null) {
					Vector direction = e.getPlayer().getEyeLocation().getDirection().normalize().multiply(10);
					Location eyeLoc = e.getPlayer().getEyeLocation();
					
					target = eyeLoc.add(direction);
				}
				
				target.getWorld().strikeLightning(target);
				
				e.getPlayer().setMetadata(CooldownMeta.class.getSimpleName(), new CooldownMeta(WANDS.MAGIC_COOLDOWN.asInt(), e.getPlayer()));
			}	
			
		} else if (e.getMaterial().equals(WANDS.DRAGON_MATERIAL.asMaterial()) && WANDS.DRAGON_ENABLED.asBoolean()
				&& isName(e.getItem(), WANDS.DRAGON_NAME.asString()) 
				&& (!Library.hasMeta(e.getPlayer(), CooldownMeta.class) || Library.getMeta(e.getPlayer(), CooldownMeta.class).asBoolean())) {
			
			if (e.getAction().equals(Action.LEFT_CLICK_AIR)
					|| e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				
				Vector direction = e.getPlayer().getEyeLocation().getDirection().normalize();
				
				Fireball fireball = e.getPlayer().launchProjectile(Fireball.class, direction.multiply(4));
				
				fireball.setGravity(true);
				fireball.setIsIncendiary(true);
				
				e.getPlayer().setMetadata(CooldownMeta.class.getSimpleName(), new CooldownMeta(WANDS.DRAGON_COOLDOWN.asInt(), e.getPlayer()));
				
			}
		} else if (Config.immediateConsuming 
				&& (e.getMaterial().isEdible() || e.getMaterial().equals(Material.POTION))) {
			
			if (e.getItem().hasItemMeta() && e.getItem().getItemMeta() instanceof PotionMeta) {
				PotionMeta potionMeta = ((PotionMeta) e.getItem().getItemMeta());
				if (potionMeta.hasCustomEffects()) e.getPlayer().addPotionEffects(potionMeta.getCustomEffects());
				
				PotionData pData = potionMeta.getBasePotionData();
				int duration = 0;
				int amplifier = 0;
				
				if (pData.getType().isInstant()) duration = 1;
				else if (pData.isExtended()) duration = 20 * 480;
				else duration = 20 * 180;
				
				if (pData.isUpgraded()) amplifier++;
				
				e.getPlayer().addPotionEffect(pData.getType().getEffectType().createEffect(duration, amplifier));
				e.getPlayer().getInventory().remove(e.getItem());
				e.getPlayer().updateInventory();
			} else {
				int foodLvl = e.getPlayer().getFoodLevel();
				float saturation = e.getPlayer().getSaturation();
				
				boolean ignoreLimit = false;
				
				switch(e.getMaterial()) {
				case GOLDEN_APPLE:
					foodLvl += 4;
					saturation += 9.6;
					
					PotionEffect[] effects = new PotionEffect[] {new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0),
							new PotionEffect(PotionEffectType.REGENERATION, 100, 1)};
					
					e.getPlayer().addPotionEffects(Arrays.asList(effects));
					
					ignoreLimit = true;
					
					break;
				
				case ENCHANTED_GOLDEN_APPLE:
					foodLvl += 4;
					saturation += 9.6;
					
					PotionEffect[] effects2 = new PotionEffect[] {new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3),
							new PotionEffect(PotionEffectType.REGENERATION, 400, 1),
							new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0),
							new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0)};
					
					e.getPlayer().addPotionEffects(Arrays.asList(effects2	));
					
					ignoreLimit = true;
					
					break;
				
				case RABBIT_STEW:
					foodLvl += 10;
					saturation += 12;
					break;
					
				case COOKED_PORKCHOP:
					foodLvl += 8;
					saturation += 12.8;
					break;
					
				case COOKED_BEEF:
					foodLvl += 8;
					saturation += 12.8;
					break;
					
				case PUMPKIN_PIE:
					foodLvl += 8;
					saturation += 4.8;
					break;
					
				case BEETROOT_SOUP:
					foodLvl += 6;
					saturation += 7.2;
					break;
					
				case COOKED_CHICKEN:
					foodLvl += 6;
					saturation += 7.2;
					break;
					
				case COOKED_MUTTON:
					foodLvl += 6;
					saturation += 9.6;
					break;
					
				case COOKED_SALMON:
					foodLvl += 6;
					saturation += 9.6;
					break;
					
				case BAKED_POTATO:
					foodLvl += 5;
					saturation += 6;
					
				default:
					foodLvl += 2;
					saturation += 1.2;
					break;
				}
				
				if (foodLvl <= 20 || ignoreLimit) {
					e.getPlayer().setFoodLevel(foodLvl);
					e.getPlayer().setSaturation(saturation);
					ItemStack item = e.getItem().clone();
					item.setAmount(1);
					e.getPlayer().getInventory().removeItem(item);
					e.getPlayer().updateInventory();
				}
			}
			
		}
	}
	
	private boolean isName(ItemStack item, String name) {
		return item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName().contentEquals(Messenger.cc(name)) : false : false;
	}
}
