package me.alchemi.alchemicpvp.meta;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.objects.handling.ItemFactory;
import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.objects.wands.AbstractWand;

public class SecondCooldownMeta extends BaseMeta {

	private int remaining;
	private final int originalRemaining;
	private final int cooldown;
	private int itemSlot;
	private boolean value;
	public Player player;
	private ItemFactory charge;
	
	public SecondCooldownMeta(AbstractWand wand, Player player) {
		super(PvP.getInstance(), false);
		value = false;
		
		remaining = wand.getSecondaryUse() * 20;
		originalRemaining = wand.getSecondaryUse() * 20;
		cooldown = wand.getSecondaryCooldown() * 20;
		this.player = player;
		this.charge = wand.getCharge();
		
		itemSlot = getSlotWithLocale(AbstractWand.CHARGELOCALIZEDNAME, player.getInventory());
	}
	
	@Override
	public boolean asBoolean() {
		
		return value;
	}
	
	@Override
	public int asInt() {
		
		return remaining;
		
	}
	
	public static int getSlotWithLocale(String localizedName, Inventory inventory) {
		int slot = 0;
		for (ItemStack item : inventory) {
			if (item != null 
					&& item.hasItemMeta() 
					&& item.getItemMeta().hasLocalizedName()
					&& item.getItemMeta().getLocalizedName().equals(localizedName)) {
				
				return slot;
			}
			slot++;
		}
		return -1;
	}
	
	public static boolean hasSlotLocale(int slot, String localizedName, Inventory inventory) {
		return slot != -1 ? 
				inventory.getItem(slot) != null ? 
					inventory.getItem(slot).hasItemMeta() ? 
							inventory.getItem(slot).getItemMeta().hasLocalizedName() ? 
									inventory.getItem(slot).getItemMeta().getLocalizedName().equals(localizedName): false : false : false : false;
	}
	
	public void update() {
		remaining--;
		
		if ( remaining%20 == 0 ) {
			if (!hasSlotLocale(itemSlot, AbstractWand.CHARGELOCALIZEDNAME, player.getInventory())) {
				itemSlot = getSlotWithLocale(AbstractWand.CHARGELOCALIZEDNAME, player.getInventory());
			}
			
			if (itemSlot == -1) itemSlot = player.getInventory().firstEmpty();
			
			player.getInventory().setItem(itemSlot, charge.setNum(remaining/20).setLocalizedName(AbstractWand.CHARGELOCALIZEDNAME));
		}
		
		if (remaining == 0) {
			value = true;
			
			int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(getOwningPlugin(), new Runnable() {
				
				private double amount;
				private int cooldownRemaining = 0;
				
				@Override
				public void run() {
					
					cooldownRemaining += 1;
					amount = (double)cooldownRemaining/(double)cooldown * (double)originalRemaining;
					
					if (amount%1 == 0) player.getInventory().setItem(itemSlot, charge.setNum((int)amount).setLocalizedName(AbstractWand.CHARGELOCALIZEDNAME));
					
				}
			}, 10, 20);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this.getOwningPlugin(), new Runnable() {
				
				@Override
				public void run() {
					
					remaining = originalRemaining;
					value = false;
					charge.setAmount(originalRemaining/20);
					player.getInventory().setItem(itemSlot, charge.setLocalizedName(AbstractWand.CHARGELOCALIZEDNAME));
					Bukkit.getScheduler().cancelTask(task);
					
				}
			}, cooldown);
			
		} 
		
	}

}
