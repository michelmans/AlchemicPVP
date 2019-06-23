package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.Library;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config.Messages;

public class TpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission(command.getPermission())) {
			if (args.length >= 3) {
				
				Location location = ((Player)sender).getLocation().clone();
				
				int i = 0;
				for (String arg : args) {
					if (Library.testIfNumber(arg)) {
						setPos(location, Double.valueOf(arg), i);
					} else if (arg.contains("~")) {
						arg = arg.replace("~", "");
						if (Library.testIfNumber(arg)) {
							addPos(location, Double.valueOf(arg), i);
						}
					} else {
						sender.sendMessage(Messenger.formatString(Messages.TP_NOLOCATION.value()));
						sender.sendMessage(Messenger.formatString(Messages.COMMANDS_WRONGFORMAT.value() + command.getUsage()));
						return true;
					}
					i++;
				}
				
				((Player)sender).teleport(location);
			}
			
		}
		return true;
	}
	
	public Location setPos(Location loc, double pos, int i) {
		if (i == 0) {
			loc.setX(pos);
		} else if (i == 1) {
			loc.setY(pos);
		} else if (i == 2) {
			loc.setZ(pos);
		}
		return loc;
	}

	public Location addPos(Location loc, double pos, int i) {
		if (i == 0) {
			loc.add(pos, 0, 0);
		} else if (i == 1) {
			loc.add(0, pos, 0);
		} else if (i == 2) {
			loc.add(0, 0, pos);
		}
		return loc;
	}
	
}
