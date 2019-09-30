package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.listeners.SpyListener;

public class SpyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && PvP.getInstance().hasPermission(sender, "alchemicpvp.spy")) {
			
			
			if (args.length > 1 && args[0].equalsIgnoreCase("ignore") && PvP.getInstance().spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					PvP.getInstance().getMessenger().sendMessage(Messages.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
					return true;
				}
				PvP.getInstance().spies.get(sender.getName()).ignorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			} else if (args.length > 1 && args[0].equalsIgnoreCase("unignore") && PvP.getInstance().spies.containsKey(sender.getName())) {
				
				if (Bukkit.getPlayer(args[1]) == null) {
					PvP.getInstance().getMessenger().sendMessage(Messages.SPY_PLAYEROFFLINE.value().replace("$player$", args[1]), sender);
					return true;
				}
				PvP.getInstance().spies.get(sender.getName()).unIgnorePlayer(Bukkit.getPlayer(args[1]));
				return true;
			}
			
			
			if (PvP.getInstance().spies.containsKey(sender.getName())) {
				PvP.getInstance().spies.remove(sender.getName());
				PvP.getInstance().getMessenger().sendMessage(Messages.SPY_STOP.value(), sender);
			} else {
				PvP.getInstance().spies.put(sender.getName(), new SpyListener((Player) sender));
				PvP.getInstance().getMessenger().sendMessage(Messages.SPY_START.value(), sender);
			}
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		return true;
	}
	
}
