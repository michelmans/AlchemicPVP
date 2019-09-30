package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;

public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		
		if (sender instanceof Player && PvP.getInstance().hasPermission(sender, "alchemicpvp.spawn")) {
			
			Player player = (Player) sender;
			Config.SPAWN = player.getLocation();
			PvP.config.save();
			PvP.getInstance().getMessenger().sendMessage(Messages.CHECK_SPAWN.value()
					.replace("$x$", String.valueOf(player.getLocation().getBlockX()))
					.replace("$y$", String.valueOf(player.getLocation().getBlockY()))
					.replace("$z$", String.valueOf(player.getLocation().getBlockZ())), player);
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
