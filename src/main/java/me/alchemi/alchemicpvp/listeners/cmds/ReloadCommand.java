package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.Config.Storage;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.meta.StatsMeta;
import me.alchemi.alchemicpvp.stats.DataType;
import me.alchemi.alchemicpvp.stats.YMLStats;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player 
				&& PvP.getInstance().hasPermission(sender, "alchemicpvp.reload")
				&& args.length == 1
				&& args[0].equals("reload")) {
			
			PvP.config.reload();
			PvP.getInstance().getMessenger().sendMessage("&9Configs reloaded.", sender);
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (DataType.valueOf(Storage.TYPE.asString()) == DataType.YML) ((YMLStats) StatsMeta.getStats(player)).reload();
				PvP.getInstance().getSsb().addPlayer(player);
			}
			
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
