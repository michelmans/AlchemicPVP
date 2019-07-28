package me.alchemi.alchemicpvp.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import me.alchemi.alchemicpvp.Config;

public class Plane {

	private float minX;
	private float minY;
	private float minZ;
	
	private float maxX;
	private float maxY;
	private float maxZ;
	
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
	
	public List<Location> placeBlockAt(Location loc, Material type) {
		return placeBlockAt(loc, type, true);
	}
	
	public List<Location> placeBlockAt(Location loc, Material type, boolean applyPhysics) {
		
		List<Location> placed = new ArrayList<Location>();
		
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
					if (loc.getWorld().getBlockAt(x, y, z).isEmpty()) {
						placed.add(new Location(loc.getWorld(), x, y, z));
						loc.getWorld().getBlockAt(x, y, z).setType(type, applyPhysics);
					}
				}
			}
		}
		return placed;
	}
	
	public List<Location> placeBlockAt(World world, Material type) {
		return placeBlockAt(world, type, true);
	}
	
	public List<Location> placeBlockAt(World world, Material type, boolean applyPhysics) {
		
		List<Location> placed = new ArrayList<Location>();
		
		for (int x = (int)minX; x <= maxX; x++) {
			for (int y = (int)minY; y <= maxY; y++) {
				for (int z = (int)minZ; z <= maxZ; z++) {
					if (world.getBlockAt(x, y, z).isEmpty()) {
						placed.add(new Location(world, x, y, z));
						world.getBlockAt(x, y, z).setType(type, applyPhysics);
					}
				}
			}
		}
		return placed;
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

	public int getDistance(Location loc) {
		
		Vector support = new Vector(Math.floor((maxX-minX)/3) + minX, 
				Math.floor((maxY-minY)/3) + minY, 
				Math.floor((maxZ-minZ)/3) + minZ);
		
		Vector direction1 = support.clone().subtract(new Vector(minX, minY, minZ));
		Vector direction2 = support.clone().subtract(new Vector(maxX, maxY, maxZ));
		
		//(x, y, z) = support + r * direction1 + s * direction2
		Vector cross = direction1.getCrossProduct(direction2);
		
		//crossX(x - supportX) + crossY(y - supportY) + crossZ(z - supportZ) = 0
		int D = - (cross.getBlockX() * support.getBlockX()) 
				- (cross.getBlockY() * support.getBlockY()) 
				- (cross.getBlockZ() * support.getBlockZ());
		
		//|crossX * x + crossY * y + crossZ * z + D|
		double t = Math.abs(cross.getBlockX() * loc.getBlockX() 
				+ cross.getBlockY() * loc.getBlockY() 
				+ cross.getBlockZ() * loc.getBlockZ() + D);
		
		//sqrt(crossX^2 + crossY^2 + crossZ^2)
		double n = Math.sqrt(Math.pow(cross.getBlockX(), 2) 
				+ Math.pow(cross.getBlockY(), 2) 
				+ Math.pow(cross.getBlockZ(), 2));
		
//		System.out.println("ux + vy + wz + D = 0"
//				.replace("u", String.valueOf(cross.getBlockX()))
//				.replace("v", String.valueOf(cross.getBlockY()))
//				.replace("w", String.valueOf(cross.getBlockZ()))
//				.replace("D", String.valueOf(D)));
		
		return (int) Math.floor(t/n);
	}
	
	
	
}

