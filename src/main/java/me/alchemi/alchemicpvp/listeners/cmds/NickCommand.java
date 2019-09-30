package me.alchemi.alchemicpvp.listeners.cmds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicpvp.Config;
import me.alchemi.alchemicpvp.PvP;
import me.alchemi.alchemicpvp.Config.Messages;
import me.alchemi.alchemicpvp.meta.NickMeta;
import me.alchemi.alchemicpvp.meta.StatsMeta;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class NickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && PvP.getInstance().hasPermission(sender, "alchemicpvp.nick") && args.length == 1) {
			Player player = (Player) sender;
			
			Matcher m = Pattern.compile("&[1234567890klnor]").matcher(args[0]);
			
			if (args[0].length() > Config.Nickname.CHARACTERLIMIT.asInt()) {
				PvP.getInstance().getMessenger().sendMessage(Messages.NICK_TOOLONG.value()
						.replace("$name$", args[0])
						.replace("$amount$", Config.Nickname.CHARACTERLIMIT.asString()), sender);
				
				return true;
			} 
			
			if (m.find() && !PvP.getInstance().hasPermission(sender, "alchemicpvp.nick.format") && !Config.Nickname.ALLOW_FORMAT.asBoolean()) {
				sender.sendMessage(Messenger.formatString(Messages.NICK_NOFORMAT.value()
						.replace("$name$", args[0])
						.replace("$eg$", "&k, &6, &1, etc.")));
				return true;
			}
			
			if (WhoCommand.whoIs(args[0]) != null 
					&& !WhoCommand.whoIs(args[0]).equals(player)) {
				
				TextComponent mainComponent = new TextComponent(Messenger.formatString(Messages.NICK_TAKEN1.value().replace("$name$", args[0])));
				TextComponent clickComponent = new TextComponent("\n" + Messenger.formatString(Messages.NICK_TAKEN2.value()));
				clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
				clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
				mainComponent.addExtra(clickComponent);
				player.spigot().sendMessage(mainComponent);
				
				return true;
			}
			
			player.setDisplayName(args[0]);
			player.setMetadata(NickMeta.class.getName(), new NickMeta(args[0]));
			StatsMeta.getStats(player).setNickname(args[0]);
			
			PvP.getInstance().getMessenger().sendMessage(Messages.NICK_NEW.value()
					.replace("$name$", args[0])
					.replace("$player$", sender.getName()), sender);
			
		} else if (sender instanceof Player) {
			PvP.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
