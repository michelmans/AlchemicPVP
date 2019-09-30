package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.listeners.CHECK;

public class UncheckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && PvP.getInstance().hasPermission(sender, "alchemicpvp.check")) {
			Player player = (Player) sender;
			CHECK check = PvP.getInstance().getCheckPlayer(player.getName());
			if (check == null) {
				PvP.getInstance().getMessenger().sendMessage(Messages.CHECK_NOTCHECK.value(), player);
				return true;
			}
			
			check.remove();
			
			player.getInventory().clear();
			for (PotionEffect pe : player.getActivePotionEffects()) {
				player.removePotionEffect(pe.getType());
			}
			if (args.length <= 0 || args[0].equals("true")) player.teleport(Config.SPAWN);
			player.setAllowFlight(false);
			
			PvP.getInstance().getMessenger().sendMessage(Messages.CHECK_DISABLE.value(), sender);
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		
		return true;
	}

}
