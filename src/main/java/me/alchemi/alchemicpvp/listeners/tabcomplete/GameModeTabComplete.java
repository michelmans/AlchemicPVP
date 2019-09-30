package me.alchemi.alchemicpvp.listeners.tabcomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.PvP;

public class GameModeTabComplete implements TabCompleter {
    
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

		List<String> tabSuggest = new ArrayList<>();
		List<String> list = new ArrayList<>();
		
		if (!(sender instanceof Player))
			return tabSuggest;

		if (!PvP.getInstance().hasPermission(sender, cmd.getPermission()))
			return tabSuggest;
		
		if (args.length == 1) {
			
			list.add("adventure");
			list.add("survival");
			list.add("creative");
			list.add("spectator");
				
		} else if (args.length == 2) {
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				list.add(p.getName());
			}
			
		}

		for (int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).startsWith(args[args.length - 1]))
				tabSuggest.add(list.get(i));

		Collections.sort(tabSuggest);
		return tabSuggest;
	}
	
}
