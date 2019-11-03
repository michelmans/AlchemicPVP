package me.alchemi.alchemicpvp.objects;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Trident;

import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.meta.CooldownMeta;
import me.alchemi.alchemicpvp.meta.SecondCooldownMeta;

public class Acetone {

	public static void run(World world, OfflinePlayer player) {
		
		if (player.isOnline()) {
			if (PersistentMeta.hasMeta(player.getPlayer(), CooldownMeta.class)) player.getPlayer().removeMetadata(CooldownMeta.class.getName(), PvP.getInstance());
			if (PersistentMeta.hasMeta(player.getPlayer(), SecondCooldownMeta.class)) player.getPlayer().removeMetadata(SecondCooldownMeta.class.getName(), PvP.getInstance());
		}
		
		for (Entity e : world.getEntitiesByClasses(Trident.class, Arrow.class, Fireball.class, DragonFireball.class)) {
			Projectile t = (Projectile)e;
			
			if (t.getShooter() == null
					|| !(t.getShooter() instanceof Player)) t.remove();
			else if (((Player)t.getShooter()).getUniqueId().equals(player.getUniqueId())) t.remove();
			
		}
		
		for (Tameable w : world.getEntitiesByClass(Tameable.class)) {
			if (w.getOwner() != null 
					&& w.getOwner().getUniqueId().equals(player.getUniqueId())) w.remove();
		}
	}
	
}
