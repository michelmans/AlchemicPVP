package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.alchemicpvp.objects.wands.AbstractWand;
import me.alchemi.alchemicpvp.objects.wands.AbstractWand.WandType;

public class GiveWandCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && args.length == 1) {
			AbstractWand.givePlayer((Player)sender, WandType.valueOf(args[0].toUpperCase()));
		}
		return true;
	}

}
