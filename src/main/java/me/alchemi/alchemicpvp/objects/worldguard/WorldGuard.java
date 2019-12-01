package me.alchemi.alchemicpvp.objects.worldguard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.alchemi.alchemicpvp.Config.Worldguard;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.objects.Cube;

public class WorldGuard implements Listener{
	private com.sk89q.worldguard.WorldGuard wg;
	private RegionContainer container;
	
	public static StateFlag BORDER_FLAG = new StateFlag("visible-border", false);
	
	private Map<String, Cube> regions = new HashMap<String, Cube>();
	
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
		
		for (World w : Bukkit.getWorlds()) {
			for (Entry<String, ProtectedRegion> region : container.get(BukkitAdapter.adapt(w)).getRegions().entrySet()) {
				if (region.getValue().getFlags().containsKey(BORDER_FLAG)) {
					regions.put(region.getKey(), 
							new Cube(BukkitAdapter.adapt(w, region.getValue().getMinimumPoint()), 
									BukkitAdapter.adapt(w, region.getValue().getMaximumPoint())));
				}
			}
		}
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
					
					Cube c = regions.get(region.getId());
					
					new BukkitRunnable() {

						@Override
						public void run() {
							
							if (c.getDistance(e.getTo()) <= 5) {
								
								c.setClosestPlaneBlock(Worldguard.VISIBLE_BORDER_BLOCK.asMaterial(), e.getPlayer());
								
								if (!region.contains(BukkitAdapter.asBlockVector(
												e.getTo().add(e.getTo().getDirection().normalize().multiply(2))))) {
									
									Location center = BukkitAdapter.adapt(e.getFrom().getWorld(), region.getMinimumPoint())
											.add(BukkitAdapter.adapt(e.getFrom().getWorld(), region.getMaximumPoint()))
											.multiply(0.5);
									
									Vector playerTPDir = e.getFrom().toVector().subtract(center.toVector()).normalize();
									
									e.setCancelled(true);
									
									new BukkitRunnable() {
										
										@Override
										public void run() {
											e.getPlayer().teleport(e.getFrom().subtract(playerTPDir));
										}
									}.runTask(PvP.getInstance());
									
								}						
							}
							
						}
						
					}.runTaskAsynchronously(PvP.getInstance());
				}
			}
		}
	}
}
