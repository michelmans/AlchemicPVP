package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.meta.StatsMeta;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.reload")) {
			
			main.config.reload();
			main.getInstance().getMessenger().sendMessage("&9Configs reloaded.", sender);
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				StatsMeta.getStats(player).reload();
				main.getInstance().getSsb().addPlayer(player);
			}
			
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
