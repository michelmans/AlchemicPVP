package me.alchemi.alchemicpvp.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.Config.Worldguard;
import me.alchemi.alchemicpvp.PvP;

public class Plane {

	private float minX;
	private float minY;
	private float minZ;
	
	private float maxX;
	private float maxY;
	private float maxZ;
	
	private String equation;
	private Vector normal;
	private double d;
	
	public Plane(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		
		if (minX != maxX && minY != maxY && minZ != maxZ) {
			throw new IllegalArgumentException("At least 2 coords need to be the same");
		}
		
		Vector support = minZ == maxZ ? new Vector(maxX, minY, minZ)
						: minX == maxX ? new Vector(minX, minY, maxZ)
						: minY == maxY ? new Vector(maxX, minY, minZ) : null; 
		
		Vector dir1 = support.clone().subtract(new Vector(minX, minY, minZ));
		Vector dir2 = support.clone().subtract(new Vector(maxX, maxY, maxZ));
		
		//normal = 
		normal = new Vector(dir1.getY() * dir2.getZ() - dir1.getZ() * dir2.getY(), -(dir1.getX() * dir2.getZ() - dir1.getZ() * dir2.getX()), dir1.getX() * dir2.getY() - dir1.getY() * dir2.getX()); 
		
		//D = nX * supportX + nY * supportY + nZ * supportZ;
		d = normal.getX() * minX + normal.getY() * minY + normal.getZ() * minZ;
		
		//Plane: nX * x + nY * y + nZ * z = D
		equation = normal.getBlockX() + "x + " + normal.getBlockY() + "y + " + normal.getBlockZ() + "z = " + Math.round(d);
	}

	public Location intersect(Vector vec, Location loc) {
		
		Vector testVector = vec.clone().normalize();
		
		for (double i = testVector.length() ; i < Math.max(maxX - minX, maxZ - minZ); i++) {
			vec.add(testVector);
			
			if ((isBetween(vec.getBlockX(), minX, maxX) && isBetween(vec.getBlockZ(), minZ, maxZ))
					&& isBetween(vec.getBlockY(), minY, maxY)) {
				return new Location(loc.getWorld(), vec.getX(), vec.getY(), vec.getZ());
			}
				
		}
		
		return null;
		
	}
	
	public static List<Plane> getPlanesFromCube(Location min, Location max){
		return getPlanesFromCube(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
	}
	
	public static List<Plane> getPlanesFromCube(Vector min, Vector max){
		return getPlanesFromCube(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
	}
	
	public static List<Plane> getPlanesFromCube(float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		List<Plane> list = new ArrayList<Plane>();
		
		list.add(new Plane(minX, minY, minZ, maxX, minY, maxZ));
		list.add(new Plane(minX, maxY, minZ, maxX, maxY, maxZ));
		
		list.add(new Plane(maxX, minY, minZ, maxX, maxY, maxZ));
		list.add(new Plane(minX, minY, minZ, minX, maxY, maxZ));
		
		list.add(new Plane(minX, minY, minZ, maxX, maxY, minZ));
		list.add(new Plane(minX, minY, maxZ, maxX, maxY, maxZ));
		
		return list;
	}
	
	public void placeBlockAt(Player player, Material type) {
		
		Location loc = player.getLocation();
		
		int x1 = (int) minX;
		int x2 = (int) maxX;
		int y1 = (int) minY;
		int y2 = (int) maxY;
		int z1 = (int) minZ;
		int z2 = (int) maxZ;
		
		if (x1 == x2) {
			y1 = loc.getBlockY() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			y2 = loc.getBlockY() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			
			z1 = loc.getBlockZ() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			z2 = loc.getBlockZ() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
		} else if (y1 == y2) {
			x1 = loc.getBlockX() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			x2 = loc.getBlockX() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			
			z1 = loc.getBlockZ() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			z2 = loc.getBlockZ() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
		} else if (z1 == z2) {
			y1 = loc.getBlockY() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			y2 = loc.getBlockY() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			
			x1 = loc.getBlockX() - Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
			x2 = loc.getBlockX() + Config.Worldguard.VISIBLE_BORDER_LEEWAY.asInt();
		}
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					Location temp = new Location(loc.getWorld(), x, y, z);
					
					if (temp.getBlockX() > maxX || temp.getBlockX() < minX
							|| temp.getBlockY() > maxY || temp.getBlockY() < minY
							|| temp.getBlockZ() > maxZ || temp.getBlockZ() < minZ) continue;
					
					player.sendBlockChange(temp, Worldguard.VISIBLE_BORDER_BLOCK.asMaterial().createBlockData());
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							
							player.sendBlockChange(temp, temp.getBlock().getBlockData());
							
						}
					}.runTaskLater(PvP.getInstance(), 50);
					
				}
			}
		}
	}
	
	public final boolean isBetween(int test, float min, float max) {
		return min <= (float)test && (float)test <= max;
	}
	
	/**
	 * @return the minX
	 */
	public final float getMinX() {
		return minX;
	}

	/**
	 * @param minX the minX to set
	 */
	public final void setMinX(float minX) {
		this.minX = minX;
	}

	/**
	 * @return the minY
	 */
	public final float getMinY() {
		return minY;
	}

	/**
	 * @param minY the minY to set
	 */
	public final void setMinY(float minY) {
		this.minY = minY;
	}

	/**
	 * @return the minZ
	 */
	public final float getMinZ() {
		return minZ;
	}

	/**
	 * @param minZ the minZ to set
	 */
	public final void setMinZ(float minZ) {
		this.minZ = minZ;
	}

	/**
	 * @return the maxX
	 */
	public final float getMaxX() {
		return maxX;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public final void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	/**
	 * @return the maxY
	 */
	public final float getMaxY() {
		return maxY;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public final void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	/**
	 * @return the maxZ
	 */
	public final float getMaxZ() {
		return maxZ;
	}

	/**
	 * @param maxZ the maxZ to set
	 */
	public final void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
	}

	public double getDistance(Location loc) {
		
		//L: <x, y, z> = location + t*normal
		// x = locX + t * nX
		// y = locY + t * nY
		// z = locZ + t * nZ
		// t = (d - nX*locX - nY*locY -nZ*locZ)/(nX^2+nY^2+nZ^2)
		double t = (d - normal.getX() * loc.getX() - normal.getY() * loc.getY() - normal.getZ() * loc.getZ())/(Math.pow(normal.getX(), 2) + Math.pow(normal.getY(), 2) + Math.pow(normal.getZ(), 2));
		
		Vector junction = new Vector(loc.getX() + t * normal.getX(), loc.getY() + t * normal.getBlockY(), loc.getZ() + t * normal.getZ());
		
		return junction.distance(loc.toVector());
	}

	@Override
	public String toString() {
		return "Plane:{ (" + minX + ", " + minY + ", " + minZ + ") --> (" + maxX + ", " + maxY + ", " + maxZ + "), equation: " + equation + "}";
	}
	
}

