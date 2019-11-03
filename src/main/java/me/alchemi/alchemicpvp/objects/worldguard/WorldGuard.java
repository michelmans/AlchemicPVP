package me.alchemi.alchemicpvp.objects.worldguard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.alchemicpvp.Config.Worldguard;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.objects.Cube;

public class WorldGuard implements Listener{
	private com.sk89q.worldguard.WorldGuard wg;
	private RegionContainer container;
	
	public static StateFlag BORDER_FLAG = new StateFlag("visible-border", false);
	
	private List<Location> placedBorders = new ArrayList<Location>();
	
	public WorldGuard() {}
	
	public void onLoad() {
		wg = com.sk89q.worldguard.WorldGuard.getInstance();
		FlagRegistry reg = wg.getFlagRegistry();
		try {
			reg.register(BORDER_FLAG);
		} catch (FlagConflictException | IllegalStateException e) {
			BORDER_FLAG = (StateFlag)reg.get("visible-border");
		}
	}
	
	public void onEnable() {
		container = wg.getPlatform().getRegionContainer();
		Bukkit.getPluginManager().registerEvents(this, PvP.getInstance());
	}
	
	public boolean canPvP(Player player) {
		
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		return set.testState(localPlayer, Flags.PVP);
	}
	
	public boolean isPvPDenied(Location loc) {
		
		ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
		return set.testState(null, Flags.PVP);
		
	}
	
	public void sendPvPDeny(Player player) {
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		String message = set.queryValue(localPlayer, Flags.DENY_MESSAGE);
		localPlayer.printRaw(message.replace("%what%", "PvP"));
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		
		ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(e.getFrom()));
		if (set.testState(null, BORDER_FLAG)) {
			for (ProtectedRegion region : set) {
				if (region.getFlags().containsKey(BORDER_FLAG)) {
					
					BlockVector3 max = region.getMaximumPoint();
					BlockVector3 min = region.getMinimumPoint();
					Location minL = BukkitAdapter.adapt(e.getFrom().getWorld(), min);
					Location maxL = BukkitAdapter.adapt(e.getFrom().getWorld(), max);
					
					Cube c = new Cube(minL, maxL);
					if (c.getDistance(e.getTo()) <= 5) {
						
						int index = c.getClosestPlaneIndex(e.getPlayer().getLocation()); 
						List<Location> removeable = c.getPlane(index).placeBlockAt(e.getPlayer().getLocation(), Worldguard.VISIBLE_BORDER_BLOCK.asMaterial()); 
						Bukkit.getScheduler().runTaskLater(PvP.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								
								for (Location loc : removeable) {
									loc.getWorld().getBlockAt(loc).setType(MaterialWrapper.AIR.getMaterial());
								}
								
							}
						}, 50);
						
						if (!region.contains(BukkitAdapter.asBlockVector(e.getTo()))) {
							
							Location center = maxL.add(minL).multiply(0.5);
							Vector playerTPDir = e.getFrom().toVector().subtract(center.toVector()).normalize();
							e.getPlayer().teleport(e.getFrom().add(playerTPDir));
							e.setCancelled(true);
							
						}						
					}
				}
			}
		}
	}
	
	public void clearBorders() {
		for (Location loc : placedBorders) {
			loc.getWorld().getBlockAt(loc).setType(MaterialWrapper.AIR.getMaterial());
		}
		placedBorders.clear();
	}
}
