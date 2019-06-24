package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.listeners.CHECK;

public class CheckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.check")) {
			
			Player player = (Player) sender;
			if (main.getInstance().getCheckPlayer(player.getName()) != null) {
				main.getInstance().getMessenger().sendMessage(Messages.CHECK_ALREADYCHECK.value(), player);
				return true;
			}
			
			if (args.length > 0) {
				if (Bukkit.getPlayer(args[0]) == null) main.getInstance().getMessenger().sendMessage(Messages.CHECK_PLAYEROFFLINE.value().replace("$player$", args[0]), player);
				
				else {
					player.teleport(Bukkit.getPlayer(args[0]));
					main.getInstance().getMessenger().sendMessage(Messages.CHECK_TELEPORT.value().replace("$player$", args[0]), player);
				}
			}
			
			main.getInstance().registerCheck(new CHECK(player)); 
			main.getInstance().getMessenger().sendMessage(Messages.CHECK_ENABLE.value(), player);
			
		}  else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}