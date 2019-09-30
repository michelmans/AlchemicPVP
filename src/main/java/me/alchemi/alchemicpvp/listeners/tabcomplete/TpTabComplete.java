package me.alchemi.alchemicpvp.listeners.tabcomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.PvP;

public class TpTabComplete implements TabCompleter {
    
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

		List<String> tabSuggest = new ArrayList<>();
		List<String> list = new ArrayList<>();
		
		if (!(sender instanceof Player))
			return tabSuggest;

		if (!PvP.getInstance().hasPermission(sender, cmd.getPermission()))
			return tabSuggest;
		
		Location target = ((Player)sender).getTargetBlockExact(10) != null ? ((Player)sender).getTargetBlockExact(10).getLocation() : null;
		
		
		if (args.length == 1) {
			
			list.add("~ ~ ~");
			list.add("~ ~");
			list.add("~");
			
			if (target != null) {
				list.add(String.valueOf(target.getBlockX()) + ".5 " + String.valueOf(target.getBlockY() + 1) + " " + String.valueOf(target.getBlockZ()) + ".5");
				list.add(String.valueOf(target.getBlockX()) + ".5 " + String.valueOf(target.getBlockY() + 1));
				list.add(String.valueOf(target.getBlockX()) + ".5");
			}
				
		} else if (args.length == 2) {
			
			list.add("~ ~");
			list.add("~");
			
			if (target != null) {
				list.add(String.valueOf(target.getBlockY() + 1) + " " + String.valueOf(target.getBlockZ()) + ".5");
				list.add(String.valueOf(target.getBlockY() + 1));
			}
			
		} else if (args.length == 3) {
			list.add("~");
			
			if (target != null) {
				list.add(String.valueOf(target.getBlockZ()) + ".5");
			}
		}

		for (int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).startsWith(args[args.length - 1]))
				tabSuggest.add(list.get(i));

		Collections.sort(tabSuggest);
		return tabSuggest;
	}
	
}
