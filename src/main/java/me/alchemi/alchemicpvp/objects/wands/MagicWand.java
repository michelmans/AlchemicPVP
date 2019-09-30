package me.alchemi.alchemicpvp.objects.wands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.Config.Wands;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.meta.CooldownMeta;
import me.alchemi.alchemicpvp.meta.SecondCooldownMeta;

public class MagicWand extends AbstractWand implements Listener{

	public static final String LOCALIZEDNAME = "alchemicpvp:item.wand.magic";
	private static final String NAME = Messenger.formatString(Wands.MAGIC_NAME.asString());
	private static final List<String> LORE = Wands.MAGIC_LORE.asStringList();
	private static final Material MATERIAL = Wands.MAGIC_MATERIAL.asMaterial();
	private static final boolean ENCHANTED = Wands.DRAGON_ENCHANTED.asBoolean();
	private static final int COOLDOWN = Wands.MAGIC_COOLDOWN.asInt();
	private static final int SECONDARYCOOLDOWN = Wands.MAGIC_SECONDARYCOOLDOWN.asInt();
	private static final double SECONDARYDAMAGE = Wands.MAGIC_SECONDARYDAMAGE.asDouble();
	
	public MagicWand() {
		
		setAmount(1);
		setType(MATERIAL);
		
		ItemMeta meta = getItemMeta();
		if (ENCHANTED) meta.addEnchant(Enchantment.CHANNELING, 1, true);
		meta.setLore(LORE);
		meta.setDisplayName(NAME);
		meta.setLocalizedName(LOCALIZEDNAME);
		setItemMeta(meta);
		
		Bukkit.getPluginManager().registerEvents(this, PvP.getInstance());
	}
	
	@Override
	public int getCooldown() {
		return COOLDOWN;
	}
	
	@Override
	public int getSecondaryCooldown() {
		return SECONDARYCOOLDOWN;
	}
	
	@EventHandler
	@Override
	public void onItemUse(PlayerInteractEvent e) {
		
		if ( e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLocalizedName() && e.getItem().getItemMeta().getLocalizedName().equals(CHARGELOCALIZEDNAME)) {
			
			if (Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(e.getAction())) e.setCancelled(true);
			
		} else if ( e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLocalizedName() && e.getItem().getItemMeta().getLocalizedName().equals(LOCALIZEDNAME)) {
			
			if (PvP.worldGuard && !PvP.getInstance().getWorldGuard().canPvP(e.getPlayer())) {
				PvP.getInstance().getWorldGuard().sendPvPDeny(e.getPlayer());
				return;
			}
			
			RayTraceResult ray = AbstractWand.getTarget(e.getPlayer());
			Location target = ray != null ? ray.getHitPosition().toLocation(e.getPlayer().getWorld()) : null; 
			
			
			
			if ((e.getAction().equals(Action.LEFT_CLICK_AIR)
					|| e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					&& (!PersistentMeta.hasMeta(e.getPlayer(), CooldownMeta.class)
							|| PersistentMeta.getMeta(e.getPlayer(), CooldownMeta.class).asBoolean())) {
				
				if (target == null) {
					Vector direction = e.getPlayer().getEyeLocation().getDirection().normalize().multiply(10);
					Location eyeLoc = e.getPlayer().getEyeLocation();
					
					target = eyeLoc.add(direction);
				}
				
				if (PvP.worldGuard && !PvP.getInstance().getWorldGuard().isPvPDenied(target)) {
					PvP.getInstance().getWorldGuard().sendPvPDeny(e.getPlayer());
					return;
				}
				
				e.getPlayer().setMetadata(CooldownMeta.class.getName(), new CooldownMeta(COOLDOWN, e.getPlayer()));
				target.getWorld().strikeLightning(target);
				if (ray != null && ray.getHitEntity() != null) {
					ray.getHitEntity().setLastDamageCause(new EntityDamageByEntityEvent(e.getPlayer(), ray.getHitEntity(), DamageCause.LIGHTNING, 5));
				}
				
			} else if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)
					|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& (!PersistentMeta.hasMeta(e.getPlayer(), SecondCooldownMeta.class) 
							|| !PersistentMeta.getMeta(e.getPlayer(), SecondCooldownMeta.class).asBoolean())) {	
				
				fire(e.getPlayer(), SECONDARYDAMAGE);
				
			}
		}
	}
}
