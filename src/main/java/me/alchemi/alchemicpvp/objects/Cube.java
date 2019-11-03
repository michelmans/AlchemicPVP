package me.alchemi.alchemicpvp.objects;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cube {

	private List<Plane> planes;
	
	private World world;
	
	public Cube(Vector min, Vector max, World w) {
		planes = Plane.getPlanesFromCube(min, max);
		world = w;
	}
	
	public Cube(Location min, Location max) {
		planes = Plane.getPlanesFromCube(min, max);
		world = min.getWorld();
	}
	
	public double getDistance(Location loc) {
		double dist = Integer.MAX_VALUE;
		for (Plane plane : planes) {
			if (plane.equals(planes.get(0))) continue;
			
			dist = Math.min(dist, plane.getDistance(loc));
		}
		return dist;
	}
	
	public int getClosestPlaneIndex(Location loc) {
		int planeIndex = 0;
		double dist = -1;
		int i = 0;
		for (Plane plane : planes) {
			double d = plane.getDistance(loc);
			
			if ((d < dist || dist == -1)
					&& (i > 0 && d != 0) ) {
				planeIndex = i;
				
				dist = d;
				i++;
				continue;
			}
			
			i++;
		}
		return planeIndex;
	}
	
	public Plane getClosestPlane(Location loc) {
		return getPlane(getClosestPlaneIndex(loc));
	}
	
	public void setClosestPlaneBlock(Material type, Player player){
		
		for(Plane p : planes) {
			if (p.getDistance(player.getLocation()) <= 5) p.placeBlockAt(player, type);
		}
		
	}
	
	public Plane getPlane(int planeIndex) {
		return planes.get(planeIndex);
	}
	
	public Plane getPlane(BlockFace face) {
		switch(face) {
		
		case DOWN:
			return planes.get(0);
			
		case UP:
			return planes.get(1);
			
		case EAST:
			return planes.get(2);
			
		case WEST:
			return planes.get(3);
			
		case NORTH:
			return planes.get(4);
		
		case SOUTH:
			return planes.get(5);
			
		default:
			return planes.get(0);
		
		}
	}
	
	public BlockFace getPlaneFace(Plane plane) {
		for (int i = 0; i < 6; i++) {
			if (planes.get(i).equals(plane)) {
				return i == 0 ? BlockFace.DOWN
						: i == 1 ? BlockFace.UP
						: i == 2 ? BlockFace.EAST
						: i == 3 ? BlockFace.WEST
						: i == 4 ? BlockFace.NORTH
						: i == 5 ? BlockFace.SOUTH
						: null;
			}
		}
		return null;
	}

	public World getWorld() {
		return world;
	}
	 
}
