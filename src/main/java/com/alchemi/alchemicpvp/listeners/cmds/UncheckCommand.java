package com.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.CHECK;

public class UncheckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.check")) {
			Player player = (Player) sender;
			CHECK check = main.getInstance().getCheckPlayer(player.getName());
			if (check == null) {
				main.getInstance().getMessenger().sendMessage(MESSAGES.CHECK_NOTCHECK.value(), player);
				return true;
			}
			
			check.remove();
			
			player.getInventory().clear();
			for (PotionEffect pe : player.getActivePotionEffects()) {
				player.removePotionEffect(pe.getType());
			}
			if (args.length <= 0 || args[0].equals("true")) player.teleport(Config.SPAWN);
			player.setAllowFlight(false);
			
			main.getInstance().getMessenger().sendMessage(MESSAGES.CHECK_UNCHECK.value(), sender);
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		
		return true;
	}

}
