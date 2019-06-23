package me.alchemi.alchemicpvp.listeners.cmds;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config.Messages;

public class GameModeCommand implements CommandExecutor {

	private static enum GameModes{
		
		ADVENTURE(GameMode.ADVENTURE, new String[] {"adventure", "a", "2"}),
		CREATIVE(GameMode.CREATIVE, new String[] {"creative", "c", "1"}),
		SURVIVAL(GameMode.SURVIVAL, new String[] {"survival", "s", "0"}),
		SPECTATOR(GameMode.SPECTATOR, new String[] {"spectator", "3"});
		
		private String[] aliases;
		private GameMode gamemode;
		
		private GameModes(GameMode gm, String[] aliases) {
			gamemode = gm;
			this.aliases = aliases;
		}
		
		private String[] getAliases() {
			return aliases;
		}
		
		private GameMode getGameMode() {
			return gamemode;
		}
		
		private static GameMode testMode(String testFor) {
			for (GameModes gm : values()) {
				for (String alias : gm.getAliases()) {
					if (testFor.equalsIgnoreCase(alias)) {
						return gm.getGameMode();
					}
				}	
			}
			
			for (GameModes gm : values()) {
				for (String alias : gm.getAliases()) {
					if (alias.contains(testFor)) {
						return gm.getGameMode();
					}
				}
			}
			return Bukkit.getDefaultGameMode();
		}
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission(command.getPermission())) {
			
			Player player = (Player) sender;
			GameMode mode = Bukkit.getDefaultGameMode();
			
			if (args.length > 1) {
				mode = GameModes.testMode(args[0]);
				player = Bukkit.getPlayer(args[1]);
				
				if (player == null) {
					sender.sendMessage(Messenger.formatString(Messages.GAMEMODE_PLAYEROFFLINE.value()
							.replace("$player$", args[1])));
					return true;
				}
				
			} else if (args.length == 1) {
				mode = GameModes.testMode(args[0]);
			}
			
			player.setGameMode(mode);
			
			if (!player.equals((Player)sender)) {
				sender.sendMessage(Messenger.formatString(Messages.GAMEMODE_SETOTHER.value()
						.replace("$player$", player.getDisplayName())
						.replace("$gamemode$", mode.name().toLowerCase())));
			}
			player.sendMessage(Messenger.formatString(Messages.GAMEMODE_SETOWN.value()
					.replace("$gamemode$", mode.name().toLowerCase())));
			
		}
		return true;
	}

}
