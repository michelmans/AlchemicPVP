package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;

public class SmiteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (PvP.getInstance().hasPermission(sender, "alchemicpvp.smite")) {
			Location smite;
			if (args.length == 0) {
				if (sender instanceof Player) {
					smite = ((Player) sender).getLocation();
					PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_SMITTEN.value(), sender);
					PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_SENT.value().replace("$player$", sender.getName()), sender);
				} else {
					PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_PROVIDE.value(), sender);
					return true;
				}
			} else {
				if (Bukkit.getPlayer(args[0]) == null || !Bukkit.getPlayer(args[0]).isOnline()) {
					PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_PLAYEROFFLINE.value().replace("$player$", args[0]), sender);
					return true;
				}
				
				smite = Bukkit.getPlayer(args[0]).getLocation();
				
				PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_SMITTEN.value(), Bukkit.getPlayer(args[0]));
				PvP.getInstance().getMessenger().sendMessage(Messages.SMITE_SENT.value().replace("$player$", args[0]), sender);
				
			}
			
			smite.getWorld().strikeLightning(smite);
			
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
