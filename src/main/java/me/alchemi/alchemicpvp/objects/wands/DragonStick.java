package me.alchemi.alchemicpvp.objects.wands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.Config.Wands;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.meta.CooldownMeta;
import me.alchemi.alchemicpvp.meta.SecondCooldownMeta;

public class DragonStick extends AbstractWand implements Listener{

	public static final String LOCALIZEDNAME = "alchemicpvp:item.wand.dragon";
	private static final String NAME = Messenger.formatString(Wands.DRAGON_NAME.asString());
	private static final List<String> LORE = Wands.DRAGON_LORE.asStringList();
	private static final Material MATERIAL = Wands.DRAGON_MATERIAL.asMaterial();
	private static final boolean ENCHANTED = Wands.DRAGON_ENCHANTED.asBoolean();
	private static final int COOLDOWN = Wands.DRAGON_COOLDOWN.asInt();
	private static final int SECONDARYCOOLDOWN = Wands.DRAGON_SECONDARYCOOLDOWN.asInt();
	private static final double SECONDARYDAMAGE = Wands.DRAGON_SECONDARYDAMAGE.asDouble();
	
	public DragonStick() {
		setAmount(1);
		setType(MATERIAL);
		
		ItemMeta meta = getItemMeta();
		if (ENCHANTED) meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
		meta.setLore(LORE);
		meta.setDisplayName(NAME);
		meta.setLocalizedName(LOCALIZEDNAME);
		setItemMeta(meta);
		
		Bukkit.getPluginManager().registerEvents(this, main.getInstance());
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
			
		} else if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLocalizedName() && e.getItem().getItemMeta().getLocalizedName().equals(LOCALIZEDNAME) ) {
			
			if (main.worldGuard && !main.getInstance().getWorldGuard().canPvP(e.getPlayer())) {
				main.getInstance().getWorldGuard().sendPvPDeny(e.getPlayer());
				return;
			}
			
			if ((e.getAction().equals(Action.LEFT_CLICK_AIR)
					|| e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					&& (!PersistentMeta.hasMeta(e.getPlayer(), CooldownMeta.class)
							|| PersistentMeta.getMeta(e.getPlayer(), CooldownMeta.class).asBoolean())) {
				
				Vector direction = e.getPlayer().getEyeLocation().getDirection().normalize();
				
				Fireball fireball = e.getPlayer().launchProjectile(Fireball.class, direction.multiply(4));
				
				fireball.setGravity(true);
				fireball.setIsIncendiary(true);
				
				e.getPlayer().setMetadata(CooldownMeta.class.getName(), new CooldownMeta(COOLDOWN, e.getPlayer()));
				
			} else if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)
					|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& (!PersistentMeta.hasMeta(e.getPlayer(), SecondCooldownMeta.class) 
							|| !PersistentMeta.getMeta(e.getPlayer(), SecondCooldownMeta.class).asBoolean())) {	
				
				fire(e.getPlayer(), SECONDARYDAMAGE);
				
			}
		}
	}

}
