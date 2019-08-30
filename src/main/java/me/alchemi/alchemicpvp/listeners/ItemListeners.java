package me.alchemi.alchemicpvp.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.meta.BooleanMeta;
import me.alchemi.alchemicpvp.meta.TaskIntMeta;

public class ItemListeners implements Listener{

	public ItemListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(this, main.getInstance());
	}
	
	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		
		if (PersistentMeta.hasMeta(e.getPlayer(), TaskIntMeta.class)) {
			Bukkit.getScheduler().cancelTask(PersistentMeta.getMeta(e.getPlayer(), TaskIntMeta.class).asInt());
			e.getPlayer().removeMetadata(TaskIntMeta.class.getName(), main.getInstance());
			
		}
		
		if (Config.immediateConsuming
				&& e.getItem() != null
				&& (MaterialWrapper.getFromItemStack(e.getItem()).isEdible() 
						|| MaterialWrapper.getFromItemStack(e.getItem()).equals(MaterialWrapper.POTION.getMaterial()))) {
			
			if (e.getItem().hasItemMeta() && e.getItem().getItemMeta() instanceof PotionMeta
					&& (!PersistentMeta.hasMeta(e.getPlayer(), BooleanMeta.class))) {
				PotionMeta potionMeta = ((PotionMeta) e.getItem().getItemMeta());
				if (potionMeta.hasCustomEffects()) e.getPlayer().addPotionEffects(potionMeta.getCustomEffects());
				
				PotionData pData = potionMeta.getBasePotionData();
				int duration = 0;
				int amplifier = 0;
				
				if (pData.getType().isInstant()) duration = 1;
				else if (pData.isExtended()) duration = 20 * 480;
				else duration = 20 * 180;
				
				if (pData.isUpgraded()) amplifier++;
				
				if (pData.getType() != PotionType.UNCRAFTABLE && pData.getType().getEffectType() != null) e.getPlayer().addPotionEffect(pData.getType().getEffectType().createEffect(duration, amplifier));
				e.getPlayer().getInventory().remove(e.getItem());
				e.getPlayer().updateInventory();
				
			} else if (!PersistentMeta.hasMeta(e.getPlayer(), BooleanMeta.class)){
				
				int foodLvl = e.getPlayer().getFoodLevel();
				float saturation = e.getPlayer().getSaturation();
				
				boolean ignoreLimit = false;
				
				switch(MaterialWrapper.wrap(MaterialWrapper.getFromItemStack(e.getItem()))) {
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
					
					e.getPlayer().addPotionEffects(Arrays.asList(effects2));
					
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
			
			e.getPlayer().setMetadata(BooleanMeta.class.getName(), new BooleanMeta(true));

			Bukkit.getScheduler().scheduleSyncDelayedTask(main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					e.getPlayer().removeMetadata(BooleanMeta.class.getName(), main.getInstance());
					
				}
			}, 5);
			
			e.setCancelled(true);
		}
	}
}
