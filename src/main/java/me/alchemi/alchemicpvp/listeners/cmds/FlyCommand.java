package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config.Messages;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission(command.getPermission())) {
			Player player = (Player) sender;
			
			if (args.length > 0) {
				player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(Messenger.formatString(Messages.CHECK_PLAYEROFFLINE.value()
							.replace("$player$", args[0])));
					return true;
				}
			}
			
			player.setAllowFlight(!player.getAllowFlight());
			
			if (player.getAllowFlight()) {
				player.sendMessage(Messenger.formatString(Messages.FLY_ENABLE.value()));
			} else {
				player.sendMessage(Messenger.formatString(Messages.FLY_DISABLE.value()));
			}
		}
		return true;
	}

}
