package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.listeners.CHECK;

public class UncheckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.check")) {
			Player player = (Player) sender;
			CHECK check = main.instance.getCheckPlayer(player.getName());
			if (check == null) {
				main.messenger.sendMessage("Check.NotCheck", player);
				return true;
			}
			
			check.remove();
			
			player.getInventory().clear();
			for (PotionEffect pe : player.getActivePotionEffects()) {
				player.removePotionEffect(pe.getType());
			}
			player.teleport(Config.SPAWN);
			player.setAllowFlight(false);
			
			main.messenger.sendMessage("Check.Uncheck", sender);
			
		} else if (sender instanceof Player) {
			main.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/uncheck");
				}
			});
		}
		
		
		return true;
	}

}
