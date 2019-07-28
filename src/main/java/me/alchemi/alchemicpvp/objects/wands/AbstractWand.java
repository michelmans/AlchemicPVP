package me.alchemi.alchemicpvp.objects.wands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.alchemi.al.Library;
import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.objects.handling.ItemFactory;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.meta.BooleanMeta;
import me.alchemi.alchemicpvp.meta.SecondCooldownMeta;
import me.alchemi.alchemicpvp.meta.TaskIntMeta;

public abstract class AbstractWand extends ItemStack{

	public static final String CHARGELOCALIZEDNAME = "alchemicpvp:item.wand.charge";
	protected ItemFactory CHARGE = new ItemFactory(MaterialWrapper.FIRE_CHARGE.getMaterial()).setName("&6&oCharge").setLocalizedName(CHARGELOCALIZEDNAME);

	public static enum WandType {
		MAGIC(MagicWand.class), DRAGON(DragonStick.class);
		
		final Class<? extends AbstractWand> clazz;
		
		private WandType(Class<? extends AbstractWand> clazz) {
			this.clazz = clazz;
		}
		
		public AbstractWand getInstance() {
			if (clazz.equals(DragonStick.class)) return main.getInstance().dragon;
			else if (clazz.equals(MagicWand.class)) return main.getInstance().magic;
			else return null;
		}
	}
	
	public AbstractWand() {
		CHARGE.setNum(getSecondaryCooldown() * 2);
	}
	
	public ItemFactory getCharge() {
		return CHARGE.clone();
	}
	
	public static void givePlayer(Player player, WandType wand) {
		
		AbstractWand mw = wand.getInstance();
		
		Library.giveItemStack(mw, player);
		if (mw.getSecondaryCooldown() * 2 > player.getInventory().getMaxStackSize()) player.getInventory().setMaxStackSize(mw.getSecondaryCooldown() * 2);
		Library.giveItemStack(mw.CHARGE, player);
	}
	
	public static RayTraceResult getTarget(Player player) {
		Vector direction = player.getEyeLocation().getDirection();
		Location loc = player.getEyeLocation().add(direction.normalize());
		
		RayTraceResult ray = loc.getWorld().rayTraceEntities(loc, direction, 9, Entity -> Entity instanceof LivingEntity);
		
		return ray != null ? ray : loc.getWorld().rayTraceBlocks(loc, direction, 9);
	}
	
	
	public abstract int getCooldown();
	
	public abstract int getSecondaryCooldown();
	
	public abstract void onItemUse(PlayerInteractEvent e);

	public void fire(Player player, double d) {
		
		if (!PersistentMeta.hasMeta(player, SecondCooldownMeta.class)) {
			player.setMetadata(SecondCooldownMeta.class.getName(), new SecondCooldownMeta(this, player));
		}
		
		Vector direction = player.getEyeLocation().getDirection().normalize().multiply(2);
		
		Runnable runnable = new Runnable() {
			
			private int ran = 0;
			@Override
			public void run() {
				
				if (PersistentMeta.hasMeta(player, SecondCooldownMeta.class)) {
					((SecondCooldownMeta)PersistentMeta.getMeta(player, SecondCooldownMeta.class)).update();
				}
				
				Arrow arrow = player.launchProjectile(Arrow.class, direction);
				arrow.setFireTicks(100);
				arrow.setKnockbackStrength(0);
				arrow.setDamage(d);
				arrow.setBounce(false);
				arrow.setShooter(player);
				arrow.setTicksLived(5900);
				arrow.setCustomName("flame");
				
				if (ran >= 5) {
					Bukkit.getScheduler().cancelTask(PersistentMeta.getMeta(player, TaskIntMeta.class).asInt());
					player.removeMetadata(TaskIntMeta.class.getName(), main.getInstance());
					player.removeMetadata(BooleanMeta.class.getName(), main.getInstance());
				}
				ran ++;
			}
		};
		
		player.setMetadata(TaskIntMeta.class.getName(),
				new TaskIntMeta(Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getInstance(),
						runnable, 1, 1)));
		
	}
	
}
