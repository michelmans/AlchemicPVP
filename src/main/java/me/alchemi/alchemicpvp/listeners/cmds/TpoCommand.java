package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config.Messages;

public class TpoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission(command.getPermission())) {
			if (args.length >= 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					if (((Player)sender).teleport(Bukkit.getPlayer(args[0]), TeleportCause.COMMAND)) {
						sender.sendMessage(Messenger.formatString(Messages.TP_TELEPORT.value()));
						return true;
					} else {
						sender.sendMessage(Messenger.formatString(Messages.TP_PLAYEROFFLINE.value()
								.replace("$player$", args[0])));
						return true;
					}
				}
				sender.sendMessage(Messenger.formatString(Messages.TP_PLAYEROFFLINE.value()
						.replace("$player$", args[0])));
				return true;
			}
			sender.sendMessage(Messenger.formatString(Messages.TP_PLAYEROFFLINE.value()
					.replace("$player$", "Notch")));
		}
		return true;
	}

}
